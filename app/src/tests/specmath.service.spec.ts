import { TestBed } from '@angular/core/testing';
import { SpecMathService } from '../shared/services/specmath.service';

describe('Call the Spec Math service', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SpecMathService],
    });
  });

  it('should respond with a conflict free response', () => {
    console.log("I was called");
  });
});
