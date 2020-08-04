import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SpecMathMergeRequest, SpecMathMergeResponse } from '../interfaces';
import { routes } from '../routes';
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
   * Performs a call to the /1.0/specMath/merge endpoint to perform an operation on a set of files
   *
   * @param spec1 - The first spec file to be used in the operation
   * @param spec2 - The second spec file to be used in the operation
   * @param defaultsFile - An optional file which indicates some rules to perform this operation on the backend
   */
  mergeSpecs(requestBody: SpecMathMergeRequest): Observable<SpecMathMergeResponse> {
    // Using a pipe to be able to convert the response object to a SpecMathResponse type
    return this.http.post(routes.mergeSpecs, requestBody, requestOptions)
      .pipe(map(response => response as any as SpecMathMergeResponse));
  }
}
