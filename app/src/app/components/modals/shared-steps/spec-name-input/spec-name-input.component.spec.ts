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
import { SpecNameInputComponent } from './spec-name-input.component';

import { MatDialogModule } from '@angular/material/dialog';
import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('SpecNameInputComponent', () => {
  let fixture: ComponentFixture<SpecNameInputComponent>;
  let component: SpecNameInputComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        SpecNameInputComponent
      ],
      imports: [
        MatIconModule,
        MatInputModule,
        FormsModule,
        ReactiveFormsModule,
        BrowserModule,
        MatDialogModule,
        BrowserAnimationsModule
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(SpecNameInputComponent);
      component = fixture.componentInstance;
    });
  }));

  it('creates the SpecNameInputComponent', () => {
    expect(component).toBeTruthy();
  });

  it('evaluates spec name input form', () => {
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
