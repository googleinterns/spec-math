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
import { DisplayResultsComponent } from './display-results.component';
import { MatTabsModule } from '@angular/material/tabs';
import { BrowserModule } from '@angular/platform-browser';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatDividerModule } from '@angular/material/divider';
import { YamlRenderModule } from '../yaml-render/yaml-render.module';
import { OperationService } from 'src/shared/services/operation.service';

@NgModule({
  declarations: [
    DisplayResultsComponent,
  ],
  imports: [
    MatTabsModule,
    BrowserModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatDividerModule,
    YamlRenderModule
  ],
  providers: [OperationService],
  exports: [DisplayResultsComponent],
})
export class DisplayResultsModule { }
