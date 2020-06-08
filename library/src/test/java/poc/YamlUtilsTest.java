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

package poc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YamlUtilsTest {
  YamlUtils yamlUtils;

  @BeforeEach
  void init() {
    yamlUtils = new YamlUtils();
  }

  @Test
  void testThrowFileNotFoundException() {
    assertThrows(
        FileNotFoundException.class,
        () -> yamlUtils.convertYamlFileToMap("src/test/resources/fakepath.yaml"));
  }

  @Test
  void testConvertYamlToMap() throws FileNotFoundException {
    Map<String, Object> expected = new LinkedHashMap<String, Object>();

    Map<String, Object> license = new LinkedHashMap<String, Object>();
    license.put("name", "MIT");

    Map<String, Object> info = new LinkedHashMap<String, Object>();
    info.put("license", license);
    info.put("title", "Swagger Petstore");

    expected.put("openapi", "3.0.0");
    expected.put("info", info);

    assertEquals(
        expected, yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore3.yaml"));
  }
}
