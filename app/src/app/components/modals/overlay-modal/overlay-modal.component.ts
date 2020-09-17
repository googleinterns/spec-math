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
import {
  OverlayRequest,
  ModalInterface,
  FileUploadOptions,
} from 'src/shared/interfaces';
import { SpecMathService } from 'src/shared/services/specmath.service';
import { SpecMathModal, StepOptions } from '../spec-math-modal';

enum Steps {
  specNameInput = 0,
  defaultsFileUpload = 1,
  specFileUpload = 2,
  confirmOperation = 3,
}

const OVERLAY_STEP_LIST: StepOptions = {
  [Steps.specNameInput]: {
    toolTipText: 'You must name your new spec',
    nextStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next',
    stepLabel: 'Name new spec',
  },
  [Steps.defaultsFileUpload]: {
    toolTipText: 'You must upload a defaults file',
    nextStep: Steps.specFileUpload,
    previousStep: Steps.specNameInput,
    nextButtonText: 'Next',
    stepLabel: 'Defaults file',
  },
  [Steps.specFileUpload]: {
    toolTipText: 'You must upload a spec file',
    nextStep: Steps.confirmOperation,
    previousStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next',
    stepLabel: 'Spec file',
  },
  [Steps.confirmOperation]: {
    previousStep: Steps.specFileUpload,
    nextButtonText: 'Confirm',
    stepLabel: 'Confirm operation',
    lastStep: true,
  },
};

@Component({
  selector: 'app-modal',
  templateUrl: './overlay-modal.component.html',
  styleUrls: ['../spec-math-modal.scss'],
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
      overlay: await readFileAsString(this.fileUploadOptions.file),
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

  handleFileUploadOptions(fileUploadOptions: FileUploadOptions) {
    switch (fileUploadOptions.type) {
      case 'defaults':
        this.fileUploadOptions = fileUploadOptions;
        break;
      case 'spec':
        this.specFilesUploadOptions = {
          specFiles: [fileUploadOptions.file],
          valid: true,
        };
        break;
    }
  }

  get defaultsFileValid(): boolean {
    return this.fileUploadOptions.file !== null;
  }

  get nextButtonEnabled(): boolean {
    switch (this.currentStep) {
      case Steps.specNameInput:
        return this.specNameInputOptions.valid;
      case Steps.specFileUpload:
        return this.specFilesUploadOptions.valid;
      case Steps.defaultsFileUpload:
        return this.defaultsFileValid;
      default:
        return true;
    }
  }

  ngOnInit() {
    this.stepList = OVERLAY_STEP_LIST;
    this.currentStep = Steps.specNameInput;
    this.operationType = 'overlay';
  }
}
