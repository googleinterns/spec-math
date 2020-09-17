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

import { Component, EventEmitter, Output, ViewChild, DebugElement, Input } from '@angular/core';
import { FileUploadOptions } from 'src/shared/interfaces';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

type FileType = 'defaults' | 'spec';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent  {
  @ViewChild('fileInput') defaultsUpload: DebugElement;
  fileUploadOptions?: FileUploadOptions = {
    file: null,
    type: null
  };

  @Input() fileType: FileType;
  @Output() options: EventEmitter<FileUploadOptions> = new EventEmitter();

  constructor(readonly router: Router, readonly dialog: MatDialog) { }

  handleFileInput(files: FileList) {
    this.fileUploadOptions.file = files[0];
    this.fileUploadOptions.type = this.fileType;
    this.defaultsUpload.nativeElement.value = '';
    this.options.emit(this.fileUploadOptions);
  }

  navigateToDefaultsPage() {
    this.dialog.closeAll();
    this.router.navigateByUrl('defaults');
  }

  deleteFile() {
    this.fileUploadOptions.file = null;
  }

  get stepFileLabel(): string {
    switch (this.fileType) {
      case 'defaults':
        return 'Select a defaults file';
      case 'spec':
        return 'Select a spec file';
    }
  }

  get shouldDisplayLearnMoreLink(): boolean {
    return this.fileType === 'defaults';
  }

  get fileTypeIcon(): string {
    switch (this.fileType) {
      case 'defaults':
        return 'api';
      case 'spec':
        return 'description';
    }
  }
}
