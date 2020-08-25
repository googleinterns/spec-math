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

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EmptyPageComponent } from './components/empty-page/empty-page.component';
import { DisplayResultsComponent } from './components/spec-math-display-results/display-results.component';
import { AboutPageComponent } from './components/about-page/about-page.component';
import { DefaultsPageComponent } from './components/defaults-page/defaults-page.component';

const routes: Routes = [
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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
