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

import { MatStepperModule } from '@angular/material/stepper';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { By } from '@angular/platform-browser';

describe('ModalComponent', () => {
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
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(ModalComponent);
      modal = fixture.componentInstance;
    });
  }));

  it('creates the modal component', () => {
    expect(modal).toBeTruthy();
  });

  it('hides the modal when the cancel button is pressed', () => {
    const cancelButton = fixture.debugElement.query(By.css('#modal-cancel-button-step-1')).nativeElement;
    cancelButton.click();
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.modal-container'))).toBeFalsy();
  });
});
