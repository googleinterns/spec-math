import { SpecMathMergeResponse } from 'src/shared/interfaces';

export const mergeSpecsMockResponse: SpecMathMergeResponse = {
  status: 'conflict detected',
  conflicts: [
    {
      keypath: 'sample keypath',
      option1: 'Option A',
      option2: 'Option B',
      resolvedValue: 'Option A'
    },
    {
      keypath: 'sample keypath',
      option1: 'Option A',
      option2: 'Option B',
      resolvedValue: 'Option A'
    },
    {
      keypath: 'sample keypath',
      option1: 'Option A',
      option2: 'Option B',
      resolvedValue: 'Option A'
    }
  ]
};
