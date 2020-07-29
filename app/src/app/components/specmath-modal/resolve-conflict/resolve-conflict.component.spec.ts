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

import { TestBed, async, ComponentFixture } from '@angular/core/testing';
import { ResolveConflictComponent } from './resolve-conflict.component';

import { BrowserModule } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { MatExpansionModule } from '@angular/material/expansion';
import { queryElement } from '../../../../shared/functions';

describe('ResolveConflictComponent', () => {
  let fixture: ComponentFixture<ResolveConflictComponent>;
  let component: ResolveConflictComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ResolveConflictComponent
      ],
      imports: [
        MatIconModule,
        BrowserModule,
        MatRadioModule,
        MatExpansionModule
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(ResolveConflictComponent);
      component = fixture.componentInstance;
    });
  }));

  it('creates the ResolveConflictComponent', () => {
    expect(component).toBeTruthy();
  });
});
