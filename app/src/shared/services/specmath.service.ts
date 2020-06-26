import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SpecOperation, FilesRequestBody, SpecMathResponse } from '../interfaces';
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
  constructor(private http: HttpClient) {

  }

  processFiles(
    spec1: string,
    spec2: string,
    operation: SpecOperation,
    defaultsFile?: string
  ): Observable<SpecMathResponse> {
    const requestBody: FilesRequestBody = {
      spec1,
      spec2,
      operation,
      defaultsFile
    };

    // Using a pipe to be able to convert the response object to a SpecMathResponse type
    return this.http.post(routes.processFiles, requestBody, requestOptions)
      .pipe(map(response => response as any as SpecMathResponse));
  }
}
