import { FilesRequestBody } from 'src/shared/interfaces';

export const processFilesMockRequest: FilesRequestBody = {
  specs: [
    "file1",
    "file2"
  ],
  driverFile: 'file',
  operation: 'merge'
}
