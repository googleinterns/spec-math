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

import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ModalComponent } from './components/specmath-modal/modal.component';
import { OperationSet } from 'src/shared/interfaces';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  // operationSet: OperationSet = {
  //   specFiles: [],
  //   resultSpec: null,
  //   valid: false
  // };
  operationSet: OperationSet = {
    specFiles: [new File(['content'], 'spec1.yaml'), new File(['content'], 'spec1.yaml')],
    defaultsFile: new File(['content'], 'deafults.yaml'),
    valid: true,
    resultSpec: {
      name: 'new_spec',
      file: new File(['content'], 'results.yaml'),
    }
  };

  constructor(readonly dialog: MatDialog) {

  }

  openDialog(): void {
    this.dialog.open(ModalComponent).afterClosed().subscribe((results: OperationSet) => {
      this.operationSet = results;
    });
  }
}
