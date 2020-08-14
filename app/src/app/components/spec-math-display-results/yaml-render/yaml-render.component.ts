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
import { YamlLevel } from 'src/shared/interfaces';
import { flattenYamlFile } from 'src/shared/functions';

@Component({
  selector: 'app-yaml-render',
  templateUrl: './yaml-render.component.html',
  styleUrls: ['./yaml-render.component.scss']
})
export class YamlRenderComponent implements OnInit {
  @Input() yamlFile: File;
  yamlLevels: YamlLevel[] = [];

  flattenFile(yamlFile: File) {
    flattenYamlFile(yamlFile).then((yamlArray) => {
      this.yamlLevels = yamlArray;
    });
  }

  ngOnInit() {
    this.flattenFile(this.yamlFile);
  }
}
