import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SpecMathService } from '../shared/services/specmath.service';
import { processFilesMockRequest } from './mocks/mockRequests';

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
    service.uploadFiles(processFilesMockRequest.specs, processFilesMockRequest.operation, processFilesMockRequest.driverFile)
      .subscribe((res) => {
        
      });
  });

  it('uploadFiles() should return an object with a list of merge conflicts when conflicting specs are sent', () => {

  });

  afterEach(() => {
    // Verifies that there are no pending requests at the end of each test
    httpMockObject.verify();
  });
});
