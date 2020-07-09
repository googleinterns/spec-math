// Copyright 2020 Google LLC

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this spec except in compliance with the License.
// You may obtain a copy of the License at

//     https://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit {
  currentStep: number;
  newSpecName: string;
  maxSteps: number;
  minSteps: number;

  constructor(readonly dialogRef: MatDialogRef<ModalComponent>) {
    dialogRef.disableClose = true;
  }

  specNameFormControl = new FormControl('', [
    Validators.required,
    Validators.pattern('[a-zA-Z0-9_-]*')
  ]);

  nextStep(): void {
    if (this.currentStep < this.maxSteps) {
      this.currentStep++;
    }
  }

  previousStep(): void {
    if (this.currentStep > this.minSteps) {
      this.currentStep--;
    }
  }

  ngOnInit() {
    this.newSpecName = '';
    this.currentStep = 1;
    this.maxSteps = 4;
    this.minSteps = 0;
  }
}
