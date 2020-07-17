// Copyright 2020 Google LLC

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this spec except in compliance with the License.
// You may obtain a copy of the License at

//     https://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import { TestBed, async, ComponentFixture } from '@angular/core/testing';
import { SpecFilesUploadComponent } from './spec-files-upload.component';

import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';
import { queryElement } from '../../../../shared/functions';

describe('SpecFilesUploadComponent', () => {
  let fixture: ComponentFixture<SpecFilesUploadComponent>;
  let component: SpecFilesUploadComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        SpecFilesUploadComponent
      ],
      imports: [
        MatIconModule,
        BrowserModule,
        MatChipsModule,
        MatButtonModule
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(SpecFilesUploadComponent);
      component = fixture.componentInstance;
    });
  }));

  it('creates the SpecFilesUploadComponent component', () => {
    expect(component).toBeTruthy();
  });

  it('displays the error message when an incorrect number of files is uploaded', () => {
    fixture.detectChanges();
    component.fileUploadError = true;

    fixture.detectChanges();
    const errorMessage = queryElement(fixture, '#spec-files-upload-error').nativeElement;
    expect(errorMessage).toBeTruthy();
  });

  it('displays the spec file chips when the correct amount of spec files are uploaded', () => {
    fixture.detectChanges();
    component.specFilesUploadOptions.specFiles = [
      new File(['content'], 'spec1.yaml'),
      new File(['content'], 'spec2.yaml')
    ];

    fixture.detectChanges();
    const specFilesChipListContaine = queryElement(fixture, '.spec-files-chip-list-container').nativeElement;
    expect(specFilesChipListContaine).toBeTruthy();
  });
});
