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

import { TestBed } from '@angular/core/testing';
import { OperationService } from './operation.service';

describe('OperationService', () => {
  let service: OperationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OperationService],
    });
    service = TestBed.inject(OperationService);
  });

  it('is instantiated', () => {
    expect(service).toBeTruthy();
  });

  it('sets and reads results when an OperationSet is provided through setResults and read through getResults', () => {
    service.setResults({
      specFiles: [
        new File(['openapi: 3.0.0'], 'spec1.yaml'),
        new File(['openapi: 3.0.0'], 'spec2.yaml'),
      ],
      resultSpec: new File(['openapi: 3.0.0'], 'results.yaml'),
      valid: true,
      type: 'merge'
    });

    service.results.subscribe((results) => {
      expect(results).toEqual({
        specFiles: [
          new File(['openapi: 3.0.0'], 'spec1.yaml'),
          new File(['openapi: 3.0.0'], 'spec2.yaml'),
        ],
        resultSpec: new File(['openapi: 3.0.0'], 'results.yaml'),
        valid: true,
        type: 'merge'
      });
    });
  });
});
