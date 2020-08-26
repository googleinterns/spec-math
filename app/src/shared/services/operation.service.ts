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

import { Injectable } from '@angular/core';
import { OperationSet } from '../interfaces';

@Injectable()
export class OperationService {
  private results: OperationSet = {
    specFiles: [],
    resultSpec: null,
    valid: false,
  };

  /**
   * Sets the results which are used by DisplayResultsComponent after the completion of an operation
   *
   * @param results - an OperationSet object
   */
  setResults(results: OperationSet) {
    this.results = results;
  }

  /**
   * Obtains the results which may be used in other components
   *
   * @returns an OperationSet object
   */
  getResults() {
    return this.results;
  }
}
