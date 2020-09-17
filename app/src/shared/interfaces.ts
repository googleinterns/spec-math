import { MatStepper } from '@angular/material/stepper';
export type OperationType = 'merge' | 'overlay';

export interface MergeRequest {
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
  status: 'success' | 'conflicts';
  result?: string;
  conflicts?: MergeConflict [];
}

export interface OverlayRequest {
  spec: string;
  overlay: string;
}

export interface SpecNameInputOptions {
  newFileName: string;
  valid: boolean;
}

export interface FileUploadOptions {
  file: File;
  type: 'defaults' | 'spec' | null;
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
  type: OperationType;
}

export interface ResolvedMergeConflictOptions {
  index: number;
  value: string;
}

export interface YamlLevel {
  attribute: string;
  level: number;
}

export interface ModalInterface {
  nextStep(stepper: MatStepper): void;
}
