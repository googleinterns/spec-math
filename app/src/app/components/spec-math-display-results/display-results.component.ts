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
import { readFileAsString } from 'src/shared/functions';
import { Observable } from 'rxjs';
import * as fileSaver from 'file-saver';
import * as yaml from 'js-yaml';
import * as JSZip from 'jszip';

interface YamlLevel {
  attribute: string;
  level: number;
}

const yamlLevelColors = {
  0: 'grey',
  1: 'black',
  2: 'orange',
  3: 'green',
  4: 'purple',
  5: 'red',
  6: 'cyan'
};

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
  resultsLevels: YamlLevel[] = [];
  defaultsLevels: YamlLevel[] = [];
  specsLevels: YamlLevel[][];

  async downloadFile(type: string, index?: number) {
    switch (type) {
      case 'result':
        fileSaver.saveAs(this.operationSet.resultSpec.file, `${this.resultsSpecFileDisplayName}.yaml`);
        break;
      case 'defaults':
        fileSaver.saveAs(this.operationSet.defaultsFile);
        break;
      case 'spec':
        fileSaver.saveAs(this.specFiles[index]);
        break;
    }
  }

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

      this.specFiles.forEach((spec) => {
        const specFileContent = readFileAsString(spec);
        operatioSetZip.file(spec.name, specFileContent);
      });

      observer.next(operatioSetZip);
    });
  }

  specLabel(index: number) {
    return `Spec ${index + 1}`;
  }

  async flattenFile(yamlFile: File, levelsArray: YamlLevel[]) {
    await readFileAsString(yamlFile).then((res) => {
      this.flattenYaml(levelsArray, 0, yaml.load(res));
    });
  }

  flattenYaml(levelArray: YamlLevel[], level: number, objectNode: object) {
    Object.keys(objectNode).forEach((key) => {
      if (typeof(objectNode[key]) === 'object') {
        levelArray.push({
          attribute: `${key}:`,
          level,
        });
        this.flattenYaml(levelArray, level + 1, objectNode[key]);
      } else {
        levelArray.push({
          attribute: `${key}: ${objectNode[key]}`,
          level,
        });
      }
    });
  }

  async generateYamlLevels() {
    await this.flattenFile(this.operationSet.resultSpec.file, this.resultsLevels);

    if (this.defaultsFileValid) {
      await this.flattenFile(this.operationSet.defaultsFile, this.defaultsLevels);
    }

    this.specFiles.forEach(async (spec, index) => {
      await this.flattenFile(spec, this.specsLevels[index]);
    });
  }

  yamlLevelColor(level: number): string {
    return yamlLevelColors[level];
  }

  generateLevelColor(level: number) {
    return `rgb(0, 0, 0, ${1.0 - level / 10})`;
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
    this.specsRendered = new Array(this.specFiles.length).fill(false);
    this.specsLevels = new Array(this.specFiles.length).fill([]);
    this.generateYamlLevels();
  }
}
