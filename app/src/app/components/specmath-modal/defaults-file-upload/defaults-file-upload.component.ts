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

import { Component, EventEmitter, Output, ViewChild, DebugElement } from '@angular/core';
import { DefaultsFileUploadOptions } from '../../../../shared/interfaces';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-defaults-file-upload',
  templateUrl: './defaults-file-upload.component.html',
  styleUrls: ['./defaults-file-upload.component.scss']
})
export class DefaultsFileUploadComponent  {
  @ViewChild('defaultsInput') defaultsUpload: DebugElement;
  defaultsFileUploadOptions?: DefaultsFileUploadOptions = {
    defaultsFile: null
  };

  @Output() options: EventEmitter<DefaultsFileUploadOptions> = new EventEmitter();

  constructor(readonly router: Router, readonly dialog: MatDialog) { }

  handleDefaultsFileInput(files: FileList) {
    this.defaultsFileUploadOptions.defaultsFile = files[0];
    this.defaultsUpload.nativeElement.value = '';
    this.options.emit(this.defaultsFileUploadOptions);
  }

  navigateToDefaultsPage() {
    this.dialog.closeAll();
    this.router.navigateByUrl('defaults');
  }

  removeDefaultsFile() {
    this.defaultsFileUploadOptions.defaultsFile = null;
  }
}
