export const processFilesMockResponseNonConflict = {
  result: "file"
};

export const processFilesMockResponseConflict = {
  specs: [
    "file1",
    "file2"
  ],
  driverFile: "file",
  operation: "merge",
  conflicts: [
    {
      optionA: "Option A",
      optionB: "Option B"
    },
    {
      optionA: "Option A",
      optionB: "Option B"
    },
    {
      optionA: "Option A",
      optionB: "Option B"
    }
  ]
};

export const processFilesMockResponseError = {
  error: "This operation could not be completed"
};
