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

import java.util.List;
import java.util.Objects;

/**
 * POJO which is used to represent a org.specmath.library.Conflict resulting from a union operation.
 */
public class Conflict {
  private String keypath;
  private List<Object> options;
  private Object resolvedValue;

  /**
   * The default constructor is needed for com.fasterxml.jackson.databind.ObjectMapper which is used
   * in the {@code org.specmath.library.ConflictStringToConflictMapConverter} class
   */
  public Conflict() {}

  /**
   * Create this org.specmath.library.Conflict object with a conflicting keypath, and the options
   * for resolving the conflicts.
   */
  public Conflict(String keypath, List<Object> options) {
    this.keypath = keypath;
    this.options = options;
  }

  /** getters are required for serialization of JSON string to conflict objects */
  public List<Object> getOptions() {
    return options;
  }

  public String getKeypath() {
    return keypath;
  }

  public Object getResolvedValue() {
    return resolvedValue;
  }

  public void addOption(Object option) {
    this.options.add(option);
  }

  @Override
  public boolean equals(Object o) {
    Conflict conflict = (Conflict) o;
    return keypath.equals(conflict.keypath)
        && options.equals(conflict.options)
        && Objects.equals(resolvedValue, conflict.resolvedValue);
  }

  @Override
  public String toString() {
    return "Conflict{"
        + "keypath='"
        + keypath
        + '\''
        + ", options="
        + options
        + ", resolvedValue="
        + resolvedValue
        + '}';
  }
}
