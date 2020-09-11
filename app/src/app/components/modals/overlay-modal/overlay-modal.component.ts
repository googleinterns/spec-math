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

import { Component, ChangeDetectorRef } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

enum Steps {
  specNameInput = 0,
  defaultsFileUpload = 1,
  specFilesUpload = 2,
  confirmOperation = 3,
}

type StepOptions = {
  [key in Steps]: {
    toolTipText?: string,
    nextStep?: Steps,
    previousStep?: Steps,
    nextButtonText?: string,
    lastBaseStep?: boolean,
    stepLabel: string,
  };
};

const stepList: StepOptions = {
  [Steps.specNameInput]: {
    toolTipText: 'You must name your new spec',
    nextStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next',
    stepLabel: 'Name new spec',
  },
  [Steps.defaultsFileUpload]: {
    toolTipText: 'You must upload a default',
    nextStep: Steps.specFilesUpload,
    previousStep: Steps.specNameInput,
    nextButtonText: 'Next',
    stepLabel: 'Defaults file',
  },
  [Steps.specFilesUpload]: {
    toolTipText: 'You must upload a spec file',
    nextStep: Steps.confirmOperation,
    previousStep: Steps.defaultsFileUpload,
    nextButtonText: 'Next',
    stepLabel: 'Spec files'
  },
  [Steps.confirmOperation]: {
    previousStep: Steps.specFilesUpload,
    nextButtonText: 'Confirm',
    stepLabel: 'Confirm operation',
    lastBaseStep: true,
  },
};

@Component({
  selector: 'app-modal',
  templateUrl: './overlay-modal.component.html',
  styleUrls: ['./overlay-modal.component.scss']
})
export class OverlayModalComponent {
  
}
