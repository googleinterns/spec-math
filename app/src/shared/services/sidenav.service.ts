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

import { Injectable } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';

@Injectable()
export class SideNavService {
  private sideNav: MatSidenav;

  /**
   * Sets the local SideNav object to be used throughout the app
   *
   * @param sideNav - a SideNav object
   */
  setSideNav(sideNav: MatSidenav) {
    this.sideNav = sideNav;
  }

  /**
   * Toggles the sideNav
   *
   */
  toggle() {
    this.sideNav.toggle();
  }

  /**
   * Returns the state of the SideNav for interaction purposes
   *
   * @returns a boolean representing the state of the SideNav
   */
  isOpened(): boolean {
    return this.sideNav.opened;
  }
}
