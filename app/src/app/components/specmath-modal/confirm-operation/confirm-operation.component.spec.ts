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

import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { queryElement } from '../../../../shared/functions';

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
    component.defaultsFileUploadOptions = { defaultsFile: new File(['content'], 'defaults.yaml') };
    component.specFilesUploadOptions = {
      specFiles: [
        new File(['content'], 'defaults.yaml'),
        new File(['content'], 'defaults.yaml')
      ],
      valid: true
    };

    fixture.detectChanges();
    expect(component.inputFilesValid).toEqual(true);

    const defaultsChip = queryElement(fixture, '.modal-step-chip-icon.description').nativeElement;
    expect(defaultsChip).toBeTruthy();

    const specFileChip = queryElement(fixture, '.modal-step-chip-icon.description').nativeElement;
    expect(specFileChip).toBeTruthy();
  });
});
