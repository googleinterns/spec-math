import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Observable } from 'rxjs';
import { SpecMathService } from '../shared/services/specmath.service';
import { mergeSpecsMockRequest } from './mocks/mockRequests';
import { mergeFilesMockResponse } from './mocks/mockResponses';
import { routes } from '../shared/routes';
import { SpecMathMergeResponse } from 'src/shared/interfaces';

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

      mergeSpecsMockCall = service.mergeSpecs(
        mergeSpecsMockRequest.spec1,
        mergeSpecsMockRequest.spec2,
        mergeSpecsMockRequest.defaultsFile
      );
    });

    it('receives a response when a POST request is sent to the merge endpoint', () => {
      mergeSpecsMockCall.subscribe((res: object) => {
        expect(res).toEqual(mergeFilesMockResponse);
      });

      const mockRequest = httpMockObject.expectOne(routes.mergeSpecs);
      expect(mockRequest.request.method).toBe('POST');
      mockRequest.flush(mergeFilesMockResponse);
    });

    afterEach(() => {
      // Verifies that there are no pending requests at the end of each test
      httpMockObject.verify();
    });
  });
});
