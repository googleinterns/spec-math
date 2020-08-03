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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class SpecTreeFiltererTest {
  @Test
  void filter_withSpecificPath_succeeds()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException,
          AllUnmatchedFilterException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    FilterCriteria filterCriteria = FilterCriteria.builder().path("/pets/{petId}").build();

    var listOfFilterCriteria = new ArrayList<FilterCriteria>();
    listOfFilterCriteria.add(filterCriteria);

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteredMonolithicSpecWithSpecificPath.yaml");

    var actual = SpecTreeFilterer.filter(map1, listOfFilterCriteria);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withSpecificOperations_succeeds()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException,
          AllUnmatchedFilterException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    var operations = new ArrayList<String>();
    operations.add("post");
    operations.add("PATCH");

    FilterCriteria filterCriteria = FilterCriteria.builder().operations(operations).build();

    var listOfFilterCriteria = new ArrayList<FilterCriteria>();
    listOfFilterCriteria.add(filterCriteria);

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteredMonolithicSpecWithSpecificOperations.yaml");
    var actual = SpecTreeFilterer.filter(map1, listOfFilterCriteria);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withRemovableTags_succeeds()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException,
          AllUnmatchedFilterException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    var removableTags = new ArrayList<String>();
    removableTags.add("public");

    FilterCriteria filterCriteria = FilterCriteria.builder().removableTags(removableTags).build();

    var listOfFilterCriteria = new ArrayList<FilterCriteria>();
    listOfFilterCriteria.add(filterCriteria);

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteredMonolithicSpecWithPublicTags.yaml");

    var actual = SpecTreeFilterer.filter(map1, listOfFilterCriteria);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withMultipleFilterCriteria_succeeds()
      throws FileNotFoundException, UnionConflictException, AllUnmatchedFilterException,
          UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    var tags = new ArrayList<String>();
    tags.add("pets");

    FilterCriteria filterCriteria1 = FilterCriteria.builder().tags(tags).build();

    var operations = new ArrayList<String>();
    operations.add("get");

    FilterCriteria filterCriteria2 =
        FilterCriteria.builder().operations(operations).path("/pets/*").build();

    var listOfFilterCriteria = new ArrayList<FilterCriteria>();
    listOfFilterCriteria.add(filterCriteria1);
    listOfFilterCriteria.add(filterCriteria2);

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteredMonolithicSpecWithAllFilterCriteria.yaml");
    var actual = SpecTreeFilterer.filter(map1, listOfFilterCriteria);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withUnselectedFilterCriteria_returnsOriginalSpec()
      throws FileNotFoundException, UnionConflictException, AllUnmatchedFilterException,
          UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    FilterCriteria filterCriteria = FilterCriteria.builder().build();

    var listOfFilterCriteria = new ArrayList<FilterCriteria>();
    listOfFilterCriteria.add(filterCriteria);

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");
    var actual = SpecTreeFilterer.filter(map1, listOfFilterCriteria);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withPathsThatWouldHaveNestedComponents_returnsComponentDependencyTree()
      throws FileNotFoundException, UnionConflictException, AllUnmatchedFilterException,
          UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    var operations = new ArrayList<String>();
    operations.add("get");

    FilterCriteria filterCriteria =
        FilterCriteria.builder().operations(operations).path("/pets").build();

    var listOfFilterCriteria = new ArrayList<FilterCriteria>();
    listOfFilterCriteria.add(filterCriteria);

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteredMonolithicSpecWithOnlyPetsGet.yaml");

    var actual = SpecTreeFilterer.filter(map1, listOfFilterCriteria);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withOnlyPathsThatDoNotHaveRefs_removesAllComponents()
      throws FileNotFoundException, UnionConflictException, AllUnmatchedFilterException,
          UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    FilterCriteria filterCriteria = FilterCriteria.builder().path("/path/with/no/refs").build();

    var listOfFilterCriteria = new ArrayList<FilterCriteria>();
    listOfFilterCriteria.add(filterCriteria);

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteredMonolithicSpecWithSpecificPathNoRefs.yaml");

    var actual = SpecTreeFilterer.filter(map1, listOfFilterCriteria);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withAllUnmatchedOrEmptyFilterCriteriaList_throws() throws FileNotFoundException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    var filterCriteriaEmptyList = new ArrayList<FilterCriteria>();

    LinkedHashMap<String, Object> expected =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteredMonolithicSpecWithPublicTags.yaml");

    assertThrows(
        AllUnmatchedFilterException.class,
        () -> SpecTreeFilterer.filter(map1, filterCriteriaEmptyList));
  }

  @Test
  void matches_succeeds() throws IOException {
    var mapper = new ObjectMapper();

    String filterCriteriaList =
        Files.readString(Path.of("src/test/resources/filtering/pathsTest.json"));

    List<FilterCriteriaJson> filterCriteria =
        mapper.readValue(filterCriteriaList, new TypeReference<List<FilterCriteriaJson>>() {});

    assertTrue(SpecTreeFilterer.matches("/pets/{id}/groom", filterCriteria.get(0).path));
    assertTrue(SpecTreeFilterer.matches("/pets/{id}", filterCriteria.get(1).path));
    assertFalse(SpecTreeFilterer.matches("/pets/\\{id\\}", filterCriteria.get(1).path));
    assertTrue(SpecTreeFilterer.matches("asdfjklasjdfkljasdklfj", filterCriteria.get(2).path));
    assertTrue(SpecTreeFilterer.matches("", filterCriteria.get(2).path));
    assertTrue(SpecTreeFilterer.matches("/pets/hello.fdsfkdsjkworld", filterCriteria.get(3).path));
    assertFalse(SpecTreeFilterer.matches("/pets/hellofdsfkdsjkworld", filterCriteria.get(3).path));
    assertTrue(
        SpecTreeFilterer.matches("/pets/dsadasdas/dsadsadsa/{id}", filterCriteria.get(4).path));
  }
}
