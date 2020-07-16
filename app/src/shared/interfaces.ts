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

export interface SpecNameInputOptions {
  newFileName: string;
  valid: boolean;
}

export interface DefaultsFileUploadOptions {
  defaultsFile: File;
}

export interface Step3Options {
  specFiles: File [];
  valid: boolean;
}
