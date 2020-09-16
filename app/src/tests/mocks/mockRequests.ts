import { MergeRequest, OverlayRequest } from 'src/shared/interfaces';

export const mergeSpecsMockRequest: MergeRequest = {
  specs: ['file1', 'file2'],
  defaultsFile: 'file',
};

export const overlaySpecsMockRequest: OverlayRequest = {
  spec: 'file',
  overlay: 'file',
};
