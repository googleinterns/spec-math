import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SpecOperation, FilesRequestBody } from './interfaces';
import { Observable } from 'rxjs';

const requestOptions = {
  headers: new HttpHeaders({
    'content-type': 'application/json',
  }),
};

@Injectable()
export class ApiService {
  private http: HttpClient;

  constructor() {
    
  }

  uploadFiles(specs: string[], driverFile: string, operation: SpecOperation) {
    const requestBody: FilesRequestBody = {
      specs,
      driverFile,
      operation,
    };

    return this.http.post( '/', requestBody, requestOptions);
  }
}
