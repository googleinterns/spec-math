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

import { Component, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent {
  // sampleMessage: string;

  constructor() {

  }

  // openDialog(): void {
  //   const dialogRef = this.dialog.open(ModalComponent, {
  //     width: '4rem',
  //     data: {
  //       sampleMessage: this.sampleMessage
  //     }
  //   });

  //   dialogRef.afterClosed().subscribe(result => {
  //     console.log('this has been closed');
  //     this.sampleMessage = result.result;
  //   });
  // }
}
