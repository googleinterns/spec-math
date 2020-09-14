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
import { MatStepper } from '@angular/material/stepper';
import {
  SpecNameInputOptions,
  DefaultsFileUploadOptions,
  SpecFilesUploadOptions,
  MergeConflict,
  OperationSet,
  ResolvedMergeConflictOptions,
  MergeRequest,
} from 'src/shared/interfaces';
import { SpecMathService } from 'src/shared/services/specmath.service';
import { readFileAsString } from 'src/shared/functions';
import { SpecMathModalComponent } from '../modal';

enum Steps {
  specNameInput = 0,
  defaultsFileUpload = 1,
  specFilesUpload = 2,
  confirmOperation = 3,
  resolveConflicts = 4,
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
    nextStep: Steps.specFilesUpload,
    previousStep: Steps.specNameInput,
    nextButtonText: 'Next',
    stepLabel: 'Defaults file',
  },
  [Steps.specFilesUpload]: {
    toolTipText: 'You must upload a set of spec files',
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
  [Steps.resolveConflicts]: {
    previousStep: Steps.confirmOperation,
    toolTipText: 'You must resolve all merge conflicts',
    nextButtonText: 'Resolve',
    stepLabel: 'Resolving conflicts',
  }
};

@Component({
  selector: 'app-merge-modal',
  templateUrl: './merge-modal.component.html',
  styleUrls: ['../modal-styling.scss']
})
export class MergeModalComponent extends SpecMathModalComponent {
  currentStep = Steps.specNameInput;
  specNameInputOptions: SpecNameInputOptions = {
    newFileName: '',
    valid: false
  };
  defaultsFileUploadOptions: DefaultsFileUploadOptions = {
    defaultsFile: null
  };
  specFilesUploadOptions: SpecFilesUploadOptions = {
    specFiles: [],
    valid: false,
  };
  resultSpec: File;
  mergeConflicts: MergeConflict[];
  loadingOperation = false;

  async nextStep(stepper: MatStepper) {
    const currStep = stepList[this.currentStep];

    if (currStep.lastBaseStep) {
      this.loadingOperation = true;
      await this.mergeOperation();
      this.loadingOperation = false;
      this.cdr.detectChanges();

      if (!this.hasMergeConflicts) {
        this.finalizeSteps();
        return;
      }
    }

    if (this.mergeConflictsResolved) {
      if (!this.hasMergeConflicts) {
        this.finalizeSteps();
        return;
      }

      this.loadingOperation = true;
      await this.sendResolvedConflicts();
      this.loadingOperation = false;
      this.finalizeSteps();
      return;
    }

    stepper.selectedIndex = ++this.currentStep;
  }

  async mergeOperation() {
    const mergeSet = await this.generateMergeSet();
    const callResponse = await this.specMathService.mergeSpecs(mergeSet).toPromise();

    switch (callResponse.status) {
      case 'conflicts':
        this.mergeConflicts = callResponse.conflicts;
        break;
      case 'success':
        this.resultSpec = new File([callResponse.result], `${this.specNameInputOptions.newFileName}.yaml`);
        break;
    }
  }

  async sendResolvedConflicts() {
    const mergeSet = await this.generateMergeSet();
    const callResponse = await this.specMathService.mergeSpecs(mergeSet).toPromise();
    this.resultSpec = new File([callResponse.result], `${this.specNameInputOptions.newFileName}.yaml`);
  }

  async generateMergeSet(): Promise<MergeRequest> {
    const requestBody: MergeRequest = {
      specs: await Promise.all(this.specFilesUploadOptions.specFiles.map((spec) => readFileAsString(spec)))
    };

    if (this.defaultsFileUploadOptions?.defaultsFile) {
      requestBody.defaultsFile = await readFileAsString(this.defaultsFileUploadOptions.defaultsFile);
    }

    if (this.hasMergeConflicts) {
      requestBody.conflictResolutions = this.mergeConflicts;
    }

    return requestBody;
  }

  get hasMergeConflicts() {
    return !!this.mergeConflicts;
  }

  get validFiles(): boolean {
    return this.specFilesUploadOptions.valid;
  }

  get newFileName(): string {
    return this.specNameInputOptions?.newFileName || '';
  }

  get nextButtonTooltipText(): string {
    return stepList[this.currentStep].toolTipText;
  }

  get nextButtonEnabled(): boolean {
    switch (this.currentStep) {
      case Steps.specNameInput:
        return this.specNameInputOptions?.valid;
      case Steps.specFilesUpload:
        return this.specFilesUploadOptions?.valid;
      case Steps.resolveConflicts:
        return this.mergeConflictsResolved;
      default:
        return true;
    }
  }

  get conflictsCount(): string {
    const resolvedConflicts = this.mergeConflicts.filter(curr => curr.resolvedValue).length;
    return `${resolvedConflicts}/${this.mergeConflicts.length}`;
  }

  get shouldDisplayConflictCounter(): boolean {
    return this.currentStep === Steps.resolveConflicts;
  }

  get mergeConflictsResolved(): boolean {
    return !!this.mergeConflicts && this.mergeConflicts.every((conflict) => conflict.resolvedValue);
  }

  handleResolvedOptions(resolvedOptions: ResolvedMergeConflictOptions) {
    this.mergeConflicts[resolvedOptions.index].resolvedValue = resolvedOptions.value;
  }
}
