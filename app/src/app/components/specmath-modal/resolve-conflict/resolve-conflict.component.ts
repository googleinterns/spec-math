// Copyright 2020 Google LLC

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     https://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { MergeConflict, ResolvedMergeConflictOptions } from 'src/shared/interfaces';

@Component({
  selector: 'app-resolve-conflict',
  templateUrl: './resolve-conflict.component.html',
  styleUrls: ['./resolve-conflict.component.scss']
})
export class ResolveConflictComponent implements OnInit {
  @Input() mergeConflicts: MergeConflict[];
  @Output() resolvedOptions: EventEmitter<ResolvedMergeConflictOptions> = new EventEmitter();
  localConflicts: MergeConflict[];

  emitResolvedValue(value: string, index: number) {
    this.localConflicts[index].resolvedValue = value;

    const resolvedConflict: ResolvedMergeConflictOptions = {
      value,
      index
    };
    console.log(value);
    console.log(index);
    // this.resolvedOptions.emit(resolvedConflict);
  }

  ngOnInit() {
    this.localConflicts = [...this.mergeConflicts];
  }
}
