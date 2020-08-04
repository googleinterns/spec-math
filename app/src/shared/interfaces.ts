export interface SpecMathMergeRequest {
  spec1: string;
  spec2: string;
  defaultsFile?: string;
  mergeConflicts?: MergeConflict[];
}

export interface MergeConflict {
  keypath: string;
  option1: string;
  option2: string;
  resolvedValue?: string;
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

export interface SpecFilesUploadOptions {
  specFiles: File [];
  valid: boolean;
}

export interface ResolvedMergeConflictOptions {
  index: number;
  value: string;
}
