import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;

class SpecTreeToYamlStringConverterTest {
  @Test
  void testConvertMapToYamlString() throws IOException, UnexpectedDataException {
    LinkedHashMap<String, Object> map1 =
        YamlStringToSpecTreeConverter.convertYamlFileToSpecTree(
            "src/test/resources/serializationSample.yaml");

    var specTreeToYamlStringsConverter = new SpecTreeToYamlStringConverter();

    assertThat(specTreeToYamlStringsConverter.convertSpecTreeToYamlString(map1))
        .isEqualTo(Files.readString(Path.of("src/test/resources/serializationSample.yaml")));
  }

  @Test
  void testSerializeMapWithUnexpectedDataThrows() {
    LinkedHashMap<String, Object> map1 = new LinkedHashMap<>();

    map1.put("key", null);

    var specTreeToYamlStringsConverter = new SpecTreeToYamlStringConverter();

    assertThrows(
        UnexpectedDataException.class,
        () -> specTreeToYamlStringsConverter.convertSpecTreeToYamlString(map1));
  }
}
