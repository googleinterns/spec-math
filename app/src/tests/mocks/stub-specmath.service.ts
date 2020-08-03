import { of } from 'rxjs';
import { delay } from 'rxjs/internal/operators';
import { Injectable } from '@angular/core';
import { SpecMathMergeResponse } from '../../shared/interfaces';
import { Observable } from 'rxjs';
import { SpecMathService } from 'src/shared/services/specmath.service';

@Injectable()
export class StubSpecMathService extends SpecMathService {
  called = false;

  mergeSpecs(): Observable<SpecMathMergeResponse> {
    let mockResponse: SpecMathMergeResponse;

    if (!this.called) {
      this.called = true;
      mockResponse = {
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
        ],
      };
    } else {
      mockResponse = {
        status: 'success',
        result: 'content',
      };
    }

    const mockObservable = of(mockResponse).pipe(delay(500));
    return mockObservable;
  }
}
