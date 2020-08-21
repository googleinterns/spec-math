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

import { Component } from '@angular/core';

enum Diagram {
  'merge',
  'overlay',
  'filter'
}

@Component({
  selector: 'app-about-page',
  templateUrl: './about-page.component.html',
  styleUrls: ['./about-page.component.scss'],
})
export class AboutPageComponent {
  debugEnabled = false;

  debugMode() {
    if (this.debugEnabled) {
      document.body.classList.remove('debug-mode');
      this.debugEnabled = false;
    } else {
      document.body.classList.add('debug-mode');
      this.debugEnabled = true;
    }
  }
}
