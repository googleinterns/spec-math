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
import { SpecMathMergeRequest, SpecMathMergeResponse } from '../interfaces';
import { routes, SPEC_MATH_URL } from '../routes';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

const requestOptions = {
  headers: new HttpHeaders({
    'content-type': 'application/json',
  }),
};

@Injectable()
export class SpecMathService {
  constructor(private http: HttpClient) { }

  /**
   * Performs a call to the operations/merge endpoint to perform an operation on a set of files
   *
   * @param spec1 - The first spec file to be used in the operation
   * @param spec2 - The second spec file to be used in the operation
   * @param defaultsFile - An optional file which indicates some rules to perform this operation on the backend
   */
  mergeSpecs(requestBody: SpecMathMergeRequest): Observable<SpecMathMergeResponse> {
    // Using a pipe to be able to convert the response object to a SpecMathResponse type
    return this.http.post(`${SPEC_MATH_URL}${routes.mergeSpecs}`, requestBody, requestOptions)
      .pipe(map(response => response as any as SpecMathMergeResponse));
  }
}
