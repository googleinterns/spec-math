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

import java.util.Objects;

/** POJO which is used to represent a Conflict resulting from a union operation. */
public class Conflict {
  private String keypath;
  private Object optionA;
  private Object optionB;
  private Object resolvedValue;

  /**
   * The default constructor is needed for com.fasterxml.jackson.databind.ObjectMapper which is used
   * in the {@code ConflictStringToConflictMapConverter} class
   */
  public Conflict() {}

  /**
   * Create this Conflict object with a conflicting keypath, and the two options for resolving the
   * conflicts.
   */
  public Conflict(String keypath, Object optionA, Object optionB) {
    this.keypath = keypath;
    this.optionA = optionA;
    this.optionB = optionB;
  }

  /** getters are required for serialization of JSON string to conflict objects */
  public Object getOptionA() {
    return optionA;
  }

  public Object getOptionB() {
    return optionB;
  }

  public String getKeypath() {
    return keypath;
  }

  public Object getResolvedValue() {
    return resolvedValue;
  }

  @Override
  public boolean equals(Object o) {
    Conflict conflict = (Conflict) o;
    return keypath.equals(conflict.keypath)
        && optionA.equals(conflict.optionA)
        && optionB.equals(conflict.optionB)
        && Objects.equals(resolvedValue, conflict.resolvedValue);
  }

  @Override
  public String toString() {
    return "Conflict{"
        + "keypath='"
        + keypath
        + '\''
        + ", optionA='"
        + optionA
        + '\''
        + ", optionB='"
        + optionB
        + '\''
        + ", resolvedValue='"
        + resolvedValue
        + '\''
        + '}';
  }
}
