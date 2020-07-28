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

import { Component, Input, OnInit } from '@angular/core';
import { OperationSet } from 'src/shared/interfaces';

declare const SwaggerEditorBundle: any;
declare const SwaggerEditorStandalonePreset: any;

@Component({
  selector: 'app-display-results',
  templateUrl: './display-results.component.html',
  styleUrls: ['./display-results.component.scss']
})
export class DisplayResultsComponent implements OnInit {
  @Input() operationSet: OperationSet;

  get defaultsFileValid(): boolean {
    return !!this.operationSet?.defaultsFile;
  }

  get resultSpecFileName(): string {
    return this.operationSet.resultSpec.file.name;
  }

  get resultsSpecFileDisplayName(): string {
    return this.operationSet.resultSpec.name;
  }

  get defaultsFileName(): string {
    return this.operationSet.defaultsFile.name;
  }

  get specFiles(): File[] {
    return this.operationSet.specFiles;
  }

  ngOnInit() {
    const editor = SwaggerEditorBundle({
      dom_id: '#swagger-results',
      layout: 'StandaloneLayout',
      presets: [
        SwaggerEditorStandalonePreset
      ],
      url: 'http://rackerlabs.github.io/wadl2swagger/openstack/swagger/dbaas.json'
    });
  }
}
