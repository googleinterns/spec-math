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
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MergeRequest, OverlayRequest, OperationResponse } from '../interfaces';
import { routes } from '../routes';
import { Observable } from 'rxjs';

const requestOptions = {
  headers: new HttpHeaders({
    'content-type': 'application/json',
  }),
};

@Injectable()
export class SpecMathService {
  constructor(private http: HttpClient) {}

  /**
   * Performs a call to the operations/merge endpoint
   *
   * @param requestBody - An MergeRequest object which can contain
   * 1. A defaults file
   * 2. Specs used for the operation
   * 3. In the case that merge conflicts occur, an object containing conflict resolutions
   */
  mergeSpecs(requestBody: MergeRequest): Observable<OperationResponse> {
    return this.http
      .post<OperationResponse>(
        `${routes.version}${routes.mergeSpecs}`,
        requestBody,
        requestOptions
      );
  }

  /**
   * Performs a call to the operations/overlay endpoint
   *
   * @param requestBody - An OverlayRequest object which can contain
   * 1. An overlay file
   * 2. Spec used to complete the operation
   */
  overlaySpecs(requestBody: OverlayRequest): Observable<OperationResponse> {
    return this.http
      .post<OperationResponse>(
        `${routes.version}${routes.overlaySpecs}`,
        requestBody,
        requestOptions
      );
  }
}
