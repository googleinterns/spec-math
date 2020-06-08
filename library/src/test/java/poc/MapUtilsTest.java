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

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapUtilsTest {
  YamlUtils yamlUtils;
  MapUtils mapUtils;

  @BeforeEach
  void init() {
    yamlUtils = new YamlUtils();
    mapUtils = new MapUtils();
  }

  @Test
  void testMergeMaps() throws FileNotFoundException {
    Map<String, Object> map1 =
        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore.yaml");
    Map<String, Object> map2 =
        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore2.yaml");
    Map<String, Object> expected =
        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstoremerged.yaml");

    assertEquals(expected, mapUtils.mergeMaps(map1, map2));
  }

  /*
  @Ignore("not ready yet, need to sort the keys by original order")
  @Test
  void testMergeMapsToYAML() throws IOException {
    Map<String, Object> map1 =
        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore.yaml");
    Map<String, Object> map2 =
        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore2.yaml");

    String expected =
        new String(Files.readAllBytes(Paths.get("src/test/resources/simplepetstoremerged.yaml")));
    assertEquals(expected, mapUtils.convertMapToYaml(mapUtils.mergeMaps(map1, map2)));
  }

  @Ignore("not ready yet, need to work on indentation in map->YAML output")
  @Test
  void testConvertMapToYaml() throws IOException {
    Map<String, Object> map1 =
        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstoremerged.yaml");

    String expected =
        new String(Files.readAllBytes(Paths.get("src/test/resources/simplepetstoremerged.yaml")));

    assertEquals(expected, mapUtils.convertMapToYaml(map1));
  }
  */
}
