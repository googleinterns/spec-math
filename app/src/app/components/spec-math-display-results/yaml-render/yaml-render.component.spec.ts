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

import { TestBed, ComponentFixture } from '@angular/core/testing';
import { YamlRenderComponent } from './yaml-render.component';

describe('YamlRenderComponent', () => {
  let fixture: ComponentFixture<YamlRenderComponent>;
  let component: YamlRenderComponent;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      declarations: [
        YamlRenderComponent
      ]
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(YamlRenderComponent);
      component = fixture.componentInstance;
    });
  });

  it('creates the YamlRenderComponent', () => {
    expect(component).toBeTruthy();
  });
});
