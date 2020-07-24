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
  MergeConflict
} from 'src/shared/interfaces';

interface StepMeta {
  toolTipText?: string;
  nextButtonText?: string;
  lastBaseStep?: boolean;
  lastConflict?: boolean;
}

enum Steps {
  specNameInput = 0,
  specFilesUpload = 2,
  confirmOperation = 3,
}

const stepOptions: StepMeta[] = [
  {
    toolTipText: 'You must name your new spec',
    nextButtonText: 'Next'
  },
  {
    nextButtonText: 'Next'
  },
  {
    toolTipText: 'You must upload a set of spec files',
    nextButtonText: 'Next'
  },
  {
    nextButtonText: 'Confirm',
    lastBaseStep: true,
  }
];

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent {
  currentIndex = 0;
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
  mergeConflicts: MergeConflict[];
  stepList: StepMeta[] = [...stepOptions];
  stepsNum = stepOptions.length;

  constructor(readonly dialogRef: MatDialogRef<ModalComponent>, private cdr: ChangeDetectorRef) {
    dialogRef.disableClose = true;
  }

  nextStep(stepper: MatStepper): void {
    if (this.currentStep.lastBaseStep) {
      // ?Service call
      this.mergeOperation();

      if (this.hasMergeConflicts) {
        this.stepList = [...this.stepList, ...this.generateMergeStepOptions];
      } else {
        this.finalizeSteps();
        return;
      }
    }

    if (this.currentStep.lastConflict) {
      // ?Service call to send resolved conflicts
      // ?Emit result file back to parent
      this.finalizeSteps();
      return;
    }

    this.cdr.detectChanges();
    stepper.selectedIndex = ++this.currentIndex;
  }

  previousStep(stepper: MatStepper): void {
    if (this.currentIndex === 0) {
      return;
    }
    stepper.selectedIndex = --this.currentIndex;
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

  get currentStep(): StepMeta {
    return this.stepList[this.currentIndex];
  }

  get generateMergeStepOptions(): StepMeta[] {
    return this.mergeConflicts.map((_, index) => ({
      toolTipText: 'You must resolve this conflict',
      nextButtonText: 'Resolve',
      lastConflict: index === this.mergeConflicts.length - 1,
    }));
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
    return this.stepList[this.currentIndex].toolTipText;
  }

  get nextButtonEnabled(): boolean {
    if (this.currentIndex > Steps.confirmOperation
      && this.hasMergeConflicts
      && !this.mergeConflicts[this.currentIndex - this.stepsNum]?.resolvedValue) {
      return false;
    }

    switch (this.currentIndex) {
      case Steps.specNameInput:
        return this.specNameInputOptions?.valid;
      case Steps.specFilesUpload:
        return this.specFilesUploadOptions?.valid;
      default:
        return true;
    }
  }

  get nextButtonText(): string {
    return this.stepList[this.currentIndex].nextButtonText;
  }

  get stepLabel(): string {
    const handlingMergeConflicts = this.currentIndex >= this.stepsNum;
    const currentIndex = (handlingMergeConflicts
      ? this.currentIndex - this.stepsNum
      : this.currentIndex) + 1;
    const currentMax = handlingMergeConflicts
      ? this.mergeConflicts.length
      : stepOptions.length;

    return `${currentIndex}/${currentMax}`;
  }

  get shouldShowFileName(): boolean {
    // Do not show the file name as the user is inputing it. Wait util we
    // have moved onto the next step;
    return this.currentIndex !== Steps.specNameInput;
  }

  get shouldShowBackButton(): boolean {
    return this.currentIndex > 0;
  }

  get stepHeaderText(): string {
    return (`Merge specs${this.currentIndex > Steps.confirmOperation ? ': Resolving conflicts' : ''}`);
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

  handleResolvedOptions(resolvedValue: string) {
    this.mergeConflicts[this.currentIndex - this.stepsNum].resolvedValue = resolvedValue;
  }
}
