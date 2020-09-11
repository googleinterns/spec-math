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

import {
  TestBed,
  async,
  ComponentFixture,
  flush,
  fakeAsync,
} from '@angular/core/testing';
import { AppComponent } from './app.component';

import { BrowserModule, By } from '@angular/platform-browser';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { queryElement } from 'src/shared/functions';
import { DisplayResultsModule } from './components/display-results/display-results.module';
import { YamlRenderModule } from './components/yaml-render/yaml-render.module';
import { MatListModule } from '@angular/material/list';
import { DefaultsPageComponent } from './components/defaults-page/defaults-page.component';
import { AboutPageComponent } from './components/about-page/about-page.component';
import { Router } from '@angular/router';
import { DisplayResultsComponent } from './components/display-results/display-results.component';
import { EmptyPageComponent } from './components/empty-page/empty-page.component';
import { MatExpansionModule } from '@angular/material/expansion';
import { OperationService } from 'src/shared/services/operation.service';
import { SideNavService } from 'src/shared/services/sidenav.service';
import { RouterTestingModule } from '@angular/router/testing';
import { MergeModalModule } from './components/modals/merge-modal/merge-modal.module';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let app: AppComponent;
  let router: Router;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        EmptyPageComponent,
        AboutPageComponent,
        DefaultsPageComponent,
      ],
      imports: [
        BrowserModule,
        BrowserAnimationsModule,
        MatSidenavModule,
        MatMenuModule,
        MatIconModule,
        MatDividerModule,
        MatButtonModule,
        MergeModalModule,
        DisplayResultsModule,
        YamlRenderModule,
        MatListModule,
        MatExpansionModule,
        RouterTestingModule.withRoutes([
          {
            path: 'about',
            component: AboutPageComponent,
          },
          {
            path: 'defaults',
            component: DefaultsPageComponent,
          },
          {
            path: 'results',
            component: DisplayResultsComponent,
          },
          {
            path: '**',
            component: EmptyPageComponent,
          },
        ]),
      ],
      providers: [OperationService, SideNavService],
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(AppComponent);
        router = TestBed.inject(Router);
        app = fixture.componentInstance;
      });
  }));

  it('creates the app component', () => {
    expect(app).toBeTruthy();
  });

  it('opens the operations menu when the New button is clicked', () => {
    const button = queryElement(fixture, '#operation-menu-button-desktop')
      .nativeElement;
    button.click();

    fixture.detectChanges();
    const menu = queryElement(fixture, '#operation-menu');
    expect(menu).toBeTruthy();
  });

  it('opens the About Default files page when its button is clicked', fakeAsync(() => {
    const aboutDefaultFilesButton = queryElement(
      fixture,
      '#about-default-files-button'
    ).nativeElement;

    aboutDefaultFilesButton.click();

    fixture.detectChanges();
    flush();

    expect(router.url).toEqual('/defaults');

    fixture.detectChanges();
    const defaultsPage = fixture.debugElement.query(
      By.directive(DefaultsPageComponent)
    );
    expect(defaultsPage).toBeTruthy();
  }));

  it('opens the About page when its button is clicked', fakeAsync(() => {
    const aboutButton = queryElement(fixture, '#about-spec-math-button')
      .nativeElement;

    aboutButton.click();

    fixture.detectChanges();
    flush();

    expect(router.url).toEqual('/about');

    fixture.detectChanges();
    const aboutPage = fixture.debugElement.query(
      By.directive(AboutPageComponent)
    );
    expect(aboutPage).toBeTruthy();
  }));

  it('opens the Empty page when the logo is clicked', fakeAsync(() => {
    const logo = queryElement(fixture, '#spec-math-logo').nativeElement;

    logo.click();

    fixture.detectChanges();
    flush();

    expect(router.url).toEqual('/');

    fixture.detectChanges();
    const emptyPage = fixture.debugElement.query(
      By.directive(EmptyPageComponent)
    );
    expect(emptyPage).toBeTruthy();
  }));
});
