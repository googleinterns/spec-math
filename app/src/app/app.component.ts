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

import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MergeModalComponent } from './components/modals/merge-modal/merge-modal.component';
import { OperationSet, OperationType } from 'src/shared/interfaces';
import { MediaMatcher } from '@angular/cdk/layout';
import { Router } from '@angular/router';
import { OperationService } from 'src/shared/services/operation.service';
import { MatSidenav } from '@angular/material/sidenav';
import { SideNavService } from 'src/shared/services/sidenav.service';
import { routes } from 'src/shared/routes';
import { OverlayModalComponent } from './components/modals/overlay-modal/overlay-modal.component';
import { SpecMathModal } from './components/modals/spec-math-modal';

type OperationModal = {
  [key in OperationType]: typeof SpecMathModal;
};

const MOBILE_MEDIA_QUERY = '(max-width: 768px)';
const modalOperation: OperationModal = {
  merge: MergeModalComponent,
  overlay: OverlayModalComponent,
};

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements AfterViewInit {
  @ViewChild('snav') sideNav: MatSidenav;
  mobileQuery: MediaQueryList;
  localRoutes = routes;

  constructor(
    readonly router: Router,
    readonly dialog: MatDialog,
    readonly operationService: OperationService,
    readonly sideNavService: SideNavService,
    readonly media: MediaMatcher
  ) {
    this.mobileQuery = media.matchMedia(MOBILE_MEDIA_QUERY);
  }

  openModal(operation: OperationType) {
    const targetModal = modalOperation[operation];

    this.dialog
      .open(targetModal)
      .afterClosed()
      .subscribe((results?: OperationSet) => {
        if (results) {
          this.operationService.setResults(results);
          this.router.navigateByUrl(this.localRoutes.results);
        }
      });
  }

  ngAfterViewInit() {
    this.sideNavService.setSideNav(this.sideNav);
  }
}
