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
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Observable } from 'rxjs';
import { SpecMathService } from './specmath.service';
import { mergeSpecsMockRequest } from '../../tests/mocks/mockRequests';
import { mergeSpecsMockResponse } from '../../tests/mocks/mockResponses';
import { routes, SPEC_MATH_URL } from '../routes';
import { SpecMathMergeResponse, SpecMathMergeRequest } from 'src/shared/interfaces';

describe('SpecMathService', () => {
  let service: SpecMathService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SpecMathService]
    });
    service = TestBed.inject(SpecMathService);
  });

  it('is instantiated', () => {
    expect(service).toBeTruthy();
  });

  describe('uploadFiles()', () => {
    let httpMockObject: HttpTestingController;
    let mergeSpecsMockCall: Observable<SpecMathMergeResponse>;

    beforeEach(() => {
      httpMockObject = TestBed.inject(HttpTestingController);
      service = TestBed.inject(SpecMathService);
      const mockSpeckSet: SpecMathMergeRequest = {
        specs: mergeSpecsMockRequest.specs,
        defaultsFile: mergeSpecsMockRequest.defaultsFile
      };

      mergeSpecsMockCall = service.mergeSpecs(mockSpeckSet);
    });

    it('receives a response when a POST request is sent to the merge endpoint', () => {
      mergeSpecsMockCall.subscribe((res: object) => {
        expect(res).toEqual(mergeSpecsMockResponse);
      });

      const mockRequest = httpMockObject.expectOne(`${SPEC_MATH_URL}${routes.mergeSpecs}`);
      expect(mockRequest.request.method).toBe('POST');
      mockRequest.flush(mergeSpecsMockResponse);
    });

    afterEach(() => {
      // Verifies that there are no pending requests at the end of each test
      httpMockObject.verify();
    });
  });
});
