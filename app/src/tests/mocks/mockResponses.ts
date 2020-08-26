import { SpecMathMergeResponse } from 'src/shared/interfaces';

export const mergeSpecsMockResponse: SpecMathMergeResponse = {
  status: 'conflict detected',
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
