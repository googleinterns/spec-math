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
import { DefaultsFileUploadComponent } from './defaults-file-upload.component';
import { RouterTestingModule } from '@angular/router/testing';

import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatButtonModule } from '@angular/material/button';
import { queryElement } from '../../../../shared/functions';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';

describe('DefaultsFileUploadComponent', () => {
  let fixture: ComponentFixture<DefaultsFileUploadComponent>;
  let component: DefaultsFileUploadComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DefaultsFileUploadComponent],
      imports: [
        MatIconModule,
        BrowserModule,
        MatChipsModule,
        MatButtonModule,
        RouterTestingModule.withRoutes([]),
        MatDialogModule
      ],
      providers: [
        {
          provide: MatDialogRef,
          useValue: { close: () => {} },
        },
      ],
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(DefaultsFileUploadComponent);
        component = fixture.componentInstance;
      });
  }));

  it('creates the Step2 component', () => {
    expect(component).toBeTruthy();
  });

  it('shows the defaults file chip when a file is uploaded', () => {
    fixture.detectChanges();
    component.defaultsFileUploadOptions = {
      defaultsFile: new File(['content'], 'defaults.yaml'),
    };
    fixture.detectChanges();
    const defaultsFileChip = queryElement(fixture, '#defaults-file-chip')
      .nativeElement;
    expect(defaultsFileChip).toBeTruthy();
  });

  it('closes the modal and navigates to /defaults when the Lean more link is clicked', () => {
    const dialogSpy = spyOn(component.dialog, 'closeAll');
    const routerSpy = spyOn(component.router, 'navigateByUrl');
    const learnMoreLink = queryElement(fixture, '#learn-more-defaults')
      .nativeElement;
    learnMoreLink.click();

    fixture.detectChanges();
    expect(dialogSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith('defaults');
  });
});
