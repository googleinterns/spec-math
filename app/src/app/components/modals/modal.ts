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
  FileUploadOptions,
  SpecFilesUploadOptions,
  MergeConflict,
  OperationSet,
  OperationType
} from 'src/shared/interfaces';
import { SpecMathService } from 'src/shared/services/specmath.service';

export type StepOptions = {
  [key in number]: {
    toolTipText?: string;
    nextStep?: number;
    previousStep?: number;
    nextButtonText?: string;
    lastStep?: boolean;
    stepLabel: string;
  };
};

export class SpecMathModal {
  stepList: StepOptions;
  currentStep: number;
  specNameInputOptions: SpecNameInputOptions = {
    newFileName: '',
    valid: false
  };
  fileUploadOptions: FileUploadOptions = {
    file: null,
    type: null
  };
  specFilesUploadOptions: SpecFilesUploadOptions = {
    specFiles: [],
    valid: false,
  };
  resultSpec: File;
  mergeConflicts: MergeConflict[];
  operationType: OperationType;

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
      defaultsFile: this.fileUploadOptions?.file,
      resultSpec: this.resultSpec,
      valid: true,
      type: this.operationType
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

  handleFileUploadOptions(fileUploadOptions: FileUploadOptions) {
    this.fileUploadOptions = fileUploadOptions;
  }

  handleSpecFilesUploadOptions(specFilesUploadOptions: SpecFilesUploadOptions) {
    this.specFilesUploadOptions = specFilesUploadOptions;
  }
}
