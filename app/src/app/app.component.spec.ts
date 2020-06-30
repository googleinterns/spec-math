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
import { AppComponent } from './app.component';

import { BrowserModule } from '@angular/platform-browser';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SpecMathNavBarModule } from './components/specmath-navbar/specmath-navbar.module';
import { By } from '@angular/platform-browser';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let app: AppComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent
      ],
      imports: [
        BrowserModule,
        BrowserAnimationsModule,
        MatSidenavModule,
        MatMenuModule,
        MatIconModule,
        MatDividerModule,
        MatButtonModule,
        SpecMathNavBarModule,
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(AppComponent);
      app = fixture.componentInstance;
    });
  }));

  it('creates the app component', () => {
    expect(app).toBeTruthy();
  });

  it('opens the operations menu when the New button is clicked', () => {
    const button = fixture.debugElement.query(By.css('#operation-menu-button-desktop')).nativeElement;
    button.click();
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('#operation-menu'))).toBeTruthy();
  });
});
