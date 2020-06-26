export type SpecOperation = 'merge' | 'filter' | 'overlay';

export interface FilesRequestBody {
  spec1: string,
  spec2: string,
  defaultsFile: string,
  operation: SpecOperation,
}

export interface OperationConflict {
  keypath: string,
  option1: string,
  option2: string,
  resolvedValue: string
}

export interface SpecMathResponse {
  status: string,
  result?: string,
  conflicts?: OperationConflict [],
}
