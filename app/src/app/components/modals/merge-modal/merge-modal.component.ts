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
import {
  ResolvedMergeConflictOptions,
  MergeRequest,
  ModalInterface,
} from 'src/shared/interfaces';
import { SpecMathService } from 'src/shared/services/specmath.service';
import { readFileAsString } from 'src/shared/functions';
import { SpecMathModal, StepOptions, Steps } from '../modal';

const MERGE_STEP_LIST: StepOptions = {
  [Steps.specNameInput]: {
    toolTipText: 'You must name your new spec',
    nextStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next',
    stepLabel: 'Name new spec',
  },
  [Steps.defaultsFileUpload]: {
    nextStep: Steps.specFilesUpload,
    previousStep: Steps.specNameInput,
    nextButtonText: 'Next',
    stepLabel: 'Defaults file',
  },
  [Steps.specFilesUpload]: {
    toolTipText: 'You must upload a set of spec files',
    nextStep: Steps.confirmOperation,
    previousStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next',
    stepLabel: 'Spec files',
  },
  [Steps.confirmOperation]: {
    previousStep: Steps.specFilesUpload,
    nextButtonText: 'Confirm',
    stepLabel: 'Confirm operation',
    lastBaseStep: true,
  },
  [Steps.resolveConflicts]: {
    previousStep: Steps.confirmOperation,
    toolTipText: 'You must resolve all merge conflicts',
    nextButtonText: 'Resolve',
    stepLabel: 'Resolving conflicts',
  },
};

@Component({
  selector: 'app-merge-modal',
  templateUrl: './merge-modal.component.html',
  styleUrls: ['../modal-styling.scss'],
})
export class MergeModalComponent
  extends SpecMathModal
  implements OnInit, ModalInterface {
    loadingOperation: boolean;
    constructor(
    readonly dialogRef: MatDialogRef<MergeModalComponent>,
    readonly cdr: ChangeDetectorRef,
    readonly specMathService: SpecMathService
  ) {
    super(dialogRef, cdr, specMathService);
  }

  async nextStep(stepper: MatStepper) {
    const currStep = this.stepList[this.currentStep];

    if (currStep.lastBaseStep) {
      this.loadingOperation = true;
      await this.mergeOperation();
      this.loadingOperation = false;
      this.cdr.detectChanges();

      if (!this.hasMergeConflicts) {
        this.finalizeSteps();
        return;
      }
    }

    if (this.mergeConflictsResolved) {
      if (!this.hasMergeConflicts) {
        this.finalizeSteps();
        return;
      }

      this.loadingOperation = true;
      await this.sendResolvedConflicts();
      this.loadingOperation = false;
      this.finalizeSteps();
      return;
    }

    stepper.selectedIndex = ++this.currentStep;
  }

  async mergeOperation() {
    const mergeSet = await this.generateMergeSet();
    const callResponse = await this.specMathService
      .mergeSpecs(mergeSet)
      .toPromise();

    switch (callResponse.status) {
      case 'conflicts':
        this.mergeConflicts = callResponse.conflicts;
        break;
      case 'success':
        this.resultSpec = new File(
          [callResponse.result],
          `${this.specNameInputOptions.newFileName}.yaml`
        );
        break;
    }
  }

  async sendResolvedConflicts() {
    const mergeSet = await this.generateMergeSet();
    const callResponse = await this.specMathService
      .mergeSpecs(mergeSet)
      .toPromise();
    this.resultSpec = new File(
      [callResponse.result],
      `${this.specNameInputOptions.newFileName}.yaml`
    );
  }

  async generateMergeSet(): Promise<MergeRequest> {
    const requestBody: MergeRequest = {
      specs: await Promise.all(
        this.specFilesUploadOptions.specFiles.map((spec) =>
          readFileAsString(spec)
        )
      ),
    };

    if (this.defaultsFileUploadOptions?.defaultsFile) {
      requestBody.defaultsFile = await readFileAsString(
        this.defaultsFileUploadOptions.defaultsFile
      );
    }

    if (this.hasMergeConflicts) {
      requestBody.conflictResolutions = this.mergeConflicts;
    }

    return requestBody;
  }

  get conflictsCount(): string {
    const resolvedConflicts = this.mergeConflicts.filter(
      (curr) => curr.resolvedValue
    ).length;
    return `${resolvedConflicts}/${this.mergeConflicts.length}`;
  }

  get shouldDisplayConflictCounter(): boolean {
    return this.currentStep === Steps.resolveConflicts;
  }

  handleResolvedOptions(resolvedOptions: ResolvedMergeConflictOptions) {
    this.mergeConflicts[resolvedOptions.index].resolvedValue =
      resolvedOptions.value;
  }

  ngOnInit() {
    this.stepList = MERGE_STEP_LIST;
  }
}
