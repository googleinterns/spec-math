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
  ResolvedMergeConflictOptions
} from 'src/shared/interfaces';

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
  };
};

const stepList: StepOptions = {
  [Steps.specNameInput]: {
    toolTipText: 'You must name your new spec',
    nextStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next'
  },
  [Steps.defaultsFileUpload]: {
    nextStep: Steps.specFilesUpload,
    previousStep: Steps.specNameInput,
    nextButtonText: 'Next'
  },
  [Steps.specFilesUpload]: {
    toolTipText: 'You must upload a set of spec files',
    nextStep: Steps.confirmOperation,
    previousStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next'
  },
  [Steps.confirmOperation]: {
    previousStep: Steps.specFilesUpload,
    nextButtonText: 'Confirm',
    lastBaseStep: true,
  },
  [Steps.resolveConflicts]: {
    previousStep: Steps.confirmOperation,
    toolTipText: 'You must resolve all merge conflicts',
    nextButtonText: 'Resolve',
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
  mergeConflictsResolved = false;
  mergeConflicts: MergeConflict[];
  stepList: StepOptions = stepList;

  constructor(readonly dialogRef: MatDialogRef<ModalComponent>, private cdr: ChangeDetectorRef) {
    dialogRef.disableClose = true;
  }

  nextStep(stepper: MatStepper): void {
    const currStep = stepList[this.currentStep];

    if (this.mergeConflictsResolved) {
      this.finalizeSteps();
      return;
    }

    if (currStep.lastBaseStep) {
      // ?Service call
      this.mergeOperation();
      this.cdr.detectChanges();

      if (!this.hasMergeConflicts) {
        this.finalizeSteps();
        return;
      }
    }

    stepper.selectedIndex = ++this.currentStep;
  }

  previousStep(stepper: MatStepper): void {
    stepper.selectedIndex = --this.currentStep;
  }

  finalizeSteps() {
    this.dialogRef.close();
  }

  mergeOperation() {
    // ?Call the SpecMath service here
    this.mergeConflicts = [
      {
        keypath: '/dogs',
        option1: 'Option A',
        option2: 'Option B',
      },
      {
        keypath: '/cats',
        option1: 'Option A',
        option2: 'Option B',
      },
      {
        keypath: '/pets/categories',
        option1: 'Option A',
        option2: 'Option B',
      }
    ];
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
    return (this.currentStep < Steps.resolveConflicts
      ? `${this.currentStep + 1}/${Steps.confirmOperation + 1}`
      : 'Resolving conflicts');
  }

  get shouldShowFileName(): boolean {
    // Do not show the file name as the user is inputing it. Wait util we
    // have moved onto the next step;
    return this.currentStep !== Steps.specNameInput;
  }

  get shouldShowBackButton(): boolean {
    return this.currentStep > 0;
  }

  get stepHeaderText(): string {
    return (`Merge specs`);
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

  handleFinalizeConflicts(resolved: boolean) {
    this.mergeConflictsResolved = resolved;
  }
}
