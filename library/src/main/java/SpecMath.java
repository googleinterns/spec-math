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

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
   * the union and if any conflicts are found a {@code UnionConflictException} will be thrown.
   *
   * @param spec1 the first spec to be merged.
   * @param spec2 the second spec to be merged.
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
   * Performs the union operation on two specs represented as strings with {@code UnionOptions}.
   *
   * <p>If {@code UnionOptions} are provided, then it will apply the options as is appropriate based
   * on the logic in the {@code SpecTreeUnionizer} class. If {@code UnionOptions} cannot resolve the
   * conflict then a {@code UnionConflictException} will be thrown.
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
   * Performs the overlay operation on two specs represented as strings by calling the {@code
   * applyOverlay} function of the {@code SpecTreeUnionizer} class.
   *
   * @param spec a spec to apply the {@code overlay} to
   * @param overlay a spec fragment which will take priority over {@code spec} during the union
   *     process
   * @return the result of applying {@code overlay} to {@code spec}, as a YAML string
   * @throws UnexpectedTypeException an unexpected type was met during the union
   */
  public static String applyOverlay(String overlay, String spec) throws UnexpectedTypeException {
    LinkedHashMap<String, Object> spec1map =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(spec);
    LinkedHashMap<String, Object> overlayMap =
        YamlStringToSpecTreeConverter.convertYamlStringToSpecTree(overlay);

    LinkedHashMap<String, Object> overlayResultMap =
        SpecTreesUnionizer.applyOverlay(overlayMap, spec1map);

    return SpecTreeToYamlStringConverter.convertSpecTreeToYamlString(overlayResultMap);
  }
}
