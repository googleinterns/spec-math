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

import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Step3Options } from '../../../../shared/interfaces';

@Component({
  selector: 'app-modal-step-3',
  templateUrl: './step3.component.html',
  styleUrls: ['./step3.component.scss']
})
export class Step3Component implements OnInit  {
  specFilesNum: number;
  fileUploadError: boolean;
  step3Options?: Step3Options = {
    specFiles: [],
    valid: false
  };

  @Output() options: EventEmitter<Step3Options> = new EventEmitter();

  constructor() {

  }

  handleSpecFileInput(files: FileList) {
    if (files.length > this.specFilesNum) {
      this.fileUploadError = true;
      return;
    } else {
      this.fileUploadError = false;
    }

    if (files.length <= this.specFilesNum) {
      for (let i = 0; i < files.length; i++) {
        this.step3Options.specFiles.push(files[i]);
      }
    }

    if (this.step3Options.specFiles.length === this.specFilesNum) {
      this.step3Options.valid = true;
      this.options.emit(this.step3Options);
    }
  }

  removeSpecFile(index: number) {
    this.step3Options.specFiles.splice(index, 1);
  }

  ngOnInit() {
    this.specFilesNum = 2;
    this.fileUploadError = false;
  }
}
