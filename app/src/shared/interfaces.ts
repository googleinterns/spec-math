export type SpecOperation = 'merge' | 'filter' | 'overlay';

export interface SpecMathRequest {
  spec1: string;
  spec2: string;
  defaultsFile: string;
  operation: SpecOperation;
}

export interface MergeConflict {
  keypath: string;
  option1: string;
  option2: string;
  resolvedValue: string;
}

export interface SpecMathResponse {
  status: string;
  result?: string;
  conflicts?: MergeConflict [];
}
