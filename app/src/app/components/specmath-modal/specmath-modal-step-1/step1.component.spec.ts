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
import { Step1Component } from './step1.component';

import { MatDialogModule } from '@angular/material/dialog';
import { By, BrowserModule } from '@angular/platform-browser';
import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const queryElement = (targetFixture: ComponentFixture<Step1Component>, targetClass: string) => {
  return targetFixture.debugElement.query(By.css(targetClass));
};

describe('Step1Component', () => {
  let fixture: ComponentFixture<Step1Component>;
  let component: Step1Component;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        Step1Component
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
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(Step1Component);
      component = fixture.componentInstance;
    });
  }));

  it('creates the Step1 component', () => {
    expect(component).toBeTruthy();
  });

  it('file name form validity', () => {
    fixture.detectChanges();
    const specNameField = component.specNameFormControl;
    expect(specNameField.valid).toBeFalsy();

    specNameField.setValue('invalid input!');
    expect(specNameField.hasError('pattern')).toBeTruthy();

    specNameField.setValue('');
    expect(specNameField.hasError('required')).toBeTruthy();

    specNameField.setValue('new_spec');
    expect(specNameField.valid).toBeTruthy();
  });
});
