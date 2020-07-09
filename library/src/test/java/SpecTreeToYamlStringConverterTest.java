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
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;

class SpecTreeToYamlStringConverterTest {
  @Test
  void testConvertMapToYamlString() throws IOException, UnexpectedTypeException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/serializationSample.yaml");

    assertThat(SpecTreeToYamlStringConverter.convertSpecTreeToYamlString(map1))
        .isEqualTo(Files.readString(Path.of("src/test/resources/serializationSample.yaml")));
  }

  @Test
  void testSerializeMapWithUnexpectedDataThrows() {
    LinkedHashMap<String, Object> map1 = new LinkedHashMap<>();

    map1.put("key", null);

    assertThrows(
        UnexpectedTypeException.class,
        () -> SpecTreeToYamlStringConverter.convertSpecTreeToYamlString(map1));
  }
}
