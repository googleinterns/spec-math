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

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Provides casting for {@code Object} into other data structures to be used only during Spec Tree
 * traversal.
 */
public class ObjectCaster {
  /**
   * Cast an object into a LinkedHashMap<String, Object>.
   *
   * <p>SuppressWarnings was used here and in a few other places in the library. When deserializing
   * the YAML file within the library (in the org.specmath.library.YamlStringToSpecTreeConverter
   * class), it becomes a LinkedHashMap<String, Object> which is a Map<String, Object>. The value of
   * map could be another Map, or other stuff i.e. List, String, Integer, Boolean. This piece of the
   * code assumes that the provided Map fits this criteria, namely that if the value is a Map, then
   * it will always be a Map<String, Object>. Usage of this function is internal in the library
   * where we can guarantee that the map parameter provided is Map<String, Object> where if the
   * Object value is a Map, and it passes the (value instanceof Map) condition, then it must be some
   * Map<String, Object>.
   *
   * @param stringObjectMap must be an instanceof LinkedHashMap<String, Object>
   * @return the {@code stringObjectMap} cast into a LinkedHashMap<String, Object>
   */
  @SuppressWarnings("unchecked")
  static LinkedHashMap<String, Object> castObjectToStringObjectMap(Object stringObjectMap) {
    return (LinkedHashMap<String, Object>) stringObjectMap;
  }

  /**
   * Cast an object into a List<Object>.
   *
   * <p>SuppressWarnings was used here and in a few other places in the library. When deserializing
   * the YAML file within the library (in the org.specmath.library.YamlStringToSpecTreeConverter
   * class), it becomes a LinkedHashMap<String, Object> which is a Map<String, Object>. The value of
   * map could be another Map, or other stuff i.e. List<Object>, String, Integer, Boolean. This
   * piece of the code assumes that the provided List fits this criteria, namely that if the value
   * is a List, then it will always be a List<Object>. Usage of this function is internal in the
   * library where we can guarantee that the listOfObjects parameter provided is List<Object>.
   *
   * @param listOfObjects must be an instanceof List<Object>
   * @return the {@code listOfObjects} cast into a List<Object>
   */
  @SuppressWarnings("unchecked")
  static List<Object> castObjectToListOfObjects(Object listOfObjects) {
    return (List<Object>) listOfObjects;
  }
}
