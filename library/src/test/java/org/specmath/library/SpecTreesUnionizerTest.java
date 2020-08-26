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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpecTreesUnionizerTest {
  YamlStringToSpecTreeConverter yamlStringToSpecTreeConverter;

  @BeforeEach
  void init() {
    yamlStringToSpecTreeConverter = new YamlStringToSpecTreeConverter();
  }

  @Test
  void union_withoutConflicts_succeeds()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict2.yaml");

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflictMerged.yaml");

    assertThat(SpecTreesUnionizer.union(map1, map2)).isEqualTo(expected);
  }

  @Test
  void union_withConflictResolutionsAndDefaults_succeeds()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict2.yaml");
    LinkedHashMap<String, Object> defaults =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflictDefaults.yaml");

    HashMap<String, Object> conflictResolutions = new HashMap<>();
    conflictResolutions.put("[paths, /pets, get, summary]", "get the pets");

    UnionizerUnionParams unionizerUnionParams =
        UnionizerUnionParams.builder()
            .defaults(defaults)
            .conflictResolutions(conflictResolutions)
            .build();

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflictsMergedWithDefaults.yaml");

    assertThat(SpecTreesUnionizer.union(map1, map2, unionizerUnionParams)).isEqualTo(expected);
  }

  @Test
  void union_withConflicts_throwsWithExceptionContainingConflictObjects()
      throws FileNotFoundException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/simplepetstore.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/simplepetstore2.yaml");
    UnionizerUnionParams unionizerUnionParams = UnionizerUnionParams.builder().build();

    UnionConflictException e =
        assertThrows(
            UnionConflictException.class,
            () -> SpecTreesUnionizer.union(map1, map2, unionizerUnionParams));

    ArrayList<Conflict> expectedConflicts = new ArrayList<>();
    expectedConflicts.add(
        new Conflict(
            "[info, title]",
            Arrays.asList("Swagger Petstore Platform", "Swagger Petstore Marketing")));
    expectedConflicts.add(
        new Conflict(
            "[paths, /pets, get, summary]", Arrays.asList("List all pets", "List every pet")));

    assertThat(e.getConflicts()).isEqualTo(expectedConflicts);
  }

  @Test
  void union_withConflicts_succeedsByResolvingWithDefaults()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/simplepetstore.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/simplepetstore2.yaml");
    LinkedHashMap<String, Object> defaults =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/simplepetstoredefaults.yaml");

    UnionizerUnionParams unionizerUnionParams =
        UnionizerUnionParams.builder().defaults(defaults).build();

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/simplepetstoremerged.yaml");

    assertThat(SpecTreesUnionizer.union(map1, map2, unionizerUnionParams)).isEqualTo(expected);
  }

  @Test
  void union_withConflicts_succeedsByResolvingWithConflictResolutions()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict2.yaml");

    HashMap<String, Object> conflictResolutions = new HashMap<>();
    conflictResolutions.put("[paths, /pets, get, summary]", "get the pets");

    UnionizerUnionParams unionizerUnionParams =
        UnionizerUnionParams.builder().conflictResolutions(conflictResolutions).build();

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflictMerged.yaml");

    assertThat(SpecTreesUnionizer.union(map1, map2, unionizerUnionParams)).isEqualTo(expected);
  }

  @Test
  void union_withConflicts_notFixedByInvalidConflictResolutions()
      throws FileNotFoundException, UnionConflictException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict2.yaml");

    HashMap<String, Object> conflictResolutions = new HashMap<>();
    conflictResolutions.put("[this, /isnt, a, path]", "CONFLICT RESOLVED");

    UnionizerUnionParams unionizerUnionParams =
        UnionizerUnionParams.builder().conflictResolutions(conflictResolutions).build();

    assertThrows(
        UnionConflictException.class,
        () -> SpecTreesUnionizer.union(map1, map2, unionizerUnionParams));
  }

  @Test
  void union_withUnnecessaryConflictResolutions_succeedsButDoesNotApplyTheResolutions()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict2.yaml");

    HashMap<String, Object> conflictResolutions = new HashMap<>();
    conflictResolutions.put("[info, title]", "CONFLICT RESOLUTION TITLE");

    UnionizerUnionParams unionizerUnionParams =
        UnionizerUnionParams.builder().conflictResolutions(conflictResolutions).build();

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflictMerged.yaml");

    var actual = SpecTreesUnionizer.union(map1, map2, unionizerUnionParams);

    assertThat(ObjectCaster.castObjectToStringObjectMap(actual.get("info")).get("title"))
        .isNotEqualTo("CONFLICT RESOLUTION TITLE");

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withUnexpectedData_throws() {
    LinkedHashMap<String, Object> map1 = new LinkedHashMap<>();
    LinkedHashMap<String, Object> map2 = new LinkedHashMap<>();

    map1.put("samekey", new LinkedHashMap<String, Object>());
    map2.put("samekey", "not a map");

    assertThrows(UnexpectedTypeException.class, () -> SpecTreesUnionizer.union(map1, map2));
  }

  @Test
  void union_withListOfTwoSpecTrees_succeeds()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict2.yaml");

    var listOfSpecTrees = new ArrayList<LinkedHashMap<String, Object>>();
    listOfSpecTrees.add(map1);
    listOfSpecTrees.add(map2);

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflictMerged.yaml");

    LinkedHashMap<String, Object> actual = SpecTreesUnionizer.union(listOfSpecTrees);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withMultipleSpecTrees_succeeds()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict2.yaml");
    LinkedHashMap<String, Object> map3 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict3.yaml");

    var listOfSpecTrees = new ArrayList<LinkedHashMap<String, Object>>();
    listOfSpecTrees.add(map1);
    listOfSpecTrees.add(map2);
    listOfSpecTrees.add(map3);

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/noConflict3SpecsMerged.yaml");

    LinkedHashMap<String, Object> actual = SpecTreesUnionizer.union(listOfSpecTrees);

    assertEquals(expected, actual);
  }

  @Test
  void union_withMultipleConflictingSpecTrees_throwsExceptionContainingMultipleOptionsInConflictObject()
      throws FileNotFoundException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict2.yaml");
    LinkedHashMap<String, Object> map3 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict3.yaml");

    var listOfSpecTrees = new ArrayList<LinkedHashMap<String, Object>>();
    listOfSpecTrees.add(map1);
    listOfSpecTrees.add(map2);
    listOfSpecTrees.add(map3);

    UnionConflictException e =
        assertThrows(UnionConflictException.class, () -> SpecTreesUnionizer.union(listOfSpecTrees));

    ArrayList<Conflict> expectedConflicts = new ArrayList<>();
    expectedConflicts.add(
        new Conflict(
            "[paths, /pets, get, summary]",
            Arrays.asList("CONFLICT 1", "CONFLICT 2", "CONFLICT 3")));

    assertThat(e.getConflicts()).isEqualTo(expectedConflicts);
  }

  @Test
  void union_withMultipleConflictingSpecTrees_succeedsByResolvingWithConflictResolutions()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict2.yaml");
    LinkedHashMap<String, Object> map3 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict3.yaml");

    var listOfSpecTrees = new ArrayList<LinkedHashMap<String, Object>>();
    listOfSpecTrees.add(map1);
    listOfSpecTrees.add(map2);
    listOfSpecTrees.add(map3);

    HashMap<String, Object> conflictResolutions = new HashMap<>();
    conflictResolutions.put("[paths, /pets, get, summary]", "get the pets");

    UnionizerUnionParams unionizerUnionParams =
        UnionizerUnionParams.builder().conflictResolutions(conflictResolutions).build();

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflictMerged.yaml");

    assertThat(SpecTreesUnionizer.union(listOfSpecTrees, unionizerUnionParams)).isEqualTo(expected);
  }

  @Test
  void union_withMultipleSpecTrees_succeedsByApplyingConflictResolutionsAndDefaults()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict1.yaml");
    LinkedHashMap<String, Object> map2 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict2.yaml");
    LinkedHashMap<String, Object> map3 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflict3.yaml");

    var listOfSpecTrees = new ArrayList<LinkedHashMap<String, Object>>();
    listOfSpecTrees.add(map1);
    listOfSpecTrees.add(map2);
    listOfSpecTrees.add(map3);

    LinkedHashMap<String, Object> defaults =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflictDefaults.yaml");

    HashMap<String, Object> conflictResolutions = new HashMap<>();
    conflictResolutions.put("[paths, /pets, get, summary]", "get the pets");

    UnionizerUnionParams unionizerUnionParams =
        UnionizerUnionParams.builder()
            .defaults(defaults)
            .conflictResolutions(conflictResolutions)
            .build();

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/conflictsMergedWithDefaults.yaml");

    assertThat(SpecTreesUnionizer.union(listOfSpecTrees, unionizerUnionParams)).isEqualTo(expected);
  }

  @Test
  void applyOverlay_succeeds() throws UnexpectedTypeException, FileNotFoundException {
    LinkedHashMap<String, Object> specString =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/petstoreMarketing.yaml");
    LinkedHashMap<String, Object> overlay =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/petstoreMetadata.yaml");
    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/petstoreBillingOverlayedWithMetadata.yaml");

    assertThat(SpecTreesUnionizer.applyOverlay(overlay, specString)).isEqualTo(expected);
  }
}
