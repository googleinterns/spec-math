import { HttpHeaders } from "@angular/common/http";

export interface SpecOperation {
  string: "merge" | "filter",
}

export interface FilesRequestBody {
  specs: string [],
  driverFile: string,
  operation: SpecOperation,
}