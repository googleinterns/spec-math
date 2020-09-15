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

import { TestBed, async, ComponentFixture } from '@angular/core/testing';
import { OverlayModalComponent } from './overlay-modal.component';

import { BrowserModule } from '@angular/platform-browser';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { StubSpecMathService } from 'src/tests/mocks/stub-specmath.service';
import { SpecMathService } from 'src/shared/services/specmath.service';
import { ConfirmOperationModule } from '../shared-steps/confirm-operation/confirm-operation.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { SpecNameInputModule } from '../shared-steps/spec-name-input/spec-name-input.module';
import { FileUploadModule } from '../shared-steps/file-upload/file-upload.module';
import { queryElement } from 'src/shared/functions';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('OverlayModalComponent', () => {
  let fixture: ComponentFixture<OverlayModalComponent>;
  let modal: OverlayModalComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        OverlayModalComponent
      ],
      imports: [
        MatStepperModule,
        MatButtonModule,
        MatDialogModule,
        MatTooltipModule,
        BrowserModule,
        MatProgressSpinnerModule,
        HttpClientModule,
        RouterModule,
        ConfirmOperationModule,
        SpecNameInputModule,
        FileUploadModule,
        MatIconModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: [
        {
          provide: MatDialogRef, useValue: { close: () => { } }
        },
        {
          provide: SpecMathService, useClass: StubSpecMathService
        }
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(OverlayModalComponent);
      modal = fixture.componentInstance;
    });
  }));

  it('creates the overlay modal component', () => {
    expect(modal).toBeTruthy();
  });

  it('is opened', () => {
    expect(queryElement(fixture, '.modal-container')).toBeTruthy();
  });

  it('is closed when then cancel button is pressed', () => {
    const spy = spyOn(modal.dialogRef, 'close').and.callThrough();
    const cancelButton = queryElement(fixture, '#modal-cancel-button').nativeElement;

    cancelButton.click();
    expect(spy).toHaveBeenCalled();
  });

  it('calls the dialog close method when the final step is reached', () => {
    const spy = spyOn(modal.dialogRef, 'close');

    modal.fileUploadOptions = {
      file: new File(['content'], 'defaults.yaml'),
      type: 'defaults',
    };
    modal.specFilesUploadOptions = {
      specFiles: [
        new File(['content'], 'spec1.yaml'),
      ],
      valid: true
    };
    modal.specNameInputOptions = {
      newFileName: 'test',
      valid: true,
    };

    fixture.detectChanges();
    modal.finalizeSteps();
    expect(spy).toHaveBeenCalled();
  });
});
