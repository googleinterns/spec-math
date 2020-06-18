import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SpecOperation, FilesRequestBody } from '../interfaces';
import { routes } from '../routes';

const requestOptions = {
  headers: new HttpHeaders({
    'content-type': 'application/json',
  }),
};

@Injectable()
export class SpecMathService {
  private http: HttpClient;

  constructor() {

  }

  uploadFiles(specs: string[], driverFile: string, operation: SpecOperation) {
    const requestBody: FilesRequestBody = {
      specs,
      driverFile,
      operation,
    };

    return this.http.post(routes.processFiles, requestBody, requestOptions);
  }
}
