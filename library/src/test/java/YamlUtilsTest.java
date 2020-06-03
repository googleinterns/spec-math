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
        expected, yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore.yaml"));
  }
}
