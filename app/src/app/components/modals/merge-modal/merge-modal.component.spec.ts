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
import { MergeModalComponent } from './merge-modal.component';

import { BrowserModule, By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Component, Output, EventEmitter } from '@angular/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { StubSpecMathService } from 'src/tests/mocks/stub-specmath.service';
import { SpecNameInputOptions } from 'src/shared/interfaces';
import { queryElement } from 'src/shared/functions';
import { SpecMathService } from 'src/shared/services/specmath.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ConfirmOperationModule } from '../shared-steps/confirm-operation/confirm-operation.module';
import { ConfirmOperationComponent } from '../shared-steps/confirm-operation/confirm-operation.component';

describe('MergeModalComponent', () => {
  let fixture: ComponentFixture<MergeModalComponent>;
  let modal: MergeModalComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        MergeModalComponent,
        SpecNameInputStubComponent,
      ],
      imports: [
        BrowserAnimationsModule,
        BrowserModule,
        MatButtonModule,
        MatDialogModule,
        MatIconModule,
        MatStepperModule,
        MatTooltipModule,
        HttpClientTestingModule,
        ConfirmOperationModule,
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
      fixture = TestBed.createComponent(MergeModalComponent);
      modal = fixture.componentInstance;
    });
  }));

  const getSpecNameInputStubComponent = () =>
    fixture.debugElement.query(By.directive(SpecNameInputStubComponent))
      .injector.get(SpecNameInputStubComponent);

  it('creates the merge modal component', () => {
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

  it('disables the next button when the modal is first opened', () => {
    fixture.detectChanges();
    const nextButton = queryElement(fixture, '#modal-button-next').nativeElement;

    expect(nextButton.disabled).toBeTruthy();
  });

  it('enables the next button when newFileName is valid', () => {
    fixture.detectChanges();
    const nextButton = queryElement(fixture, '#modal-button-next').nativeElement;

    getSpecNameInputStubComponent().emitOptions();
    fixture.detectChanges();
    expect(nextButton.disabled).toBeFalsy();
  });

  it('moves onto the next step when next button is clicked', () => {
    fixture.detectChanges();
    const nextButton = queryElement(fixture, '#modal-button-next').nativeElement;

    getSpecNameInputStubComponent().emitOptions();
    fixture.detectChanges();
    nextButton.click();
    fixture.detectChanges();
    expect(modal.currentStep).toEqual(1);
  });

  it('renders the ConfirmOperationComponent when a set of files is valid', () => {
    modal.specFilesUploadOptions = {
      specFiles: [
        new File(['content'], 'spec1.yaml'),
        new File(['content'], 'spec2.yaml')
      ],
      valid: true
    };

    fixture.detectChanges();
    expect(modal.validFiles).toEqual(true);

    const confirmOperationComponent =
      fixture.debugElement.query(By.directive(ConfirmOperationComponent)).nativeElement;
    expect(confirmOperationComponent).toBeTruthy();
  });

  it('populates the merge conflicts array when conflicts are received from the backend', async () => {
    modal.specFilesUploadOptions = {
      specFiles: [
        new File(['content'], 'spec1.yaml'),
        new File(['content'], 'spec2.yaml')
      ],
      valid: true
    };

    await modal.mergeOperation();
    expect(modal.hasMergeConflicts).toEqual(true);
  });

  it('calls the dialog close method when the final step is reached', () => {
    const spy = spyOn(modal.dialogRef, 'close');

    modal.specFilesUploadOptions = {
      specFiles: [
        new File(['content'], 'spec1.yaml'),
        new File(['content'], 'spec2.yaml')
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

/**
 * Stub component for the SpecNameInputComponent
 */
@Component({
  selector: 'app-spec-name-input',
  template: '<div>spec-name-input stub</div>'
})
export class SpecNameInputStubComponent {
  @Output() options = new EventEmitter<SpecNameInputOptions>();

  emitOptions() {
    this.options.emit({ newFileName: 'new_spec', valid: true });
  }
}
