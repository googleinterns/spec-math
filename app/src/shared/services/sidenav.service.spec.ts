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

import { TestBed, fakeAsync, flush, ComponentFixture } from '@angular/core/testing';
import { SideNavService } from './sidenav.service';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { Component, NgModule, ViewChild } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('SideNavService', () => {
  let service: SideNavService;
  let mockComponent: MatSideNavStubComponent;
  let fixture: ComponentFixture<MatSideNavStubComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SideNavService],
      imports: [MatSidenavModule, BrowserAnimationsModule],
    });
    service = TestBed.inject(SideNavService);
    fixture = TestBed.createComponent(MatSideNavStubComponent);
    mockComponent = fixture.componentInstance;
  });

  it('is instantiated', () => {
    expect(service).toBeTruthy();
  });

  it('sets a SideNav object when setSideNav is used', () => {
    fixture.detectChanges();
    service.setSideNav(mockComponent.sideNav);

    const opened = service.isOpened();
    expect(opened).toEqual(false);
  });

  it('toggles the sideNav when toggle is called', () => {
    fixture.detectChanges();
    service.setSideNav(mockComponent.sideNav);

    const opened = service.isOpened();
    expect(opened).toEqual(false);

    service.toggle();
    const toggled = service.isOpened();
    expect(toggled).toEqual(true);
  });
});

/**
 * Stub component for the MatSideNav
 */
@Component({
  selector: 'app-spec-name-input',
  template: '<div><mat-sidenav #sidenav></mat-sidenav></div>',
})
export class MatSideNavStubComponent {
  @ViewChild('sidenav') sideNav: MatSidenav;
}
@NgModule({
  declarations: [MatSideNavStubComponent],
  imports: [MatSidenavModule, BrowserAnimationsModule],
})
export class MatSideNavStubModule {}
