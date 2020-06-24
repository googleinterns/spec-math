export const processFilesMockResponseNonConflict = {
  result: "file"
};

export const processFilesMockResponseConflict = {
  conflicts: [
    {
      keypath: "sample keypath",
      optionA: "Option A",
      optionB: "Option B",
      resolvedValue: "Option A"
    },
    {
      keypath: "sample keypath",
      optionA: "Option A",
      optionB: "Option B",
      resolvedValue: "Option A"
    },
    {
      keypath: "sample keypath",
      optionA: "Option A",
      optionB: "Option B",
      resolvedValue: "Option A"
    }
  ]
};

export const processFilesMockResponseError = {
  error: "This operation could not be completed"
};
