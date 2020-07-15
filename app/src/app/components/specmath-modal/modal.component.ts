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
import { Step1Options, Step2Options } from 'src/shared/interfaces';

type FileUpload = 'default' | 'spec';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit {
  minSteps: number;
  currentStep: number;
  maxSteps: number;
  step1Options?: Step1Options;
  step2Options?: Step2Options;

  constructor(readonly dialogRef: MatDialogRef<ModalComponent>) {
    dialogRef.disableClose = true;
  }

  get newFileName(): string {
    if (!this.step1Options) {
      return '';
    }

    return this.step1Options.newFileName;
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

  specNameValid(): boolean {
    if (!this.step1Options) {
      return false;
    }

    return this.step1Options.valid;
  }

  handleStep1Options(step1Options: Step1Options) {
    this.step1Options = step1Options;
  }

  handleStep2Options(step2Options: Step2Options) {
    this.step2Options = step2Options;
  }

  ngOnInit() {
    this.currentStep = 1;
    this.maxSteps = 4;
    this.minSteps = 1;
  }
}
