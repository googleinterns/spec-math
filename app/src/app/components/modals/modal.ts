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

import { ChangeDetectorRef } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatStepper } from '@angular/material/stepper';
import {
  SpecNameInputOptions,
  DefaultsFileUploadOptions,
  SpecFilesUploadOptions,
  MergeConflict,
  OperationSet,
} from 'src/shared/interfaces';
import { SpecMathService } from 'src/shared/services/specmath.service';

export enum Steps {
  specNameInput = 0,
  defaultsFileUpload = 1,
  specFilesUpload = 2,
  confirmOperation = 3,
  resolveConflicts = 4,
}

export type StepOptions = {
  [key in number]: {
    toolTipText?: string;
    nextStep?: number;
    previousStep?: number;
    nextButtonText?: string;
    lastBaseStep?: boolean;
    stepLabel: string;
  };
};

export class SpecMathModal {
  stepList: StepOptions;
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

  constructor(readonly dialogRef: MatDialogRef<SpecMathModal>,
              readonly cdr: ChangeDetectorRef,
              readonly specMathService: SpecMathService) {
    dialogRef.disableClose = true;
  }

  previousStep(stepper: MatStepper) {
    stepper.selectedIndex = --this.currentStep;
  }

  finalizeSteps() {
    const finalOperationSet: OperationSet = {
      specFiles: this.specFilesUploadOptions.specFiles,
      defaultsFile: this.defaultsFileUploadOptions?.defaultsFile,
      resultSpec: this.resultSpec,
      valid: true,
    };
    this.dialogRef.close(finalOperationSet);
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
    return this.stepList[this.currentStep].stepLabel;
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
}
