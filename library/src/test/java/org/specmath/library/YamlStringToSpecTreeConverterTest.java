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

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class YamlStringToSpecTreeConverterTest {
  @Test
  void convertYamlFileToSpecTree_withFakePath_throws() {
    assertThrows(
        FileNotFoundException.class,
        () ->
            YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
                "src/test/resources/fakepath.yaml"));
  }

  @Test
  void convertYamlFileToSpecTree_withValidYamlFile_succeeds() throws FileNotFoundException {
    Map<String, Object> expected = new LinkedHashMap<>();

    Map<String, Object> license = new LinkedHashMap<>();
    license.put("name", "MIT");

    Map<String, Object> info = new LinkedHashMap<>();
    info.put("license", license);
    info.put("title", "Swagger Petstore");

    expected.put("openapi", "3.0.0");
    expected.put("info", info);

    assertThat(
            YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
                "src/test/resources/simplepetstore3.yaml"))
        .isEqualTo(expected);
  }

  @Test
  void convertYamlFileToSpecTree_withEmptyString_returnsEmptyMap() {
    Map<String, Object> expected = new LinkedHashMap<>();

    assertThat(YamlStringToSpecTreeConverter.convertYamlStringToSpecTree("")).isEqualTo(expected);
  }
}
