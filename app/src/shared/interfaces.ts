export type SpecOperation = 'merge' | 'filter' | 'overlay';

export interface SpecMathMergeRequest {
  spec1: string;
  spec2: string;
  defaultsFile: string;
}

export interface MergeConflict {
  keypath: string;
  option1: string;
  option2: string;
  resolvedValue: string;
}

export interface SpecMathMergeResponse {
  status: string;
  result?: string;
  conflicts?: MergeConflict [];
}
