// Copyright 2020 Google LLC

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     https://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatStepper } from '@angular/material/stepper';
import { readFileAsString } from 'src/shared/functions';
import { OverlayRequest, ModalInterface } from 'src/shared/interfaces';
import { SpecMathService } from 'src/shared/services/specmath.service';
import { SpecMathModal, StepOptions, Steps } from '../modal';

const OVERLAY_STEP_LIST: StepOptions = {
  [Steps.specNameInput]: {
    toolTipText: 'You must name your new spec',
    nextStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next',
    stepLabel: 'Name new spec',
  },
  [Steps.defaultsFileUpload]: {
    toolTipText: 'You must upload a default file',
    nextStep: Steps.specFilesUpload,
    previousStep: Steps.specNameInput,
    nextButtonText: 'Next',
    stepLabel: 'Defaults file',
  },
  [Steps.specFilesUpload]: {
    toolTipText: 'You must upload a spec file',
    nextStep: Steps.confirmOperation,
    previousStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next',
    stepLabel: 'Spec files',
  },
  [Steps.confirmOperation]: {
    previousStep: Steps.specFilesUpload,
    nextButtonText: 'Confirm',
    stepLabel: 'Confirm operation',
    lastStep: true,
  },
};

@Component({
  selector: 'app-modal',
  templateUrl: './overlay-modal.component.html',
  styleUrls: ['./overlay-modal.component.scss'],
})
export class OverlayModalComponent
  extends SpecMathModal
  implements OnInit, ModalInterface {
  loadingOperation: boolean;
  constructor(
    readonly dialogRef: MatDialogRef<OverlayModalComponent>,
    readonly cdr: ChangeDetectorRef,
    readonly specMathService: SpecMathService
  ) {
    super(dialogRef, cdr, specMathService);
  }

  async nextStep(stepper: MatStepper) {
    const currStep = this.stepList[this.currentStep];

    if (currStep.lastStep) {
      this.loadingOperation = true;
      await this.overlayOperation().then(() => {
        this.loadingOperation = false;
      });
      this.cdr.detectChanges();
      this.finalizeSteps();
      return;
    }

    stepper.selectedIndex = ++this.currentStep;
  }

  async generateOverlaySet(): Promise<OverlayRequest> {
    const requestBody: OverlayRequest = {
      spec: await readFileAsString(this.specFilesUploadOptions.specFiles[0]),
      overlay: await readFileAsString(
        this.defaultsFileUploadOptions.defaultsFile
      ),
    };
    return requestBody;
  }

  async overlayOperation() {
    const overlaySet = await this.generateOverlaySet();
    const callResponse = await this.specMathService
      .overlaySpecs(overlaySet)
      .toPromise();
    this.resultSpec = new File(
      [callResponse.result],
      `${this.specNameInputOptions.newFileName}.yaml`
    );
  }

  ngOnInit() {
    this.stepList = OVERLAY_STEP_LIST;
  }
}
