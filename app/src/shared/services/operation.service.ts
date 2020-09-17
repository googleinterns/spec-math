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
import { BehaviorSubject, Observable } from 'rxjs';
import { OperationSet } from '../interfaces';

@Injectable()
export class OperationService {
  private resultsObservable = new BehaviorSubject({
    specFiles: [],
    resultSpec: null,
    valid: false,
    type: null,
  });

  /**
   * Sets the results which are used by DisplayResultsComponent after the completion of an operation
   *
   * @param results - an OperationSet object
   */
  setResults(results: OperationSet) {
    this.resultsObservable.next(results);
  }

  /**
   * Returns the latest update to results as an observable
   *
   */
  get results(): Observable<OperationSet> {
    return this.resultsObservable;
  }
}
