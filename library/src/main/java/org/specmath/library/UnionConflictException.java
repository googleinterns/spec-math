/*
Copyright 2020 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.specmath.library;

import java.util.ArrayList;

/**
 * An exception thrown when a union operation resulted in one more conflicts, represented as {@code
 * org.specmath.library.Conflict} objects
 */
public class UnionConflictException extends Exception {
  private ArrayList<Conflict> conflicts;

  /**
   * Create this exception, initialized with an array of {@code org.specmath.library.Conflict}
   * objects.
   */
  public UnionConflictException(ArrayList<Conflict> conflicts) {
    this.conflicts = conflicts;
  }

  /**
   * @return the array of {@code org.specmath.library.Conflict} objects contained within this
   *     exception
   */
  public ArrayList<Conflict> getConflicts() {
    return conflicts;
  }
}
