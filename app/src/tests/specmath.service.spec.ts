import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SpecMathService } from '../shared/services/specmath.service';
import { processFilesMockRequest } from './mocks/mockRequests';
import { processFilesMockResponseNonConflict, processFilesMockResponseConflict, processFilesMockResponseError } from './mocks/mockResponses';
import { routes } from 'src/shared/routes';

describe('SpecMathService', () => {
  let httpMockObject: HttpTestingController;
  let service: SpecMathService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SpecMathService],
    });
    httpMockObject = TestBed.get(HttpTestingController);
    service = TestBed.get(SpecMathService);
  });

  it('should be instantiated', () => {
    expect(service).toBeTruthy();
  });

  it('uploadFiles() should return an object with a result spec when non-conflicting specs are sent', () => {
    service.uploadFiles(
      processFilesMockRequest.specs,
      processFilesMockRequest.operation,
      processFilesMockRequest.driverFile
    ).subscribe((res) => {
      expect(res).toEqual(processFilesMockResponseNonConflict);
    });

    const mockRequest = httpMockObject.expectOne(routes.processFiles);
    expect(mockRequest.request.method).toBe('POST');
    mockRequest.flush(processFilesMockResponseNonConflict);
  });

  it('uploadFiles() should return an object with a list of merge conflicts when conflicting specs are sent', () => {
    service.uploadFiles(
      processFilesMockRequest.specs,
      processFilesMockRequest.operation,
      processFilesMockRequest.driverFile
    ).subscribe((res) => {
      expect(res).toEqual(processFilesMockResponseConflict);
    });

    const mockRequest = httpMockObject.expectOne(routes.processFiles);
    expect(mockRequest.request.method).toBe('POST');
    mockRequest.flush(processFilesMockResponseConflict);
  });

  it(`uploadFiles() should return an error when there's an error with the operation on the backend`, () => {
    service.uploadFiles(
      processFilesMockRequest.specs,
      processFilesMockRequest.operation,
      processFilesMockRequest.driverFile
    ).subscribe((res) => {
      expect(res).toEqual(processFilesMockResponseError);
    });

    const mockRequest = httpMockObject.expectOne(routes.processFiles);
    expect(mockRequest.request.method).toBe('POST');
    mockRequest.flush(processFilesMockResponseError);
  });

  afterEach(() => {
    // Verifies that there are no pending requests at the end of each test
    httpMockObject.verify();
  });
});
