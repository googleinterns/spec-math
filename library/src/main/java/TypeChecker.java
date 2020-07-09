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

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Provides functions for checking common types used throughout the library.
 */
public class TypeChecker {

  /**
   * Returns true if the Object {@code value} is a LinkedHashMap, false otherwise.
   */
  static boolean isObjectMap(Object value) {
    return value instanceof LinkedHashMap;
  }

  /**
   * Returns true if the Object {@code value} is a List, false otherwise.
   */
  static boolean isObjectList(Object value){
    return value instanceof List;
  }

  /**
   * Returns true if the Object {@code value} is a primitive object, false otherwise.
   */
  static boolean isObjectPrimitive(Object value) {
    return value instanceof String
        || value instanceof Boolean
        || value instanceof Number
        || value instanceof Character;
  }
}
