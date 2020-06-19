export interface SpecOperation {
  string: "merge" | "filter",
}

export interface FilesRequestBody {
  specs: string [],
  driverFile: string,
  operation: SpecOperation,
}