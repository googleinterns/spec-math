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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Provides functions for performing operations such as union and overlay on OpenAPI specifications
 * represented as YAML strings.
 */
public class SpecMath {
  /**
   * Performs the union operation on two specs represented as strings.
   *
   * <p>This operation will attempt to combine {@code spec1} and {@code spec2} using the logic
   * provided in the SpecTreeUnionizer class. Since no special options are provided, it will attempt
   * the union and if any conflicts are found a {@code org.specmath.library.UnionConflictException}
   * will be thrown.
   *
   * @param spec1 an OpenAPI specification represented as a YAML string
   * @param spec2 an OpenAPI specification represented as a YAML string
   * @return the result of the union on spec1 and spec2, as a YAML string.
   * @throws IOException if there was a parsing issue in reading conflictResolutions
   * @throws UnionConflictException if there was a conflict in the union process, i.e. when two
   *     keypaths have the same value.
   * @throws UnexpectedTypeException an unexpected type was met during the union
   */
  public static String union(String spec1, String spec2)
      throws IOException, UnionConflictException, UnexpectedTypeException {
    UnionOptions params = UnionOptions.builder().build();

    return union(spec1, spec2, params);
  }

  /**
   * Performs the union operation on two specs represented as strings with {@code
   * org.specmath.library.UnionOptions}.
   *
   * <p>If {@code org.specmath.library.UnionOptions} are provided, then it will apply the options as
   * is appropriate based on the logic in the {@code SpecTreeUnionizer} class. If {@code
   * org.specmath.library.UnionOptions} cannot resolve the conflict then a {@code
   * org.specmath.library.UnionConflictException} will be thrown.
   *
   * @param spec1 an OpenAPI specification represented as a YAML string
   * @param spec2 an OpenAPI specification represented as a YAML string
   * @param unionOptions a set of special options which can be applied during the union
   * @return the result of the union on spec1 and spec2, as a YAML string
   * @throws IOException if there was a parsing issue in reading conflictResolutions
   * @throws UnionConflictException if there was a conflict in the union process, i.e. when two
   *     keypaths have the same value
   * @throws UnexpectedTypeException an unexpected type was met during the union
   */
  public static String union(String spec1, String spec2, UnionOptions unionOptions)
      throws IOException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> spec1map =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(spec1);
    LinkedHashMap<String, Object> spec2map =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(spec2);
    LinkedHashMap<String, Object> defaults =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(unionOptions.defaults());

    var conflictStringToConflictMapConverter = new ConflictStringToConflictMapConverter();
    HashMap<String, Object> conflictResolutionsMap =
        conflictStringToConflictMapConverter.convertConflictResolutionsStringToConflictMap(
            unionOptions.conflictResolutions());

    UnionizerUnionParams unionizerUnionParams =
        UnionizerUnionParams.builder()
            .defaults(defaults)
            .conflictResolutions(conflictResolutionsMap)
            .build();

    LinkedHashMap<String, Object> unionResultMap =
        SpecTreesUnionizer.union(spec1map, spec2map, unionizerUnionParams);

    return SpecTreeToYamlStringConverter.convertSpecTreeToYamlString(unionResultMap);
  }

  /**
   * Performs the union operation on a list of specs represented as YAML strings.
   *
   * <p>This operation will attempt to combine a list of specs represented as YAML strings using the logic
   * provided in the SpecTreeUnionizer class. Since no special options are provided, it will attempt
   * the union and if any conflicts are found a {@code org.specmath.library.UnionConflictException}
   * will be thrown.
   *
   * @param specsToMerge a list of OpenAPI specifications represented as YAML strings to merge
   * @return the result of the union on spec1 and spec2, as a YAML string.
   * @throws IOException if there was a parsing issue in reading conflictResolutions
   * @throws UnionConflictException if there was a conflict in the union process, i.e. when two
   *     keypaths have the same value.
   * @throws UnexpectedTypeException an unexpected type was met during the union
   */
  public static String union(List<String> specsToMerge)
      throws UnionConflictException, UnexpectedTypeException, IOException {
    UnionOptions params = UnionOptions.builder().build();

    return union(specsToMerge, params);
  }

  /**
   * Performs the union operation on a list of specs represented as YAML strings with {@code
   * org.specmath.library.UnionOptions}.
   *
   * <p>If {@code org.specmath.library.UnionOptions} are provided, then it will apply the options as
   * is appropriate based on the logic in the {@code SpecTreeUnionizer} class. If {@code
   * org.specmath.library.UnionOptions} cannot resolve the conflict then a {@code
   * org.specmath.library.UnionConflictException} will be thrown.
   *
   * @param specsToMerge a list of OpenAPI specifications represented as YAML strings to merge
   * @param unionOptions a set of special options which can be applied during the union
   * @return the result of the union on spec1 and spec2, as a YAML string
   * @throws IOException if there was a parsing issue in reading conflictResolutions
   * @throws UnionConflictException if there was a conflict in the union process, i.e. when two
   *     keypaths have the same value
   * @throws UnexpectedTypeException an unexpected type was met during the union
   */
  public static String union(List<String> specsToMerge, UnionOptions unionOptions)
      throws IOException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> defaults =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(unionOptions.defaults());

    var specTreesToMerge = new ArrayList<LinkedHashMap<String, Object>>();

    for (String spec: specsToMerge){
      specTreesToMerge.add(YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(spec));
    }

    var conflictStringToConflictMapConverter = new ConflictStringToConflictMapConverter();
    HashMap<String, Object> conflictResolutionsMap =
        conflictStringToConflictMapConverter.convertConflictResolutionsStringToConflictMap(
            unionOptions.conflictResolutions());

    UnionizerUnionParams unionizerUnionParams =
        UnionizerUnionParams.builder()
            .defaults(defaults)
            .conflictResolutions(conflictResolutionsMap)
            .build();

    LinkedHashMap<String, Object> unionResultMap =
        SpecTreesUnionizer.union(specTreesToMerge, unionizerUnionParams);

    return SpecTreeToYamlStringConverter.convertSpecTreeToYamlString(unionResultMap);
  }

  /**
   * Performs the filter operation on a spec represented as a string using the {@code
   * filterCriteriaList}
   *
   * @param specToFilter an OpenAPI specification represented as a YAML string
   * @param filterCriteriaList a JSON string which contains a list of {@code
   *     org.specmath.library.FilterCriteria}
   * @return the result of the filter on {@code specToFilter} using the {@code filterCriteriaList},
   *     as a YAML string
   * @throws IOException if there was a parsing issue
   * @throws UnionConflictException if there was an issue when merging the results from each
   *     org.specmath.library.FilterCriteria
   * @throws AllUnmatchedFilterException if the filtered result would have no paths
   * @throws UnexpectedTypeException if there was an issue when merging the results from each
   *     org.specmath.library.FilterCriteria
   */
  public static String filter(String specToFilter, String filterCriteriaList)
      throws IOException, UnionConflictException, AllUnmatchedFilterException,
          UnexpectedTypeException {
    FilterOptions emptyFilterOptions = FilterOptions.builder().build();

    return filter(specToFilter, filterCriteriaList, emptyFilterOptions);
  }

  /**
   * Performs the filter operation on a spec represented as a string using the {@code
   * filterCriteriaList} string and {@code filterOptions}
   *
   * @param specToFilter an OpenAPI specification represented as a YAML string
   * @param filterCriteriaList a JSON string which contains a list of {@code
   *     org.specmath.library.FilterCriteria}
   * @param filterOptions a set of special options which can be applied during the filter
   * @return the result of the filter on {@code specToFilter} using the {@code filterCriteriaList},
   *     as a YAML string
   * @throws IOException if there was a parsing issue
   * @throws UnionConflictException if there was an issue when merging the results from each
   *     org.specmath.library.FilterCriteria
   * @throws AllUnmatchedFilterException if the filtered result would have no paths
   * @throws UnexpectedTypeException if there was an issue when merging the results from each
   *     org.specmath.library.FilterCriteria
   */
  public static String filter(
      String specToFilter, String filterCriteriaList, FilterOptions filterOptions)
      throws IOException, UnionConflictException, AllUnmatchedFilterException,
          UnexpectedTypeException {
    var mapper = new ObjectMapper();
    List<FilterCriteriaJson> filterCriteria =
        mapper.readValue(filterCriteriaList, new TypeReference<List<FilterCriteriaJson>>() {});

    var listOfFilterCriteria = new ArrayList<FilterCriteria>();

    for (FilterCriteriaJson filterCriteriaJson : filterCriteria) {
      listOfFilterCriteria.add(
          FilterCriteria.builder()
              .operations(filterCriteriaJson.operations)
              .path(filterCriteriaJson.path)
              .removableTags(filterCriteriaJson.removableTags)
              .tags(filterCriteriaJson.tags)
              .build());
    }

    return filter(specToFilter, listOfFilterCriteria, filterOptions);
  }

  /**
   * Performs the filter operation on a spec represented as a string using the {@code
   * filterCriteriaList} list and {@code filterOptions}
   *
   * @param specToFilter an OpenAPI specification represented as a YAML string
   * @param filterCriteriaList a list of {@code org.specmath.library.FilterCriteria}
   * @param filterOptions a set of special options which can be applied during the filter
   * @return the result of the filter on {@code specToFilter} using the {@code filterCriteriaList},
   *     as a YAML string
   * @throws IOException if there was a parsing issue
   * @throws UnionConflictException if there was an issue when merging the results from each
   *     org.specmath.library.FilterCriteria
   * @throws AllUnmatchedFilterException if the filtered result would have no paths
   * @throws UnexpectedTypeException if there was an issue when merging the results from each
   *     org.specmath.library.FilterCriteria
   */
  public static String filter(
      String specToFilter, List<FilterCriteria> filterCriteriaList, FilterOptions filterOptions)
      throws UnexpectedTypeException, UnionConflictException, AllUnmatchedFilterException {
    LinkedHashMap<String, Object> spec1map =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(specToFilter);

    LinkedHashMap<String, Object> filterResultMap =
        SpecTreeFilterer.filter(spec1map, (ArrayList<FilterCriteria>) filterCriteriaList);

    if (filterOptions.defaults().isEmpty()) {
      return SpecTreeToYamlStringConverter.convertSpecTreeToYamlString(filterResultMap);
    }

    var defaults =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(filterOptions.defaults());
    var overlayedMap = SpecTreesUnionizer.applyOverlay(defaults, filterResultMap);
    return SpecTreeToYamlStringConverter.convertSpecTreeToYamlString(overlayedMap);
  }

  /**
   * Performs the overlay operation on two specs represented as strings by calling the {@code
   * applyOverlay} function of the {@code SpecTreeUnionizer} class.
   *
   * @param spec an OpenAPI specification represented as a YAML string
   * @param overlay an OpenAPI spec fragment which will take priority over {@code spec} during the
   *     union process
   * @return the result of applying {@code overlay} to {@code spec}, as a YAML string
   * @throws UnexpectedTypeException an unexpected type was met during the union
   */
  public static String applyOverlay(String overlay, String spec) throws UnexpectedTypeException {
    LinkedHashMap<String, Object> specMap =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(spec);
    LinkedHashMap<String, Object> overlayMap =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(overlay);

    LinkedHashMap<String, Object> overlayResultMap =
        SpecTreesUnionizer.applyOverlay(overlayMap, specMap);

    return SpecTreeToYamlStringConverter.convertSpecTreeToYamlString(overlayResultMap);
  }
}
