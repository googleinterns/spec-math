import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SpecMathService } from '../shared/services/specmath.service';

describe('SpecMathService', () => {
  let httpMockObject: HttpClientTestingModule;
  let service: SpecMathService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SpecMathService],
    });
    httpMockObject = TestBed.get(HttpClientTestingModule);
    service = TestBed.get(SpecMathService);
  });

  it('should be instantiated', () => {
    expect(service).toBeTruthy();
  });
});
