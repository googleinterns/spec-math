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
import { FormControl, Validators } from '@angular/forms';
import { Step1Options } from '../../../../shared/interfaces';

@Component({
  selector: 'app-spec-name-input',
  templateUrl: './spec-name-input.component.html',
  styleUrls: ['./spec-name-input.component.scss']
})
export class SpecNameInputComponent implements OnInit  {
  specNameFormControl: FormControl;
  step1Options: Step1Options = {
    newFileName: '',
    valid: false,
  };

  @Output() options: EventEmitter<Step1Options> = new EventEmitter();

  constructor() {

  }

  ngOnInit() {
    this.specNameFormControl = new FormControl ('', [
      Validators.required,
      Validators.pattern('[a-zA-Z0-9_-]*')
    ]);

    this.specNameFormControl.valueChanges.subscribe(input => {
      this.step1Options.newFileName = input;
      this.step1Options.valid = this.specNameFormControl.valid;
      this.options.emit(this.step1Options);
    });
  }
}
