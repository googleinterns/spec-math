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

enum MainPanel {
  'empty',
  'result',
  'defaults',
  'about',
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  mainPanelContent = MainPanel.empty;
  mobileQuery: MediaQueryList;
  operationSet: OperationSet = {
    specFiles: [],
    resultSpec: null,
    valid: false,
  };

  constructor(
    private router: Router,
    readonly dialog: MatDialog,
    media: MediaMatcher
  ) {
    this.mobileQuery = media.matchMedia('(max-width: 768px)');
    router.events
      .pipe(filter((event: RouterEvent) => event instanceof NavigationEnd))
      .subscribe((path: RouterEvent) => {
        this.handleRoute(path.url);
      });
  }

  selectSideNavOption(option: MainPanel) {
    switch (option) {
      case MainPanel.defaults:
        this.router.navigateByUrl('/defaults');
        break;
    }
  }

  handleRoute(route: string) {
    console.log(route);

    switch (route) {
      case '/defaults':
        console.log('here');
        this.selectSideNavOption(MainPanel.defaults);
        break;
      case '/about':
        this.selectSideNavOption(MainPanel.about);
        break;
    }
  }

  openDialog(): void {
    this.dialog
      .open(ModalComponent)
      .afterClosed()
      .subscribe((results?: OperationSet) => {
        if (results) {
          this.operationSet = results;
          this.mainPanelContent = MainPanel.result;
        }
      });
  }
}
