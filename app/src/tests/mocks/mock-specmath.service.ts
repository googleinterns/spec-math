import { of } from 'rxjs';
import { delay } from 'rxjs/internal/operators';
import { Injectable } from '@angular/core';
import { SpecMathMergeResponse } from '../../shared/interfaces';
import { Observable } from 'rxjs';

@Injectable()
export class MockSpecMathService {
  mergeSpecsConflicts(): Observable<SpecMathMergeResponse> {
    const mockResponse: SpecMathMergeResponse = {
      status: 'conflicts',
      conflicts: [
        {
          keypath: '/dogs',
          option1: 'Option A',
          option2: 'Option B',
        },
        {
          keypath: '/cats',
          option1: 'Option A',
          option2: 'Option B',
        },
        {
          keypath: '/pets/categories',
          option1: 'Option A',
          option2: 'Option B',
        },
        {
          keypath: '/dogs',
          option1: 'Option A',
          option2: 'Option B',
        },
        {
          keypath: '/cats',
          option1: 'Option A',
          option2: 'Option B',
        },
        {
          keypath: '/pets/categories',
          option1: 'Option A',
          option2: 'Option B',
        },
        {
          keypath: '/dogs',
          option1: 'Option A',
          option2: 'Option B',
        },
        {
          keypath: '/cats',
          option1: 'Option A',
          option2: 'Option B',
        },
        {
          keypath: '/pets/categories',
          option1: 'Option A',
          option2: 'Option B',
        },
      ],
    };

    const mockObservable = of(mockResponse).pipe(delay(250));
    return mockObservable;
  }

  mergeSpecs(): Observable<SpecMathMergeResponse> {
    const mockResponse: SpecMathMergeResponse = {
      status: 'success',
      result: 'content',
    };

    const mockObservable = of(mockResponse).pipe(delay(250));
    return mockObservable;
  }
}
