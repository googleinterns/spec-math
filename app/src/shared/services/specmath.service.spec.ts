import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Observable } from 'rxjs';
import { SpecMathService } from './specmath.service';
import { mergeSpecsMockRequest } from '../../tests/mocks/mockRequests';
import { mergeSpecsMockResponse } from '../../tests/mocks/mockResponses';
import { routes } from '../routes';
import { SpecMathMergeResponse, SpecMathMergeRequest } from 'src/shared/interfaces';

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

  describe('uploadFiles()', () => {
    let httpMockObject: HttpTestingController;
    let mergeSpecsMockCall: Observable<SpecMathMergeResponse>;

    beforeEach(() => {
      httpMockObject = TestBed.inject(HttpTestingController);
      service = TestBed.inject(SpecMathService);
      const mockSpeckSet: SpecMathMergeRequest = {
        spec1: mergeSpecsMockRequest.spec1,
        spec2: mergeSpecsMockRequest.spec2,
        defaultsFile: mergeSpecsMockRequest.defaultsFile
      };

      mergeSpecsMockCall = service.mergeSpecs(mockSpeckSet);
    });

    it('receives a response when a POST request is sent to the merge endpoint', () => {
      mergeSpecsMockCall.subscribe((res: object) => {
        expect(res).toEqual(mergeSpecsMockResponse);
      });

      const mockRequest = httpMockObject.expectOne(routes.mergeSpecs);
      expect(mockRequest.request.method).toBe('POST');
      mockRequest.flush(mergeSpecsMockResponse);
    });

    afterEach(() => {
      // Verifies that there are no pending requests at the end of each test
      httpMockObject.verify();
    });
  });
});
