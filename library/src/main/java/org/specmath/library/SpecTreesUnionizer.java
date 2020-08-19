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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/** Provides functions for performing union operations on spec trees represented as Maps. */
public class SpecTreesUnionizer {
  /**
   * Performs a union on {@code mapToMergeInto} and {@code mapToMerge} and returns the result.
   *
   * @param mapToMergeInto the map which {@code mapToMerge} is merged into. {@code mapToMergeInto}
   *     will contain the result of the union.
   * @param mapToMerge the map to merge into {@code mapToMergeInto}
   * @return the result of a union on {@code mapToMergeInto} and {@code mapToMerge}
   */
  static LinkedHashMap<String, Object> union(
      LinkedHashMap<String, Object> mapToMergeInto, LinkedHashMap<String, Object> mapToMerge)
      throws UnionConflictException, UnexpectedTypeException {
    UnionizerUnionParams unionizerUnionParams = UnionizerUnionParams.builder().build();

    return union(mapToMergeInto, mapToMerge, unionizerUnionParams);
  }

  /**
   * Performs a union on {@code mapToMergeInto} and {@code mapToMerge} with the given {@code
   * unionizerUnionParams} and returns the result.
   *
   * @param mapToMergeInto the map which {@code mapToMerge} is merged into. {@code mapToMergeInto}
   *     will contain the result of the union.
   * @param mapToMerge the map to merge into {@code mapToMergeInto}
   * @param unionizerUnionParams a set of special options which can be applied during the union
   * @return the result of a union on {@code mapToMergeInto} and {@code mapToMerge} with options
   *     from {@code unionizerUnionParams} applied
   */
  static LinkedHashMap<String, Object> union(
      LinkedHashMap<String, Object> mapToMergeInto,
      LinkedHashMap<String, Object> mapToMerge,
      UnionizerUnionParams unionizerUnionParams)
      throws UnionConflictException, UnexpectedTypeException {
    var conflicts = new ArrayList<Conflict>();
    LinkedHashMap<String, Object> mergedMap =
        union(
            mapToMergeInto,
            mapToMerge,
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
    }

    throw new UnionConflictException(conflicts);
  }

  /**
   * Performs a union on the list of maps in {@code mapsToMerge} and returns the result.
   *
   * @param mapsToMerge the list of maps to merge
   * @return the result of a union on the list of maps in {@code mapsToMerge}
   */
  static LinkedHashMap<String, Object> union(List<LinkedHashMap<String, Object>> mapsToMerge)
      throws UnionConflictException, UnexpectedTypeException {
    UnionizerUnionParams unionizerUnionParams = UnionizerUnionParams.builder().build();

    return union(mapsToMerge, unionizerUnionParams);
  }

  /**
   * Performs a union on on the list of maps in {@code mapsToMerge} with the given {@code
   * unionizerUnionParams} and returns the result.
   *
   * @param mapsToMerge the list of maps to merge
   * @param unionizerUnionParams a set of special options which can be applied during the union
   * @return the result of a union on the list of maps in {@code mapsToMerge} with options from
   *     {@code unionizerUnionParams} applied
   */
  static LinkedHashMap<String, Object> union(
      List<LinkedHashMap<String, Object>> mapsToMerge, UnionizerUnionParams unionizerUnionParams)
      throws UnionConflictException, UnexpectedTypeException {

    var allConflicts = new ArrayList<Conflict>();

    // merge all the specs in mapsToMerge together
    LinkedHashMap<String, Object> mergedSpec = mapsToMerge.get(0);
    for (int i = 1; i < mapsToMerge.size(); i++) {
      mergedSpec =
          union(
              mergedSpec,
              mapsToMerge.get(i),
              false,
              new Stack<String>(),
              allConflicts,
              unionizerUnionParams.conflictResolutions());
    }

    removeConflictsFixedByDefaults(unionizerUnionParams.defaults(), allConflicts);

    LinkedHashMap<String, Object> defaultsMap =
        new LinkedHashMap<>(unionizerUnionParams.defaults());

    LinkedHashMap<String, Object> resolvedMap = applyOverlay(defaultsMap, mergedSpec);

    if (allConflicts.isEmpty()) {
      return resolvedMap;
    }

    throw new UnionConflictException(allConflicts);
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
   * Performs a union on {@code defaults} and {@code mapToOverlay} and returns the result. Conflicts
   * are resolved by using the {@code defaults} map as priority.
   *
   * @param defaults the map which {@code mapToOverlay} is merged into. {@code defaults} will
   *     contain the result of the union and will take priority over {@code mapToOverlay} in the
   *     case of conflicts.
   * @param mapToOverlay the map to merge into {@code defaults}
   * @return the result of a union on {@code defaults} and {@code mapToOverlay}, where conflicts are
   *     resolved by using the {@code defaults} map as priority.
   */
  static LinkedHashMap<String, Object> applyOverlay(
      LinkedHashMap<String, Object> defaults, LinkedHashMap<String, Object> mapToOverlay)
      throws UnexpectedTypeException {
    return union(
        defaults,
        mapToOverlay,
        true,
        new Stack<String>(),
        new ArrayList<Conflict>(),
        new HashMap<String, Object>());
  }

  /**
   * Union function with all possible options. Other functions provide a nicer interface for
   * different use cases of union, and ultimately call this function.
   *
   * @param mapToMergeInto map 1/2 to merge. This map is special because it can take priority in the
   *     union based on {@code mapToMergeIntoIsDefault} flag
   * @param mapToMerge map 2/2 to merge
   * @param mapToMergeIntoIsDefault if true, mapToMergeInto will take priority over mapToMerge in
   *     case of different leaf values, and no conflict will be reported
   * @param keypath the key path which the leaf nodes belong to in the current iteration of the
   *     recursive frame
   * @param conflicts appended to if there is an unresolvable conflict
   * @param conflictResolutions a map which can provide conflict resolutions based on keypaths
   * @return the result of union on {@code mapToMergeInto} and {@code mapToMerge}
   */
  private static LinkedHashMap<String, Object> union(
      LinkedHashMap<String, Object> mapToMergeInto,
      LinkedHashMap<String, Object> mapToMerge,
      boolean mapToMergeIntoIsDefault,
      Stack<String> keypath,
      ArrayList<Conflict> conflicts,
      HashMap<String, Object> conflictResolutions)
      throws UnexpectedTypeException {

    for (Map.Entry<String, Object> entry : mapToMerge.entrySet()) {
      String keyOfMapToMerge = entry.getKey();
      Object valueOfMapToMerge = entry.getValue();

      keypath.push(keyOfMapToMerge);

      if (mapToMergeInto.containsKey(keyOfMapToMerge)) {
        processSubtreesWithSameKey(
            mapToMergeInto,
            mapToMergeIntoIsDefault,
            keypath,
            conflicts,
            conflictResolutions,
            keyOfMapToMerge,
            valueOfMapToMerge);

      } else {
        // mapToMerge contains a key which mapToMergeInto does not have, so just add the entire
        // subtree / leaf node.
        mapToMergeInto.put(keyOfMapToMerge, valueOfMapToMerge);
      }

      // Backtrack on the keypath.
      keypath.pop();
    }

    return mapToMergeInto;
  }

  private static void processSubtreesWithSameKey(
      LinkedHashMap<String, Object> mapToMergeInto,
      boolean mapToMergeIntoIsDefault,
      Stack<String> keypath,
      ArrayList<Conflict> conflicts,
      HashMap<String, Object> conflictResolutions,
      String key,
      Object valueOfMapToMerge)
      throws UnexpectedTypeException {
    Object value1 = mapToMergeInto.get(key);

    if (value1.equals(valueOfMapToMerge)) {
      return;
    }

    if (TypeChecker.isObjectMap(value1) && TypeChecker.isObjectMap((valueOfMapToMerge))) {
      // We have two maps which contain the same key, add the key and process maps further.
      processUnequalSubtrees(
          mapToMergeInto,
          mapToMergeIntoIsDefault,
          keypath,
          conflicts,
          conflictResolutions,
          key,
          valueOfMapToMerge,
          value1);
    } else if (TypeChecker.isObjectList(value1) && TypeChecker.isObjectList(valueOfMapToMerge)) {
      processUnequalListNodes(
          mapToMergeInto, mapToMergeIntoIsDefault, key, valueOfMapToMerge, value1);
    } else if (TypeChecker.isObjectPrimitive(value1)
        && TypeChecker.isObjectPrimitive(valueOfMapToMerge)) {
      processUnequalLeafNodes(
          mapToMergeInto,
          key,
          value1,
          valueOfMapToMerge,
          mapToMergeIntoIsDefault,
          keypath,
          conflicts,
          conflictResolutions);
    } else {
      // Either an unexpected type was met, or one map had a different type (primitive, map,
      // list) as a value compared to the other.
      throw new UnexpectedTypeException("Unexpected Data During Union");
    }
  }

  private static void processUnequalListNodes(
      LinkedHashMap<String, Object> mapToMergeInto,
      boolean mapToMergeIntoIsDefault,
      String key,
      Object valueOfMapToMerge,
      Object value1) {
    if (!mapToMergeIntoIsDefault) {
      List<Object> output =
          ListUtils.listUnion(
              ObjectCaster.castObjectToListOfObjects(value1),
              ObjectCaster.castObjectToListOfObjects(valueOfMapToMerge));
      mapToMergeInto.put(key, output);
    }
  }

  /**
   * Used by the union function when two subtrees are different. We have two maps which contain the
   * same key, add the key and process maps further.
   *
   * @param mapToMergeInto the output map
   * @param keypath the keypath to the current node
   * @param key the key shared by the two subtrees
   * @param valueMapToMerge subtree 2, to be processed further
   * @param valueMapToMergeInto subtree 1, to be processed further
   */
  private static void processUnequalSubtrees(
      LinkedHashMap<String, Object> mapToMergeInto,
      boolean map1IsDefault,
      Stack<String> keypath,
      ArrayList<Conflict> conflicts,
      HashMap<String, Object> conflictResolutions,
      String key,
      Object valueMapToMerge,
      Object valueMapToMergeInto)
      throws UnexpectedTypeException {
    LinkedHashMap<String, Object> value1Map =
        ObjectCaster.castObjectToStringObjectMap(valueMapToMergeInto);
    LinkedHashMap<String, Object> value2Map =
        ObjectCaster.castObjectToStringObjectMap(valueMapToMerge);
    mapToMergeInto.put(
        key, union(value1Map, value2Map, map1IsDefault, keypath, conflicts, conflictResolutions));
  }

  /**
   * Used by the union function when two leaf nodes are different. If {@code map1IsDefault} is true,
   * then there is no conflict. Otherwise, there is a conflict if there is no {@code key} in {@code
   * conflictResolutions} that matches the {@code keypath} of the current nodes. In the case of a
   * conflict, add it to the {@code conflictResolutions} array.
   *
   * @param mapToMergeInto the output map, which may be added to
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
      LinkedHashMap<String, Object> mapToMergeInto,
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
        mapToMergeInto.put(key, conflictResolutions.get(keypathString));
      } else {
        handleConflict(value1, value2, conflicts, keypathString);
      }
    }
  }

  private static void handleConflict(Object value1, Object value2, ArrayList<Conflict> conflicts,
      String keypathString) {
    // if it exists, attempt to add the conflicting values to an existing conflict object
    Conflict conflictWithSameKeypath =
        conflicts.stream()
            .filter(conflict -> conflict.getKeypath().equals(keypathString))
            .findAny()
            .orElse(null);

    if (conflictWithSameKeypath != null) {
      if (!conflictWithSameKeypath.getOptions().contains(value1)) {
        conflictWithSameKeypath.addOption(value1);
      }
      if (!conflictWithSameKeypath.getOptions().contains(value2)) {
        conflictWithSameKeypath.addOption(value2);
      }
      return;
    }

    // there was no already existing conflict object with the keypath. Make a new one
    List<Object> options = new ArrayList<>();
    options.add(value1);
    options.add(value2);
    Conflict conflict = new Conflict(keypathString, options);
    conflicts.add(conflict);
  }
}
