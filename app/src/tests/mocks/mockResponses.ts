import { OperationResponse } from 'src/shared/interfaces';

export const operationMockResponse: OperationResponse = {
  status: 'conflicts',
  conflicts: [
    {
      keypath: 'sample keypath',
      options: ['Option A', 'Option B'],
      resolvedValue: 'Option A',
    },
    {
      keypath: 'sample keypath',
      options: ['Option A', 'Option B'],
      resolvedValue: 'Option A',
    },
    {
      keypath: 'sample keypath',
      options: ['Option A', 'Option B'],
      resolvedValue: 'Option A',
    },
  ],
};
