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

import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatStepper } from '@angular/material/stepper';
import { SpecNameInputOptions, DefaultsFileUploadOptions, SpecFilesUploadOptions } from 'src/shared/interfaces';

const toolTipText = {
  1: 'You must name your new spec',
  3: 'You must upload a set of spec files'
};

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit {
  minSteps: number;
  currentStep: number;
  maxSteps: number;
  specNameInputOptions?: SpecNameInputOptions;
  defaultsFileUploadOptions?: DefaultsFileUploadOptions;
  specFilesUploadOptions?: SpecFilesUploadOptions;

  constructor(readonly dialogRef: MatDialogRef<ModalComponent>) {
    dialogRef.disableClose = true;
  }

  nextStep(stepper: MatStepper): void {
    if (this.currentStep < this.maxSteps) {
      this.currentStep++;
      stepper.next();
    }
  }

  previousStep(stepper: MatStepper): void {
    if (this.currentStep > this.minSteps) {
      this.currentStep--;
      stepper.previous();
    }
  }

  get newFileName(): string {
    if (!this.specNameInputOptions) {
      return '';
    }

    return this.specNameInputOptions.newFileName;
  }

  get nextButtonTooltipText(): string {
    return toolTipText[this.currentStep];
  }

  get nextButtonDisabled(): boolean {
    if (this.currentStep === 1 && !!!this.specNameInputOptions?.valid) {
      return true;
    }

    if (this.currentStep === 3 && !!!this.specFilesUploadOptions?.valid) {
      return true;
    }

    return false;
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

  ngOnInit() {
    this.currentStep = 1;
    this.maxSteps = 4;
    this.minSteps = 1;
  }
}
