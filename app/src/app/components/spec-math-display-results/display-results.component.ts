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

import { Component, Input, AfterViewInit } from '@angular/core';
import { OperationSet } from 'src/shared/interfaces';
import * as yaml from 'js-yaml';
import { readFileAsString } from 'src/shared/functions';

@Component({
  selector: 'app-display-results',
  templateUrl: './display-results.component.html',
  styleUrls: ['./display-results.component.scss']
})
export class DisplayResultsComponent implements AfterViewInit {
  @Input() operationSet: OperationSet;

  downloadOperationSet() {
    console.log('create and download zip');
  }

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

  renderYamlToDiv(divId: string, yamlJSON: object) {
    const renderDiv = document.getElementById(divId);

    console.log(renderDiv);

    Object.keys(yamlJSON).forEach((key) => {
      const fileTextNode = document.createTextNode(`${key}: ${yamlJSON[key]}`);
      const renderNode = document.createElement('p').appendChild(fileTextNode);
      renderDiv.appendChild(renderNode);
      renderDiv.appendChild(document.createElement('br'));
    });
  }

  ngAfterViewInit() {
    readFileAsString(this.operationSet.resultSpec.file).then((res) => {
      this.renderYamlToDiv('results-file-render',  yaml.load(res));
    });

    if (this.defaultsFileValid) {
      readFileAsString(this.operationSet.defaultsFile).then((res) => {
        console.log(yaml.load(res));
        this.renderYamlToDiv('defaults-file-render',  yaml.load(res));
      });
    }

    this.operationSet.specFiles.forEach((file, index) => {
      readFileAsString(file).then((res) => {
        console.log(yaml.load(res));
        this.renderYamlToDiv(`spec-file-${index}-render`,  yaml.load(res));
      });
    });
  }
}
