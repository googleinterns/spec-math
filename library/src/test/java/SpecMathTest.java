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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
    String spec1String = Files.readString(Path.of("src/test/resources/elgoogMarketing.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/elgoogBilling.yaml"));

    UnionConflictException e =
        assertThrows(UnionConflictException.class, () -> SpecMath.union(spec1String, spec2String));

    ArrayList<Conflict> expectedConflicts = new ArrayList<>();
    expectedConflicts.add(
        new Conflict("[info, title]", "Elgoog Marketing Team API", "Elgoog Billing Team API"));
    expectedConflicts.add(
        new Conflict(
            "[info, description]",
            "An API for Elgoog's marketing team",
            "An API for Elgoog's billing team"));

    assertThat(e.getConflicts()).isEqualTo(expectedConflicts);
  }

  @Test
  void union_withDefaults_succeeds()
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/elgoogMarketing.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/elgoogBilling.yaml"));
    String defaults = Files.readString(Path.of("src/test/resources/elgoogMetadata.yaml"));

    UnionOptions unionOptions = UnionOptions.builder().defaults(defaults).build();
    String actual = SpecMath.union(spec1String, spec2String, unionOptions);

    String expected =
        Files.readString(Path.of("src/test/resources/elgoogBillingAndMarketingMetadataUnion.yaml"));

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
    String spec1String = Files.readString(Path.of("src/test/resources/elgoogMarketing.yaml"));
    String spec2String = Files.readString(Path.of("src/test/resources/elgoogMarketing.yaml"));
    String actual = SpecMath.union(spec1String, spec2String);
    String expected = Files.readString(Path.of("src/test/resources/elgoogMarketing.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void overlay_appliedToSpec_succeeds() throws IOException, UnexpectedTypeException {
    String spec1String = Files.readString(Path.of("src/test/resources/elgoogMarketing.yaml"));
    String overlay = Files.readString(Path.of("src/test/resources/elgoogMetadata.yaml"));
    String actual = SpecMath.applyOverlay(overlay, spec1String);
    String expected =
        Files.readString(Path.of("src/test/resources/elgoogBillingOverlayedWithMetadata.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withSpecificPath_succeeds()
      throws IOException, AllUnmatchedFilterException, UnionConflictException, UnexpectedTypeException {
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
      throws IOException, AllUnmatchedFilterException, UnionConflictException, UnexpectedTypeException {
    String specString =
        Files.readString(Path.of("src/test/resources/filtering/filteringMonolithicSpec.yaml"));
    String filterCriteria =
        Files.readString(Path.of("src/test/resources/filtering/specificOperationsFilterCriteria.json"));
    String actual = SpecMath.filter(specString, filterCriteria);
    String expected =
        Files.readString(
            Path.of(
                "src/test/resources/filtering/filteredMonolithicSpecWithSpecificOperations.yaml"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void filter_withRemovableTags_succeeds()
      throws IOException, AllUnmatchedFilterException, UnionConflictException, UnexpectedTypeException {
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
      throws IOException, AllUnmatchedFilterException {
    String specString =
        Files.readString(Path.of("src/test/resources/filtering/filteringMonolithicSpec.yaml"));
    String filterCriteria =
        Files.readString(Path.of("src/test/resources/filtering/allFilterCriteria.json"));
    String defaults = Files.readString(Path.of("src/test/resources/filtering/elgoogMetadata.yaml"));
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
      throws IOException, AllUnmatchedFilterException, UnionConflictException, UnexpectedTypeException {
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
