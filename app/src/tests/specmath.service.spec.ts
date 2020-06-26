import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Observable } from 'rxjs';
import { SpecMathService } from '../shared/services/specmath.service';
import { processFilesMockRequest } from './mocks/mockRequests';
import { processFilesMockResponseNonConflict } from './mocks/mockResponses';
import { routes } from '../shared/routes';
import { SpecMathResponse } from 'src/shared/interfaces';

describe('SpecMathService', () => {
  let service: SpecMathService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SpecMathService]
    });
    service = TestBed.get(SpecMathService);
  });

  it('is instantiated', () => {
    expect(service).toBeTruthy();
  });
  
  describe('uploadFiles()', () => {
    let httpMockObject: HttpTestingController;
    let mockProcessFilesCall: Observable<SpecMathResponse>;

    beforeEach(() => {
      httpMockObject = TestBed.get(HttpTestingController);
      service = TestBed.get(SpecMathService);

      mockProcessFilesCall = service.processFiles(
        processFilesMockRequest.spec1,
        processFilesMockRequest.spec2,
        processFilesMockRequest.operation,
        processFilesMockRequest.driverFile
      );
    });

    it('receives a response when a POST request is sent to the backend', () => {
      mockProcessFilesCall.subscribe((res: object) => {
        expect(res).toEqual(processFilesMockResponseNonConflict);
      });
  
      const mockRequest = httpMockObject.expectOne(routes.processFiles);
      expect(mockRequest.request.method).toBe('POST');
      mockRequest.flush(processFilesMockResponseNonConflict);
    });
  
    afterEach(() => {
      // Verifies that there are no pending requests at the end of each test
      httpMockObject.verify();
    });
  });
});
