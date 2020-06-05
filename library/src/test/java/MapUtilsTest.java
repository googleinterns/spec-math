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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

  // TODO merge maps test once function is complete

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
//  @Test
//  void testMergeMaps2() throws FileNotFoundException {
//    Map<String, Object> map1 =
//        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore.yaml");
//    Map<String, Object> map2 =
//        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore2.yaml");
//    Map<String, Object> expected =
//        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstoremerged.yaml");
//
//      assertEquals(expected, mapUtils.mergeMaps(map2, map1));
//  }
//
//  @Test
//  void testConvertMapToYaml() throws IOException {
//    Map<String, Object> map1 =
//        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore.yaml");
//
//    String expected =
//        new String(Files.readAllBytes(Paths.get("src/test/resources/simplepetstore.yaml")));
//
//    assertEquals(expected, mapUtils.convertMapToYaml(map1));
//  }
}
