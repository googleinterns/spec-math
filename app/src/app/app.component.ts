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
import { MatDialog } from '@angular/material/dialog';
import { ModalComponent } from './components/specmath-modal/modal.component';
import { OperationSet } from 'src/shared/interfaces';
import { MediaMatcher } from '@angular/cdk/layout';
import { Router, RouterEvent, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { OperationService } from 'src/shared/services/results.service';

type Route = 'home' | 'result' | 'defaults' | 'about';

type Routes = {
  [route in Route]: string;
};

const MOBILE_MEDIA_QUERY = '(max-width: 768px)';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  mobileQuery: MediaQueryList;

  constructor(
    readonly router: Router,
    readonly dialog: MatDialog,
    readonly results: OperationService,
    media: MediaMatcher
  ) {
    this.mobileQuery = media.matchMedia(MOBILE_MEDIA_QUERY);
  }

  openDialog() {
    this.dialog
      .open(ModalComponent)
      .afterClosed()
      .subscribe((results?: OperationSet) => {
        if (results) {
          this.results.setResults(results);
          this.router.navigateByUrl('results');
        }
      });
  }
}
