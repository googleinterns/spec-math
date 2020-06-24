import { type } from 'os'
export type SpecOperation = 'merge' | 'filter';

export interface FilesRequestBody {
  specs: string [],
  driverFile: string,
  operation: SpecOperation,
}
