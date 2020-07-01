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
   * Adds all root to leaf node keypaths in {@code map} to {@code keypaths} HashSet
   *
   * @param map the map which we will find all the keypaths for
   * @param keypath the path of keys taken to get to a leaf node in the map
   * @param keypaths a HashSet of Strings which this function will add all the keypaths of leaf
   *     nodes to during the traversal
   */
  @SuppressWarnings("unchecked")
  public static void getKeypathsFromMap(
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
