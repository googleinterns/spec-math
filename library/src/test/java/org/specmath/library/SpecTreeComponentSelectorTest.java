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

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;

class SpecTreeComponentSelectorTest {
  @Test
  void getRelevantComponents_withoutNestedComponents_succeeds()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> originalSpec =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    LinkedHashMap<String, Object> filteredSpec =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteredMonolithicSpecWithSpecificPath.yaml");

    LinkedHashMap<String, Object> expectedComponents =
        ObjectCaster.castObjectToStringObjectMap(filteredSpec.get("components"));

    filteredSpec.put("components", originalSpec.get("components"));

    var actualComponents =
        SpecTreeComponentSelector.getRelevantComponents(filteredSpec).get("components");

    assertThat(actualComponents).isEqualTo(expectedComponents);
  }

  @Test
  void getRelevantComponents_withOneLevelNestedComponents_succeeds()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> originalSpec =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteringMonolithicSpec.yaml");

    LinkedHashMap<String, Object> filteredSpec =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/filteredMonolithicSpecWithOnlyPetsGet.yaml");

    LinkedHashMap<String, Object> expectedComponents =
        ObjectCaster.castObjectToStringObjectMap(filteredSpec.get("components"));

    filteredSpec.put("components", originalSpec.get("components"));

    var actualComponents =
        SpecTreeComponentSelector.getRelevantComponents(filteredSpec).get("components");

    assertThat(actualComponents).isEqualTo(expectedComponents);
  }

  @Test
  void getRelevantComponents_withThreeLevelNestedComponents_succeeds()
      throws FileNotFoundException, UnionConflictException, UnexpectedTypeException {
    LinkedHashMap<String, Object> originalSpec =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/componentSelectionDeeplyNestedOriginal.yaml");

    LinkedHashMap<String, Object> expectedComponents =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/filtering/componentSelectionDeeplyNestedRelevantComponents.yaml");

    var actualComponents = SpecTreeComponentSelector.getRelevantComponents(originalSpec);

    assertThat(actualComponents).isEqualTo(expectedComponents);
  }
}
