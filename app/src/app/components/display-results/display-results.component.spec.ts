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

import { TestBed, ComponentFixture } from '@angular/core/testing';
import { DisplayResultsComponent } from './display-results.component';

import { MatTabsModule } from '@angular/material/tabs';
import { BrowserModule } from '@angular/platform-browser';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { queryElement } from 'src/shared/functions';
import { OperationService } from 'src/shared/services/operation.service';
import { YamlRenderModule } from '../yaml-render/yaml-render.module';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('DisplayResultsComponent', () => {
  let fixture: ComponentFixture<DisplayResultsComponent>;
  let component: DisplayResultsComponent;
  let operationService: OperationService;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      declarations: [
        DisplayResultsComponent,
      ],
      imports: [
        MatTabsModule,
        BrowserModule,
        MatButtonModule,
        MatIconModule,
        MatMenuModule,
        MatDividerModule,
        YamlRenderModule,
        RouterTestingModule,
        BrowserAnimationsModule
      ],
      providers: [OperationService],
    }).compileComponents().then(() => {
      operationService = TestBed.inject(OperationService);
      operationService.setResults({
        specFiles: [
          new File(['openapi: 3.0.0'], 'spec1.yaml'),
          new File(['openapi: 3.0.0'], 'spec2.yaml')
        ],
        resultSpec: new File(['openapi: 3.0.0'], 'results.yaml'),
        valid: true
      });

      fixture = TestBed.createComponent(DisplayResultsComponent);
      component = fixture.componentInstance;
    });
  });

  it('creates the DisplayResultsComponent', () => {
    expect(component).toBeTruthy();
  });

  it('downloads the OperationSet zip file when the "Download operation set" button is clicked', () => {
    fixture.detectChanges();
    const spy = spyOn(component, 'downloadOperationSet');
    const downloadButton = queryElement(fixture, '#results-zip-download').nativeElement;

    downloadButton.click();
    expect(spy).toHaveBeenCalled();
  });

  it('downloads each separate file when each file type download button is clicked', () => {
    fixture.detectChanges();
    const spy = spyOn(component, 'downloadFile');
    const resultsMenu = queryElement(fixture, '#results-options-menu').nativeElement;
    resultsMenu.click();

    fixture.detectChanges();
    const resultsDownload = queryElement(fixture, '#results-options-download').nativeElement;
    resultsDownload.click();
    expect(spy).toHaveBeenCalledWith(component.operationSet.resultSpec);
  });
});
