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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Provides functions for performing union operations on spec trees represented as Maps.
 */
public class SpecTreesUnionizer {
  /**
   * Performs a union on {@code map1} and {@code map2} and returns the result.
   *
   * @param map1 the map which {@code map2} is merged into. {@code map1} will contain the result of
   *     the union.
   * @param map2 the map to merge into {@code map1}
   * @return the result of a union on {@code map1} and {@code map2}
   */
  static LinkedHashMap<String, Object> union(
      LinkedHashMap<String, Object> map1, LinkedHashMap<String, Object> map2)
      throws UnionConflictException, UnexpectedDataException {
    UnionizerUnionParams unionizerUnionParams = UnionizerUnionParams.builder().build();

    return union(map1, map2, unionizerUnionParams);
  }

  /**
   * Performs a union on {@code map1} and {@code map2} with the given {@code unionizerUnionParams}
   * and returns the result.
   *
   * @param map1 the map which {@code map2} is merged into. {@code map1} will contain the result of
   *     the union.
   * @param map2 the map to merge into {@code map1}
   * @param unionizerUnionParams a set of special options which can be applied during the union
   * @return the result of a union on {@code map1} and {@code map2} with options from {@code
   *     unionizerUnionParams} applied
   */
  static LinkedHashMap<String, Object> union(
      LinkedHashMap<String, Object> map1,
      LinkedHashMap<String, Object> map2,
      UnionizerUnionParams unionizerUnionParams)
      throws UnionConflictException, UnexpectedDataException {
    var conflicts = new ArrayList<Conflict>();
    LinkedHashMap<String, Object> mergedMap =
        union(
            map1,
            map2,
            false,
            new Stack<String>(),
            conflicts,
            unionizerUnionParams.conflictResolutions());

    removeConflictsFixedByDefaults(unionizerUnionParams.defaults(), conflicts);

    LinkedHashMap<String, Object> defaultsMap =
        new LinkedHashMap<>(unionizerUnionParams.defaults());

    LinkedHashMap<String, Object> resolvedMap = applyOverlay(defaultsMap, mergedMap);

    if (conflicts.isEmpty()) {
      return resolvedMap;
    } else {
      throw new UnionConflictException(conflicts);
    }
  }

  /**
   * Removes conflicts from {@code conflicts} parameter passed by reference if the conflicting
   * keypath is also a path in the defaults map.
   *
   * @param defaults a map which contains defaults to apply to some output of a union
   * @param conflicts an ArrayList passed by reference, which gets updated based on the conflicts
   *     which do not occur thanks to the default map
   */
  private static void removeConflictsFixedByDefaults(
      LinkedHashMap<String, Object> defaults, ArrayList<Conflict> conflicts) {

    var defaultKeypaths = new HashSet<String>();

    MapUtils.getKeypathsFromMap(defaults, new Stack<>(), defaultKeypaths);

    conflicts.removeIf(conflict -> defaultKeypaths.contains(conflict.getKeypath()));
  }

  /**
   * Performs a union on {@code defaults} and {@code map2} and returns the result. Conflicts are
   * resolved by using the {@code defaults} map as priority.
   *
   * @param defaults the map which {@code map2} is merged into. {@code defaults} will contain the
   *     result of the union and will take priority over {@code map2} in the case of conflicts.
   * @param map2 the map to merge into {@code defaults}
   * @return the result of a union on {@code defaults} and {@code map2}, where conflicts are
   *     resolved by using the {@code defaults} map as priority.
   */
  static LinkedHashMap<String, Object> applyOverlay(
      LinkedHashMap<String, Object> defaults, LinkedHashMap<String, Object> map2)
      throws UnexpectedDataException {
    return union(
        defaults,
        map2,
        true,
        new Stack<String>(),
        new ArrayList<Conflict>(),
        new HashMap<String, Object>());
  }

  /**
   * Union function with all possible options. Other functions provide a nicer interface for
   * different use cases of union, and ultimately call this function.
   *
   * @param map1 map 1/2 to merge. This map is special because it can take priority in the union
   *     based on {@code map1IsDefault} flag
   * @param map2 map 2/2 to merge
   * @param map1IsDefault if true, map1 will take priority over map2 in case of different leaf
   *     values, and no conflict will be reported
   * @param keypath the key path which the leaf nodes belong to in the current iteration of the
   *     recursive frame
   * @param conflicts appended to if there is an unresolvable conflict
   * @param conflictResolutions a map which can provide conflict resolutions based on keypaths
   * @return the result of union on {@code map1} and {@code map2}
   */
  private static LinkedHashMap<String, Object> union(
      LinkedHashMap<String, Object> map1,
      LinkedHashMap<String, Object> map2,
      boolean map1IsDefault,
      Stack<String> keypath,
      ArrayList<Conflict> conflicts,
      HashMap<String, Object> conflictResolutions)
      throws UnexpectedDataException {

    for (Map.Entry<String, Object> entry : map2.entrySet()) {
      String key = entry.getKey();
      Object value2 = entry.getValue();

      keypath.push(key);

      if (map1.containsKey(entry.getKey())) {
        Object value1 = map1.get(key);

        if (!value1.equals(value2)) {
          if (TypeChecker.isObjectMap(value1) && TypeChecker.isObjectMap((value2))) {
            // We have two maps which contain the same key, add the key and process maps further.
            processUnequalSubtrees(
                map1, map1IsDefault, keypath, conflicts, conflictResolutions, key, value2, value1);
          } else if (TypeChecker.isObjectList(value1)
              && TypeChecker.isObjectList(value2)
              && !map1IsDefault) {
            List<Object> output =
                ListUtils.listUnion(
                    ObjectCaster.castObjectToListOfObjects(value1),
                    ObjectCaster.castObjectToListOfObjects(value2));
            map1.put(key, output);
          } else if (TypeChecker.isObjectPrimitive(value1)
              && TypeChecker.isObjectPrimitive(value2)) {
            processUnequalLeafNodes(
                map1, key, value1, value2, map1IsDefault, keypath, conflicts, conflictResolutions);
          } else {
            // Either an unexpected type was met, or one map had a different type (primitive, map,
            // list) as a value compared to the other.
            throw new UnexpectedDataException("Unexpected Data During Union");
          }
        }

      } else {
        // map2 contains a key which map1 does not have, so just add the entire subtree / leaf node.
        map1.put(key, value2);
      }

      // Backtrack on the keypath.
      keypath.pop();
    }

    return map1;
  }

  /**
   * Used by the union function when two subtrees are different. We have two maps which contain the
   * same key, add the key and process maps further.
   *
   * @param map1 the output map
   * @param keypath the keypath to the current node
   * @param key the key shared by the two subtrees
   * @param valueMap2 subtree 2, to be processed further
   * @param valueMap1 subtree 1, to be processed further
   */
  private static void processUnequalSubtrees(
      LinkedHashMap<String, Object> map1,
      boolean map1IsDefault,
      Stack<String> keypath,
      ArrayList<Conflict> conflicts,
      HashMap<String, Object> conflictResolutions,
      String key,
      Object valueMap2,
      Object valueMap1)
      throws UnexpectedDataException {
    LinkedHashMap<String, Object> value1Map = ObjectCaster.castObjectToStringObjectMap(valueMap1);
    LinkedHashMap<String, Object> value2Map = ObjectCaster.castObjectToStringObjectMap(valueMap2);
    map1.put(
        key, union(value1Map, value2Map, map1IsDefault, keypath, conflicts, conflictResolutions));
  }

  /**
   * Used by the union function when two leaf nodes are different. If {@code map1IsDefault} is true,
   * then there is no conflict. Otherwise, there is a conflict if there is no {@code key} in {@code
   * conflictResolutions} that matches the {@code keypath} of the current nodes. In the case of a
   * conflict, add it to the {@code conflictResolutions} array.
   *
   * @param map1 the output map, which may be added to
   * @param key the key which both leaf nodes belong to
   * @param value1 the first value to consider
   * @param value2 the second value to consider. If it is different from value2 and cannot be
   *     resolved by either defaults or conflictResolutions, then there is a conflict
   * @param map1IsDefault if true, nothing is done since {@code value1} already contains the correct
   *     value.
   * @param keypath the key path which both leaf nodes belong to
   * @param conflicts appended to if there is an unresolvable conflict
   * @param conflictResolutions a map which can provide conflict resolutions based on keypaths
   */
  private static void processUnequalLeafNodes(
      LinkedHashMap<String, Object> map1,
      String key,
      Object value1,
      Object value2,
      boolean map1IsDefault,
      Stack<String> keypath,
      ArrayList<Conflict> conflicts,
      HashMap<String, Object> conflictResolutions) {

    if (!map1IsDefault) {
      String keypathString = keypath.toString();
      if (conflictResolutions.containsKey(keypathString)) {
        // can be resolved by a conflictResolution
        map1.put(key, conflictResolutions.get(keypathString));
      } else {
        Conflict conflict = new Conflict(keypathString, value1, value2);
        conflicts.add(conflict);
      }
    }
  }
}
