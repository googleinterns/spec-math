import { of } from 'rxjs';
import { delay } from 'rxjs/internal/operators';
import { Injectable } from '@angular/core';
import { OperationResponse } from '../../shared/interfaces';
import { Observable } from 'rxjs';
import { SpecMathService } from 'src/shared/services/specmath.service';

@Injectable()
export class StubSpecMathService extends SpecMathService {
  called = false;

  mergeSpecs(): Observable<OperationResponse> {
    let mockResponse: OperationResponse;

    if (!this.called) {
      this.called = true;
      mockResponse = {
        status: 'conflicts',
        conflicts: [
          {
            keypath: '[paths, /pets, get, summary]',
            options: [
              'get the specified pets',
              'get the specified pet',
              'get the other pets',
            ],
          },
          {
            keypath: '[paths, /pets, post, summary]',
            options: [
              'get the specified cat',
              'get the specified cats',
              'get the other cats',
            ],
          },
        ],
      };
    } else {
      mockResponse = {
        status: 'success',
        result: `
        openapi: 3.0.0
        info:
          title: Default Swagger Petstore
          license:
            name: MIT
          version: 1.0.2
        servers:
          - url: http://petstore.swagger.io/v1
        paths:
          /pets:
            get:
              summary: get the pets
              tags:
                - pets
                - dogs
              responses:
                '201':
                  description: Null response
                '202':
                  description: Hello world
              operationId: listPets
            post:
              summary: Create a pet
          /pets/{petId}:
            get:
              summary: hello
              parameters:
                - name: petId1
                  in: path
                - name: petId2
                  in: path2
              responses:
                '200':
                  description: Expected response to a valid request
                  content:
                    application/json:
                      schema:
                        $ref: "#/components/schemas/Pet"
          /newPath:
            get:
              parameters:
                - name: petId
                  schema:
                    type: string
        components:
          schemas:
            Pet:
              type: object
              required:
                - id
                - name
              properties:
                id:
                  type: integer
                  format: int64
                name:
                  type: string
                tag:
                  type: string`,
      };
    }

    const mockObservable = of(mockResponse).pipe(delay(500));
    return mockObservable;
  }

  overlaySpecs(): Observable<OperationResponse> {
    const mockResponse: OperationResponse = {
      status: 'success',
      result: `
      openapi: 3.0.0
      info:
        title: Default Swagger Petstore
        license:
          name: MIT
        version: 1.0.2
      servers:
        - url: http://petstore.swagger.io/v1
      paths:
        /pets:
          get:
            summary: get the pets
            tags:
              - pets
              - dogs
            responses:
              '201':
                description: Null response
              '202':
                description: Hello world
            operationId: listPets
          post:
            summary: Create a pet
        /pets/{petId}:
          get:
            summary: hello
            parameters:
              - name: petId1
                in: path
              - name: petId2
                in: path2
            responses:
              '200':
                description: Expected response to a valid request
                content:
                  application/json:
                    schema:
                      $ref: "#/components/schemas/Pet"
        /newPath:
          get:
            parameters:
              - name: petId
                schema:
                  type: string
      components:
        schemas:
          Pet:
            type: object
            required:
              - id
              - name
            properties:
              id:
                type: integer
                format: int64
              name:
                type: string
              tag:
                type: string`,
    };

    const mockObservable = of(mockResponse).pipe(delay(500));
    return mockObservable;
  }
}
