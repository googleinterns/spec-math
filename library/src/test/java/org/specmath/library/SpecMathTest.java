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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class SpecMathTest {
  @Test
  void union_withoutConflicts_succeeds()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/noConflict1.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/noConflict2.yaml"));
    String actual = SpecMath.union(spec1String, spec2String);
    String expectedString = Files.readString(Path.of("src/test/resources/noConflictMerged.yaml"));

    assertThat(actual).isEqualTo(expectedString);
  }

  @Test
  void union_withConflictResolutionsAndDefaults_succeeds()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/conflict1.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/conflict2.yaml"));
    String defaults = Files.readString(Path.of("src/test/resources/conflictDefaults.yaml"));

    String conflictResolutions =
        Files.readString(Path.of("src/test/resources/conflictsMergedResolutions.json"));

    UnionOptions unionOptions =
        UnionOptions.builder().defaults(defaults).conflictResolutions(conflictResolutions).build();
    String actual = SpecMath.union(spec1String, spec2String, unionOptions);
    String expected =
        Files.readString(Path.of("src/test/resources/conflictsMergedWithDefaults.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withConflicts_throws() throws IOException {
    String spec1String = Files.readString(Path.of("src/test/resources/petstoreMarketing.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/petstoreBilling.yaml"));

    UnionConflictException e =
        assertThrows(UnionConflictException.class, () -> SpecMath.union(spec1String, spec2String));

    ArrayList<Conflict> expectedConflicts = new ArrayList<>();

    expectedConflicts.add(
        new Conflict(
            "[info, title]",
            Arrays.asList("The Best Petstore Marketing Team API", "The Best Petstore Billing Team API")));
    expectedConflicts.add(
        new Conflict(
            "[info, description]",
            Arrays.asList(
                "An API for The Best Petstore's marketing team", "An API for The Best Petstore's billing team")));

    assertThat(e.getConflicts()).isEqualTo(expectedConflicts);
  }

  @Test
  void union_withDefaults_succeeds()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/petstoreMarketing.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/petstoreBilling.yaml"));
    String defaults = Files.readString(Path.of("src/test/resources/petstoreMetadata.yaml"));

    UnionOptions unionOptions = UnionOptions.builder().defaults(defaults).build();
    String actual = SpecMath.union(spec1String, spec2String, unionOptions);

    String expected =
        Files.readString(Path.of("src/test/resources/petstoreBillingAndMarketingMetadataUnion.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withConflictsFixedByConflictResolutions_succeeds()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/conflict1.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/conflict2.yaml"));

    String conflictResolutions =
        Files.readString(Path.of("src/test/resources/conflictsMergedResolutions.json"));

    UnionOptions unionOptions =
        UnionOptions.builder().conflictResolutions(conflictResolutions).build();

    String actual = SpecMath.union(spec1String, spec2String, unionOptions);

    String expected = Files.readString(Path.of("src/test/resources/conflictMerged.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withConflictsNotFixedByInvalidConflictResolutions_throws() throws IOException {
    String spec1String = Files.readString(Path.of("src/test/resources/conflict1.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/conflict2.yaml"));

    String conflictResolutions =
        Files.readString(Path.of("src/test/resources/unnecessaryConflictResolution.json"));

    UnionOptions unionOptions =
        UnionOptions.builder().conflictResolutions(conflictResolutions).build();

    assertThrows(
        UnionConflictException.class, () -> SpecMath.union(spec1String, spec2String, unionOptions));
  }

  @Test
  void union_withUnnecessaryConflictResolutions_doesNotApplyThem()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/noConflict1.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/noConflict2.yaml"));

    String expected = Files.readString(Path.of("src/test/resources/noConflictMerged.yaml"));

    String conflictResolutions =
        Files.readString(Path.of("src/test/resources/unnecessaryConflictResolution.json"));

    UnionOptions unionOptions =
        UnionOptions.builder().conflictResolutions(conflictResolutions).build();

    String actual = SpecMath.union(spec1String, spec2String, unionOptions);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withEqualStrings_returnsOriginalString()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/petstoreMarketing.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/petstoreMarketing.yaml"));
    String actual = SpecMath.union(spec1String, spec2String);
    String expected = Files.readString(Path.of("src/test/resources/petstoreMarketing.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withListOfEqualSpecs_returnsOriginalString()
    throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/petstoreMarketing.yaml"));

    var listOfSpecTrees = new ArrayList<String>();
    listOfSpecTrees.add(spec1String);
    listOfSpecTrees.add(spec1String);
    listOfSpecTrees.add(spec1String);
    listOfSpecTrees.add(spec1String);

    String actual = SpecMath.union(listOfSpecTrees);
    String expected = Files.readString(Path.of("src/test/resources/petstoreMarketing.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withListOfTwoSpecs_succeeds()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1 = Files.readString(Path.of("src/test/resources/noConflict1.yaml"));
    String spec2 = Files.readString(Path.of("src/test/resources/noConflict2.yaml"));

    var listOfSpecTrees = new ArrayList<String>();
    listOfSpecTrees.add(spec1);
    listOfSpecTrees.add(spec2);

    String actual = SpecMath.union(listOfSpecTrees);
    String expected = Files.readString(Path.of("src/test/resources/noConflictMerged.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withMultipleSpecs_succeeds()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1 = Files.readString(Path.of("src/test/resources/noConflict1.yaml"));
    String spec2 = Files.readString(Path.of("src/test/resources/noConflict2.yaml"));
    String spec3 = Files.readString(Path.of("src/test/resources/noConflict3.yaml"));

    var listOfSpecTrees = new ArrayList<String>();
    listOfSpecTrees.add(spec1);
    listOfSpecTrees.add(spec2);
    listOfSpecTrees.add(spec3);

    String actual = SpecMath.union(listOfSpecTrees);
    String expected = Files.readString(Path.of("src/test/resources/noConflict3SpecsMerged.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withMultipleConflictingSpecs_throwsExceptionWithMultipleOptionsInConflictObject()
      throws IOException {
    String spec1 = Files.readString(Path.of("src/test/resources/conflict1.yaml"));
    String spec2 = Files.readString(Path.of("src/test/resources/conflict2.yaml"));
    String spec3 = Files.readString(Path.of("src/test/resources/conflict3.yaml"));

    var listOfSpecTrees = new ArrayList<String>();
    listOfSpecTrees.add(spec1);
    listOfSpecTrees.add(spec2);
    listOfSpecTrees.add(spec3);

    UnionConflictException e =
        assertThrows(UnionConflictException.class, () -> SpecMath.union(listOfSpecTrees));

    ArrayList<Conflict> expectedConflicts = new ArrayList<>();
    expectedConflicts.add(
        new Conflict(
            "[paths, /pets, get, summary]",
            Arrays.asList("CONFLICT 1", "CONFLICT 2", "CONFLICT 3")));

    assertThat(e.getConflicts()).isEqualTo(expectedConflicts);
  }

  @Test
  void union_withMultipleConflictingSpecs_succeedsByResolvingWithConflictResolutions()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1 = Files.readString(Path.of("src/test/resources/conflict1.yaml"));
    String spec2 = Files.readString(Path.of("src/test/resources/conflict2.yaml"));
    String spec3 = Files.readString(Path.of("src/test/resources/conflict3.yaml"));

    var listOfSpecTrees = new ArrayList<String>();
    listOfSpecTrees.add(spec1);
    listOfSpecTrees.add(spec2);
    listOfSpecTrees.add(spec3);

    String conflictResolutions = Files.readString(Path.of("src/test/resources/conflictsMergedResolutions.json"));

    UnionOptions unionOptions = UnionOptions.builder().conflictResolutions(conflictResolutions).build();

    String actual = SpecMath.union(listOfSpecTrees, unionOptions);
    String expected = Files.readString(Path.of("src/test/resources/conflictMerged.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void union_withMultipleSpecs_succeedsByApplyingConflictResolutionsAndDefaults()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1 = Files.readString(Path.of("src/test/resources/conflict1.yaml"));
    String spec2 = Files.readString(Path.of("src/test/resources/conflict2.yaml"));
    String spec3 = Files.readString(Path.of("src/test/resources/conflict3.yaml"));

    var listOfSpecTrees = new ArrayList<String>();
    listOfSpecTrees.add(spec1);
    listOfSpecTrees.add(spec2);
    listOfSpecTrees.add(spec3);

    String defaults = Files.readString(Path.of("src/test/resources/conflictDefaults.yaml"));

    String conflictResolutions = Files.readString(Path.of("src/test/resources/conflictsMergedResolutions.json"));

    UnionOptions unionOptions =
        UnionOptions.builder()
            .defaults(defaults)
            .conflictResolutions(conflictResolutions)
            .build();

    String actual = SpecMath.union(listOfSpecTrees, unionOptions);
    String expected = Files.readString(Path.of("src/test/resources/conflictsMergedWithDefaults.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void overlay_appliedToSpec_succeeds() throws IOException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/petstoreMarketing.yaml"));
    String overlay = Files.readString(Path.of("src/test/resources/petstoreMetadata.yaml"));
    String actual = SpecMath.applyOverlay(overlay, spec1String);
    String expected =
        Files.readString(Path.of("src/test/resources/petstoreBillingOverlayedWithMetadata.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withSpecificPath_succeeds()
      throws IOException, AllUnmatchedFilterException, UnionConflictException,
          UnexpectedTypeException {
    String specString =
        Files.readString(Path.of("src/test/resources/filtering/filteringMonolithicSpec.yaml"));
    String filterCriteria =
        Files.readString(Path.of("src/test/resources/filtering/specificPathFilterCriteria.json"));
    String actual = SpecMath.filter(specString, filterCriteria);
    String expected =
        Files.readString(
            Path.of("src/test/resources/filtering/filteredMonolithicSpecWithSpecificPath.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withSpecificOperations_succeeds()
      throws IOException, AllUnmatchedFilterException, UnionConflictException,
          UnexpectedTypeException {
    String specString =
        Files.readString(Path.of("src/test/resources/filtering/filteringMonolithicSpec.yaml"));
    String filterCriteria =
        Files.readString(
            Path.of("src/test/resources/filtering/specificOperationsFilterCriteria.json"));
    String actual = SpecMath.filter(specString, filterCriteria);
    String expected =
        Files.readString(
            Path.of(
                "src/test/resources/filtering/filteredMonolithicSpecWithSpecificOperations.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withRemovableTags_succeeds()
      throws IOException, AllUnmatchedFilterException, UnionConflictException,
          UnexpectedTypeException {
    String specString =
        Files.readString(Path.of("src/test/resources/filtering/filteringMonolithicSpec.yaml"));
    String filterCriteria =
        Files.readString(Path.of("src/test/resources/filtering/publicTagsFilterCriteria.json"));
    String actual = SpecMath.filter(specString, filterCriteria);
    String expected =
        Files.readString(
            Path.of("src/test/resources/filtering/filteredMonolithicSpecWithPublicTags.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withFilterCriteriaAndOptions_succeeds()
      throws IOException, AllUnmatchedFilterException, UnionConflictException,
          UnexpectedTypeException {
    String specString =
        Files.readString(Path.of("src/test/resources/filtering/filteringMonolithicSpec.yaml"));
    String filterCriteria =
        Files.readString(Path.of("src/test/resources/filtering/allFilterCriteria.json"));
    String defaults = Files.readString(Path.of("src/test/resources/petstoreMetadata.yaml"));
    FilterOptions filterOptions = FilterOptions.builder().defaults(defaults).build();
    String actual = SpecMath.filter(specString, filterCriteria, filterOptions);
    String expected =
        Files.readString(
            Path.of(
                "src/test/resources/filtering/filteredMonolithicSpecWithOptionsAndAllFilterCriteria.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withMultipleFilterCriteria_succeeds()
      throws IOException, AllUnmatchedFilterException, UnionConflictException,
          UnexpectedTypeException {
    String specString =
        Files.readString(Path.of("src/test/resources/filtering/filteringMonolithicSpec.yaml"));
    String filterCriteria =
        Files.readString(Path.of("src/test/resources/filtering/allFilterCriteria.json"));
    String actual = SpecMath.filter(specString, filterCriteria);
    String expected =
        Files.readString(
            Path.of(
                "src/test/resources/filtering/filteredMonolithicSpecWithAllFilterCriteria.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withAllUnmatchedOrEmptyFilterCriteria_throws()
      throws IOException, AllUnmatchedFilterException {
    String specString =
        Files.readString(Path.of("src/test/resources/filtering/filteringMonolithicSpec.yaml"));
    String filterCriteriaUnmatched =
        Files.readString(Path.of("src/test/resources/filtering/unmatchedFilterCriteria.json"));
    String filterCriteriaEmptyList = "[]";

    assertThrows(
        AllUnmatchedFilterException.class,
        () -> SpecMath.filter(specString, filterCriteriaUnmatched));
    assertThrows(
        AllUnmatchedFilterException.class,
        () -> SpecMath.filter(specString, filterCriteriaEmptyList));
  }
}
