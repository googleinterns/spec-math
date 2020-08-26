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

import { BrowserModule } from '@angular/platform-browser';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { ModalModule } from './components/specmath-modal/modal.module';
import { DisplayResultsModule } from './components/spec-math-display-results/display-results.module';
import { YamlRenderModule } from './components/yaml-render/yaml-render.module';
import { MatListModule } from '@angular/material/list';
import { MatExpansionModule } from '@angular/material/expansion';
import { OperationService } from 'src/shared/services/operation.service';
import { RouterModule, Routes } from '@angular/router';
import { AboutPageComponent } from './components/about-page/about-page.component';
import { DefaultsPageComponent } from './components/defaults-page/defaults-page.component';
import { DisplayResultsComponent } from './components/spec-math-display-results/display-results.component';
import { EmptyPageComponent } from './components/empty-page/empty-page.component';
import { SideNavService } from 'src/shared/services/sidenav.service';
import { routes } from 'src/shared/routes';

@NgModule({
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
    ModalModule,
    DisplayResultsModule,
    YamlRenderModule,
    MatListModule,
    MatExpansionModule,
    RouterModule.forRoot([
      {
        path: routes.aboutSpecMath,
        component: AboutPageComponent,
      },
      {
        path: routes.aboutDefaults,
        component: DefaultsPageComponent,
      },
      {
        path: routes.results,
        component: DisplayResultsComponent,
      },
      {
        path: '**',
        component: EmptyPageComponent,
      },
    ]),
  ],
  providers: [OperationService, SideNavService],
  bootstrap: [AppComponent],
})
export class AppModule {}
