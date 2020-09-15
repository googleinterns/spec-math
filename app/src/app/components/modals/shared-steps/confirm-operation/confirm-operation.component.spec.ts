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
import { ConfirmOperationComponent } from './confirm-operation.component';

import { BrowserModule, By } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { queryElement } from 'src/shared/functions';

describe('ConfirmOperationComponent', () => {
  let fixture: ComponentFixture<ConfirmOperationComponent>;
  let component: ConfirmOperationComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ConfirmOperationComponent
      ],
      imports: [
        MatIconModule,
        BrowserModule,
        MatChipsModule,
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(ConfirmOperationComponent);
      component = fixture.componentInstance;
    });
  }));

  it('creates the ConfirmOperationComponent component', () => {
    expect(component).toBeTruthy();
  });

  it('displays a defaults and spec file chips when a set of files is uploaded', () => {
    component.fileUploadOptions = { file: new File(['content'], 'defaults.yaml'), type: 'defaults' };
    component.specFilesUploadOptions = {
      specFiles: [
        new File(['content'], 'spec1.yaml'),
        new File(['content'], 'spec2.yaml')
      ],
      valid: true
    };

    fixture.detectChanges();

    const defaultsChip = queryElement(fixture, '.modal-step-chip-icon.defaults').nativeElement;
    expect(defaultsChip).toBeTruthy();

    const specFileChip = queryElement(fixture, '.modal-step-chip-icon.spec').nativeElement;
    expect(specFileChip).toBeTruthy();
  });

  it('does not display any chips if the set of files is invalid', () => {
    component.fileUploadOptions = { file: null, type: null };
    component.specFilesUploadOptions = {
      specFiles: [],
      valid: false
    };

    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.modal-step-chip-icon.defaults'))).toBeNull();
    expect(fixture.debugElement.query(By.css('.modal-step-chip-icon.spec'))).toBeNull();
  });

  it('displays only spec file chips when a default file is missing', () => {
    component.fileUploadOptions = { file: null, type: null };
    component.specFilesUploadOptions = {
      specFiles: [
        new File(['content'], 'spec1.yaml'),
        new File(['content'], 'spec2.yaml')
      ],
      valid: true
    };

    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.modal-step-chip-icon.defaults'))).toBeNull();

    const specFileChip = queryElement(fixture, '.modal-step-chip-icon.spec').nativeElement;
    expect(specFileChip).toBeTruthy();
  });
});
