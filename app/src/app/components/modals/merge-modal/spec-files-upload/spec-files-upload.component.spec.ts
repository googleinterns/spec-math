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

  it('emits a valid SpecFilesUploadOptions object when the correct amount of files is uploaded', () => {
    const spy = spyOn(component.options, 'emit');

    component.validateFiles([
      new File(['content'], 'spec1.yaml'),
      new File(['content'], 'spec2.yaml'),
    ]);
    fixture.detectChanges();

    expect(component.specFilesUploadOptions.valid).toEqual(true);
    expect(spy).toHaveBeenCalledWith({
      specFiles: [
        new File(['content'], 'spec1.yaml'),
        new File(['content'], 'spec2.yaml'),
      ],
      valid: true
    });
  });

  it('emits an invalid SpecFilesUploadOptions when an incomplete set of files is uploaded', () => {
    const spy = spyOn(component.options, 'emit');

    component.validateFiles([
      new File(['content'], 'spec1.yaml'),
    ]);
    fixture.detectChanges();

    expect(component.specFilesUploadOptions.valid).toEqual(false);
    expect(spy).toHaveBeenCalledWith({
      specFiles: [
        new File(['content'], 'spec1.yaml'),
      ],
      valid: false
    });
  });

  it('emits an SpecFilesUploadOptions object every time a spec file is uploaded', () => {
    const spy = spyOn(component.options, 'emit');

    component.validateFiles([
      new File(['content'], 'spec1.yaml'),
    ]);
    fixture.detectChanges();

    expect(component.specFilesUploadOptions.valid).toEqual(false);
    expect(spy).toHaveBeenCalled();

    component.validateFiles([
      new File(['content'], 'spec2.yaml'),
    ]);
    fixture.detectChanges();

    expect(component.specFilesUploadOptions.valid).toEqual(true);
    expect(spy).toHaveBeenCalled();
  });
});
