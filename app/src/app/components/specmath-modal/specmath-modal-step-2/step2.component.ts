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
import { FormControl, Validators } from '@angular/forms';
import { Step2Options } from '../../../../shared/interfaces';

@Component({
  selector: 'app-modal-step-2',
  templateUrl: './step2.component.html',
  styleUrls: ['./step2.component.scss']
})
export class Step2Component implements OnInit  {
  specNameFormControl: FormControl;
  step2Options: Step2Options = {
    defaultsFile: null
  };

  @Output() options: EventEmitter<Step2Options> = new EventEmitter();

  constructor() {

  }

  handleDefaultsFileInput(files: FileList) {
    this.step2Options.defaultsFile = files[0];
  }

  removeDefaultsFile() {
    this.step2Options.defaultsFile = null;
  }

  ngOnInit() {

  }
}
