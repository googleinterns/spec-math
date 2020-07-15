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

import { NgModule } from '@angular/core';
import { ModalComponent } from './modal.component';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { MatChipsModule } from '@angular/material/chips';
import { Step1Component } from './specmath-modal-step-1/step1.component';
import { Step2Component } from './specmath-modal-step-2/step2.component';
import { Step3Component } from './specmath-modal-step-3/step3.component';

@NgModule({
  declarations: [
    ModalComponent,
    Step1Component,
    Step2Component,
    Step3Component
  ],
  imports: [
    MatStepperModule,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatTooltipModule,
    BrowserModule,
    MatChipsModule,
  ],
  exports: [
    ModalComponent,
    Step1Component,
    Step2Component,
    Step3Component
  ],
  providers: [],
  bootstrap: [ModalComponent]
})
export class ModalModule { }
