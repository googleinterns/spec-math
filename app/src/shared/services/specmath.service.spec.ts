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
import { mergeSpecsMockRequest, overlaySpecsMockRequest } from '../../tests/mocks/mockRequests';
import { operationMockResponse } from '../../tests/mocks/mockResponses';
import { routes } from '../routes';
import { OperationResponse, MergeRequest, OverlayRequest } from 'src/shared/interfaces';

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

  describe('mergeSpecs', () => {
    let httpMockObject: HttpTestingController;
    let mergeSpecsMockCall: Observable<OperationResponse>;

    beforeEach(() => {
      httpMockObject = TestBed.inject(HttpTestingController);
      service = TestBed.inject(SpecMathService);
      const mockMergeSet: MergeRequest = mergeSpecsMockRequest;

      mergeSpecsMockCall = service.mergeSpecs(mockMergeSet);
    });

    it('receives a response when a POST request is sent to the merge endpoint', () => {
      mergeSpecsMockCall.subscribe((res: object) => {
        expect(res).toEqual(operationMockResponse);
      });

      const mockRequest = httpMockObject.expectOne(`${routes.version}${routes.mergeSpecs}`);
      expect(mockRequest.request.method).toBe('POST');
      mockRequest.flush(operationMockResponse);
    });

    afterEach(() => {
      // Verifies that there are no pending requests at the end of each test
      httpMockObject.verify();
    });
  });

  describe('overlaySpecs', () => {
    let httpMockObject: HttpTestingController;
    let overlaySpecsMockCall: Observable<OperationResponse>;

    beforeEach(() => {
      httpMockObject = TestBed.inject(HttpTestingController);
      service = TestBed.inject(SpecMathService);
      const mockOverlaySet: OverlayRequest = overlaySpecsMockRequest;

      overlaySpecsMockCall = service.overlaySpecs(mockOverlaySet);
    });

    it('receives a response when a POST request is sent to the overlay endpoint', () => {
      overlaySpecsMockCall.subscribe((res: object) => {
        expect(res).toEqual(operationMockResponse);
      });

      const mockRequest = httpMockObject.expectOne(`${routes.version}${routes.overlaySpecs}`);
      expect(mockRequest.request.method).toBe('POST');
      mockRequest.flush(operationMockResponse);
    });

    afterEach(() => {
      // Verifies that there are no pending requests at the end of each test
      httpMockObject.verify();
    });
  });
});
