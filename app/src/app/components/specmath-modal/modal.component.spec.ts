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

import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { BrowserModule } from '@angular/platform-browser';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Step1Component } from './specmath-modal-step-1/step1.component';
import { queryElement } from '../../../shared/functions';

describe('ModalComponent', () => {
  let fixture: ComponentFixture<ModalComponent>;
  let modal: ModalComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ModalComponent, Step1Component
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

  it('next button is disabled when the modal is first opened', () => {
    fixture.detectChanges();
    const nextButton = queryElement(fixture, '#modal-button-next').nativeElement;

    expect(nextButton.disabled).toBeTruthy();
  });

  it('next button is enabled when newFileName is valid', () => {
    fixture.detectChanges();
    const nextButton = queryElement(fixture, '#modal-button-next').nativeElement;

    modal.step1Options = { newFileName: 'new_spec', valid: true };
    fixture.detectChanges();
    expect(nextButton.disabled).toBeFalsy();
  });

  it('moves onto the next step when next button is clicked', () => {
    fixture.detectChanges();
    const nextButton = queryElement(fixture, '#modal-button-next').nativeElement;

    modal.step1Options = { newFileName: 'new_spec', valid: true };
    fixture.detectChanges();
    nextButton.click();
    fixture.detectChanges();
    expect(modal.currentStep).toEqual(2);
  });
});
