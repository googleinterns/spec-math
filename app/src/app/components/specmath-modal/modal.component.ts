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

import { Component, ChangeDetectorRef } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatStepper } from '@angular/material/stepper';
import {
  SpecNameInputOptions,
  DefaultsFileUploadOptions,
  SpecFilesUploadOptions,
  MergeConflict,
  OperationSet,
  ResolvedMergeConflictOptions
} from 'src/shared/interfaces';
import { MockSpecMathService } from 'src/tests/mocks/mock-specmath.service';

enum Steps {
  specNameInput = 0,
  defaultsFileUpload = 1,
  specFilesUpload = 2,
  confirmOperation = 3,
  resolveConflicts = 4,
}

type StepOptions = {
  [key in Steps]: {
    toolTipText?: string,
    nextStep?: Steps,
    previousStep?: Steps,
    nextButtonText?: string,
    lastBaseStep?: boolean,
    stepLabel: string,
  };
};

const stepList: StepOptions = {
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
    stepLabel: 'Spec files'
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
  }
};

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent {
  currentStep = Steps.specNameInput;
  specNameInputOptions: SpecNameInputOptions = {
    newFileName: '',
    valid: false
  };
  defaultsFileUploadOptions: DefaultsFileUploadOptions = {
    defaultsFile: null
  };
  specFilesUploadOptions: SpecFilesUploadOptions = {
    specFiles: [],
    valid: false,
  };
  resultSpec: File;
  mergeConflicts: MergeConflict[];
  stepList: StepOptions = stepList;

  constructor(readonly dialogRef: MatDialogRef<ModalComponent>,
              private cdr: ChangeDetectorRef,
              private mockService: MockSpecMathService) {
    dialogRef.disableClose = true;
  }

  async nextStep(stepper: MatStepper) {
    const currStep = stepList[this.currentStep];

    if (currStep.lastBaseStep) {
      // ?Service call
      await this.mergeOperation();
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

      await this.sendResolvedConflicts();
      this.finalizeSteps();
      return;
    }

    stepper.selectedIndex = ++this.currentStep;
  }

  previousStep(stepper: MatStepper) {
    stepper.selectedIndex = --this.currentStep;
  }

  finalizeSteps() {
    const finalOperationSet: OperationSet = {
      specFiles: this.specFilesUploadOptions.specFiles,
      defaultsFile: this.defaultsFileUploadOptions?.defaultsFile,
      resultSpec: {
        name: this.specNameInputOptions.newFileName,
        file: this.resultSpec
      },
      valid: true,
    };
    this.dialogRef.close(finalOperationSet);
  }

  async mergeOperation() {
    // ?Replace the mock service with real once its deployed
    const callResponse = await this.mockService.mergeSpecsConflicts().toPromise();

    switch (callResponse?.status) {
      case 'conflicts':
        this.mergeConflicts = callResponse.conflicts;
        break;
      case 'success':
        this.resultSpec = new File([callResponse.result], this.specNameInputOptions.newFileName);
    }
  }

  async sendResolvedConflicts() {
    // ?Replace the mock service with real once its deployed
    const callResponse = await this.mockService.mergeSpecs().toPromise();
    this.resultSpec = new File([callResponse.result], this.specNameInputOptions.newFileName);
  }

  get hasMergeConflicts() {
    return !!this.mergeConflicts;
  }

  get validFiles(): boolean {
    return this.specFilesUploadOptions.valid;
  }

  get newFileName(): string {
    return this.specNameInputOptions?.newFileName || '';
  }

  get nextButtonTooltipText(): string {
    return this.stepList[this.currentStep].toolTipText;
  }

  get nextButtonEnabled(): boolean {
    switch (this.currentStep) {
      case Steps.specNameInput:
        return this.specNameInputOptions?.valid;
      case Steps.specFilesUpload:
        return this.specFilesUploadOptions?.valid;
      case Steps.resolveConflicts:
        return this.mergeConflictsResolved;
      default:
        return true;
    }
  }

  get nextButtonText(): string {
    return this.stepList[this.currentStep].nextButtonText;
  }

  get stepLabel(): string {
    return (stepList[this.currentStep].stepLabel);
  }

  get conflictsCount(): string {
    const resolvedConflicts = this.mergeConflicts.reduce((acc, curr) => curr?.resolvedValue ? ++acc : acc, 0);
    return (`${resolvedConflicts}/${this.mergeConflicts.length}`);
  }

  get shouldShowBackButton(): boolean {
    return this.currentStep > 0;
  }

  get mergeConflictsResolved(): boolean {
    return !!this.mergeConflicts && this.mergeConflicts.every((conflict) => conflict.resolvedValue);
  }

  handleSpecNameInputOptions(specNameInputOptions: SpecNameInputOptions) {
    this.specNameInputOptions = specNameInputOptions;
  }

  handleDefaultsFileUploadOptions(defaultsFileUploadOptions: DefaultsFileUploadOptions) {
    this.defaultsFileUploadOptions = defaultsFileUploadOptions;
  }

  handleSpecFilesUploadOptions(specFilesUploadOptions: SpecFilesUploadOptions) {
    this.specFilesUploadOptions = specFilesUploadOptions;
  }

  handleResolvedOptions(resolvedOptions: ResolvedMergeConflictOptions) {
    this.mergeConflicts[resolvedOptions.index].resolvedValue = resolvedOptions.value;
  }
}
