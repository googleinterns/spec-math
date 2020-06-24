export const processFilesMockResponseNonConflict = {
  result: "file"
};

export const processFilesMockResponseConflict = {
  conflicts: [
    {
      keypath: "sample keypath",
      option1: "Option A",
      option2: "Option B",
      resolvedValue: "Option A"
    },
    {
      keypath: "sample keypath",
      option1: "Option A",
      option2: "Option B",
      resolvedValue: "Option A"
    },
    {
      keypath: "sample keypath",
      option1: "Option A",
      option2: "Option B",
      resolvedValue: "Option A"
    }
  ]
};

export const processFilesMockResponseError = {
  error: "This operation could not be completed"
};
