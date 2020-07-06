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

import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

/** Provides static functions for some operations on Maps */
public class MapUtils {

  /**
   * Adds all root to leaf node keypaths in {@code map} to {@code keypaths} HashSet.
   *
   * @param map the map which we will find all the keypaths for
   * @param keypath the path of keys taken to get to a leaf node in the map
   * @param keypaths a HashSet of Strings which this function will add all the keypaths of leaf
   *     nodes to during the traversal
   */
  // SuppressWarnings was used here and in a few other places in the library. When deserializing the
  // YAML file within the library (in the YamlStringToSpecTreeConverter class), it becomes a
  // LinkedHashMap<String, Object> which is a Map<String, Object>. The value of map could be another
  // Map, or other stuff i.e. List, String, Integer, Boolean. This piece of recursive code assumes
  // that the provided Map fits this criteria, namely that if the value is a Map, then it will
  // always be a Map<String, Object>. Usage of this function is internal in the library where we can
  // guarantee that the map parameter provided is Map<String, Object> where if the Object value is a
  // Map, and it passes the (value instanceof Map) condition, then it must be some Map<String,
  // Object>.
  @SuppressWarnings("unchecked")
  static void getKeypathsFromMap(
      Map<String, Object> map, Stack<String> keypath, HashSet<String> keypaths) {
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      keypath.push(key);
      if (value instanceof Map) {
        Map<String, Object> nmap = (Map<String, Object>) value;
        getKeypathsFromMap(nmap, keypath, keypaths);
      } else {
        // this is a leaf node, so add the keypath and then we are done
        keypaths.add(keypath.toString());
      }

      keypath.pop(); // backtrack
    }
  }
}
