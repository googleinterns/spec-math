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

import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { DefaultsFileUploadOptions } from '../../../../shared/interfaces';

@Component({
  selector: 'app-defaults-file-upload',
  templateUrl: './defaults-file-upload.component.html',
  styleUrls: ['./defaults-file-upload.component.scss']
})
export class DefaultsFileUploadComponent implements OnInit  {
  step2Options?: DefaultsFileUploadOptions = {
    defaultsFile: null
  };

  @Output() options: EventEmitter<DefaultsFileUploadOptions> = new EventEmitter();

  constructor() {

  }

  handleDefaultsFileInput(files: FileList) {
    this.step2Options.defaultsFile = files[0];
    this.options.emit(this.step2Options);
  }

  removeDefaultsFile() {
    this.step2Options.defaultsFile = null;
  }

  ngOnInit() {

  }
}
