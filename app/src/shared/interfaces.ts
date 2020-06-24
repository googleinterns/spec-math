export type SpecOperation = 'merge' | 'filter' | 'overlay';

export interface FilesRequestBody {
  spec1: string,
  spec2: string,
  driverFile: string,
  operation: SpecOperation,
}
