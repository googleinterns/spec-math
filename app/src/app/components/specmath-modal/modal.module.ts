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

import { BrowserModule } from '@angular/platform-browser';
import { DefaultsFileUploadComponent } from './defaults-file-upload/defaults-file-upload.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { MatStepperModule } from '@angular/material/stepper';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ModalComponent } from './modal.component';
import { NgModule } from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';
import { SpecFilesUploadComponent } from './spec-files-upload/spec-files-upload.component';
import { SpecNameInputComponent } from './spec-name-input/spec-name-input.component';
import { ConfirmOperationComponent } from './confirm-operation/confirm-operation.component';
import { ResolveConflictComponent } from './resolve-conflict/resolve-conflict.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { StubSpecMathService } from '../../../tests/mocks/stub-specmath.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SpecMathService } from '../../../shared/services/specmath.service';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    ModalComponent,
    SpecNameInputComponent,
    DefaultsFileUploadComponent,
    SpecFilesUploadComponent,
    ConfirmOperationComponent,
    ResolveConflictComponent
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
    MatRadioModule,
    MatExpansionModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    RouterModule
  ],
  exports: [
    ModalComponent,
    SpecNameInputComponent,
    DefaultsFileUploadComponent,
    SpecFilesUploadComponent,
  ],
  providers: [{ provide: SpecMathService, useClass: StubSpecMathService }],
  bootstrap: [ModalComponent]
})
export class ModalModule { }
