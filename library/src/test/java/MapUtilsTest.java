/*
Copyright 2020 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
You may obtain a copy of the License at

https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import static com.google.common.truth.Truth.assertThat;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapUtilsTest {
  YamlStringToSpecTreeConverter yamlStringToSpecTreeConverter;

  @BeforeEach
  void init() {
    yamlStringToSpecTreeConverter = new YamlStringToSpecTreeConverter();
  }

  @Test
  void testGetKeypathsFromMap() throws FileNotFoundException, UnableToUnionException {
    Map<String, Object> map1 =
        yamlStringToSpecTreeConverter.convertYamlFileToSpecTree("src/test/resources/metadata.yaml");
    HashSet<String> actual = new HashSet<>();

    HashSet<String> expected = new HashSet<>();

    expected.add("[info, version]");
    expected.add("[openapi]");
    expected.add("[info, title]");
    expected.add("[info, license, name]");

    MapUtils.getKeypathsFromMap(map1, new Stack<>(), actual);

    assertThat(actual).isEqualTo(expected);
  }
}
