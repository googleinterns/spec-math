export interface SpecMathMergeRequest {
  specs: string[];
  defaultsFile?: string;
  conflictResolutions?: MergeConflict[];
}

export interface MergeConflict {
  keypath: string;
  options: string[];
  resolvedValue?: string;
}

export interface OperationResponse {
  status: 'success' | 'conflicts' | 'operation error';
  result?: string;
  conflicts?: MergeConflict [];
}

export interface SpecMathOverlayRequest {
  spec: string;
  overlay: string;
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

export interface OperationSet {
  specFiles: File [];
  defaultsFile?: File;
  resultSpec: File;
  valid: boolean;
}

export interface ResolvedMergeConflictOptions {
  index: number;
  value: string;
}

export interface YamlLevel {
  attribute: string;
  level: number;
}
