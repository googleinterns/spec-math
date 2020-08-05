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
import * as yaml from 'js-yaml';
import { readFileAsString } from 'src/shared/functions';
import { MatTabChangeEvent } from '@angular/material/tabs';
import * as fileSaver from 'file-saver';
import * as JSZip from 'jszip';
import { Content } from '@angular/compiler/src/render3/r3_ast';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-display-results',
  templateUrl: './display-results.component.html',
  styleUrls: ['./display-results.component.scss']
})
export class DisplayResultsComponent implements OnInit {
  @Input() operationSet: OperationSet;
  resultsRendered = false;
  defaultsRendered = false;
  specsRendered: boolean[];

  downloadOperationSet() {
    this.generateOperationSetZip().subscribe(set => {
      set.generateAsync({ type: 'blob' }).then((zipContent) => {
        fileSaver.saveAs(zipContent, `${this.resultsSpecFileDisplayName}-merge-set`);
      });
    });
  }

  generateOperationSetZip(): Observable<JSZip> {
    return new Observable((observer) => {
      const operatioSetZip = new JSZip();

      const resultFileContent = readFileAsString(this.operationSet.resultSpec.file);
      operatioSetZip.file(this.resultSpecFileName, resultFileContent);

      if (this.defaultsFileValid) {
        const defaultsFileContent = readFileAsString(this.operationSet.defaultsFile);
        operatioSetZip.file(this.defaultsFileName, defaultsFileContent);
      }

      this.operationSet.specFiles.forEach((spec) => {
        const specFileContent = readFileAsString(spec);
        operatioSetZip.file(spec.name, specFileContent);
      });

      console.log(operatioSetZip.files);
      observer.next(operatioSetZip);
    });
  }

  specLabel(index: number) {
    return `Spec ${index + 1}`;
  }

  renderYamlToDiv(divId: string, yamlJSON: object) {
    const renderDiv = document.getElementById(divId);
    Object.keys(yamlJSON).forEach((key) => {
      const fileTextNode = document.createTextNode(`${key}: ${yamlJSON[key]}`);
      const renderNode = document.createElement('p').appendChild(fileTextNode);
      renderDiv.appendChild(renderNode);
      renderDiv.appendChild(document.createElement('br'));
    });
  }

  renderResultsFile() {
    if (this.resultsRendered) {
      return;
    }
    this.resultsRendered = true;

    readFileAsString(this.operationSet.resultSpec.file).then((res) => {
      this.renderYamlToDiv('results-file-render', yaml.load(res));
    });
  }

  renderDefaultsFile() {
    if (this.defaultsRendered) {
      return;
    }
    this.defaultsRendered = true;

    readFileAsString(this.operationSet.defaultsFile).then((res) => {
      this.renderYamlToDiv('defaults-file-render', yaml.load(res));
    });
  }

  renderSpecFile(index: number) {
    if (this.specsRendered[index]) {
      return;
    }
    this.specsRendered[index] = true;

    readFileAsString(this.operationSet.specFiles[index]).then((res) => {
      this.renderYamlToDiv(`spec-file-${index}-render`, yaml.load(res));
    });
  }

  renderFile(event: MatTabChangeEvent) {
    const tab = event.tab.textLabel;

    switch (tab) {
      case 'Defaults':
        this.renderDefaultsFile();
        break;
      case 'Result':
        this.renderResultsFile();
        break;
    }

    if (tab.includes('Spec')) {
      const index = parseInt(tab.slice(tab.length - 1), 10) - 1;
      this.renderSpecFile(index);
    }
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

  ngOnInit() {
    this.renderResultsFile();
    this.specsRendered = new Array(this.operationSet.specFiles.length).fill(false);
  }
}
