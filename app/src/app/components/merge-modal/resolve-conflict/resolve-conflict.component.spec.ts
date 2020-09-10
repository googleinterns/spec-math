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

import { BrowserModule, By } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';
import { MatRadioModule } from '@angular/material/radio';
import { MatExpansionModule } from '@angular/material/expansion';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
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
        MatExpansionModule,
        BrowserAnimationsModule
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(ResolveConflictComponent);
      component = fixture.componentInstance;
    });
  }));

  it('creates the ResolveConflictComponent', () => {
    expect(component).toBeTruthy();
  });

  it('renders a list of conflicts when conflicts are passed into the component', () => {
    component.mergeConflicts = [
      {
        keypath: '/path1',
        options: ['Option A', 'Option B'],
      },
      {
        keypath: '/path2',
        options: ['Option A', 'Option B'],
      },
      {
        keypath: '/path3',
        options: ['Option A', 'Option B'],
      },
    ];

    fixture.detectChanges();
    const conflictsContainer = queryElement(fixture, '.conflicts-container').nativeElement;
    expect(conflictsContainer).toBeTruthy();
  });

  it('does not render any conflicts when no conflicts are passed in', () => {
    expect(fixture.debugElement.query(By.css('.conflicts-container'))).toBeNull();
  });

  it('emits a solved conflict when a conflict radio button is selected', () => {
    const spy = spyOn(component, 'emitResolvedValue');
    component.mergeConflicts = [
      {
        keypath: '/path1',
        options: ['Option A', 'Option B'],
      },
      {
        keypath: '/path2',
        options: ['Option A', 'Option B'],
      },
      {
        keypath: '/path3',
        options: ['Option A', 'Option B'],
      },
    ];

    fixture.detectChanges();

    component.mergeConflicts.forEach((conflict, index) => {
      const conflictOptionGroup = queryElement(fixture, `#conflict-${index}-radio-group`);

      fixture.detectChanges();
      conflictOptionGroup.triggerEventHandler('change', { value: conflict.options[0] });

      fixture.detectChanges();
      expect(spy).toHaveBeenCalledWith(conflict.options[0], index);
    });
  });
});
