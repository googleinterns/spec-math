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

import { Component, EventEmitter, Output, ViewChild, DebugElement } from '@angular/core';
import { SpecFilesUploadOptions } from '../../../../shared/interfaces';

const filesListToArray = (files: FileList) => Array.from(files);

@Component({
  selector: 'app-spec-files-upload',
  templateUrl: './spec-files-upload.component.html',
  styleUrls: ['./spec-files-upload.component.scss']
})
export class SpecFilesUploadComponent {
  @ViewChild('specsInput') specUploads: DebugElement;

  fileUploadError = false;
  specFilesUploadOptions: SpecFilesUploadOptions = {
    specFiles: [],
    valid: false
  };

  @Output() options: EventEmitter<SpecFilesUploadOptions> = new EventEmitter();

  handleSpecFileInput(files: FileList) {
    this.validateFiles(filesListToArray(files));
    this.specUploads.nativeElement.value = '';
  }

  validateFiles(files: File[]) {
    this.specFilesUploadOptions.specFiles.push(...Array.from(files));
    this.emitFileStatus();
  }

  removeSpecFile(index: number) {
    this.specFilesUploadOptions.specFiles.splice(index, 1);
    this.emitFileStatus();
  }

  emitFileStatus() {
    this.specFilesUploadOptions.valid = this.specFilesUploadOptions.specFiles.length >= 2;

    const specFilesUploadOptionsCopy: SpecFilesUploadOptions = {
      specFiles: [...this.specFilesUploadOptions.specFiles],
      valid: this.specFilesUploadOptions.valid
    };
    this.options.emit(specFilesUploadOptionsCopy);
  }
}
