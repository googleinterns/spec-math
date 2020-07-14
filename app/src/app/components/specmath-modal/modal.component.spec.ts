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
import { ModalComponent } from './modal.component';

import { MatDialogModule, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { By, BrowserModule } from '@angular/platform-browser';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const queryElement = (targetFixture: ComponentFixture<ModalComponent>, targetClass: string) => {
  return targetFixture.debugElement.query(By.css(targetClass));
};

describe('ModalComponent', () => {
  let dialog: MatDialog;
  let fixture: ComponentFixture<ModalComponent>;
  let modal: ModalComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ModalComponent
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
        MatDialogModule,
        BrowserAnimationsModule
      ],
      providers: [
        {
          provide: MatDialogRef, useValue: { close: () => {} }
        },
      ]
    }).compileComponents().then(() => {
      dialog = TestBed.inject(MatDialog);
      fixture = TestBed.createComponent(ModalComponent);
      modal = fixture.componentInstance;
    });
  }));

  it('creates the modal component', () => {
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

  it('next button is disabled on step 1 when the new file name input is empty', () => {
    fixture.detectChanges();
    const nextButton = queryElement(fixture, '#modal-step-1-next').nativeElement;

    expect(nextButton.disabled).toBeTruthy();
  });

  it('new file name form validity', () => {
    fixture.detectChanges();
    const specNameField = modal.specNameFormControl;
    expect(specNameField.valid).toBeFalsy();

    specNameField.setValue('invalid input!');
    expect(specNameField.hasError('pattern')).toBeTruthy();

    specNameField.setValue('');
    expect(specNameField.hasError('required')).toBeTruthy();

    specNameField.setValue('test_spec');
    expect(specNameField.valid).toBeTruthy();
  });

  it('calls nextStep() when the new file name is valid and the next button is clicked', () => {
    fixture.detectChanges();
    const specNameField = modal.specNameFormControl;
    const nextButton = queryElement(fixture, '#modal-step-1-next').nativeElement;
    const spy = spyOn(modal, 'nextStep');

    specNameField.setValue('test_spec');
    fixture.detectChanges();
    nextButton.click();
    expect(spy).toHaveBeenCalled();
  });

  it('calls submitName() when the new file name is valid and the next button is clicked', () => {
    fixture.detectChanges();
    const specNameField = modal.specNameFormControl;
    const nextButton = queryElement(fixture, '#modal-step-1-next').nativeElement;
    const spy = spyOn(modal, 'submitName');

    specNameField.setValue('test_spec');
    fixture.detectChanges();
    nextButton.click();
    expect(spy).toHaveBeenCalled();
  });

  it('the defaults file chip is shown when a file is uploaded', () => {
    fixture.detectChanges();
    const specNameField = modal.specNameFormControl;
    const nextButton = queryElement(fixture, '#modal-step-1-next').nativeElement;

    specNameField.setValue('test_spec');
    fixture.detectChanges();
    nextButton.click();

    const nextButtonStep2 = queryElement(fixture, '#modal-step-2-next').nativeElement;
    // modal.defaultsFile = 
  });
});
