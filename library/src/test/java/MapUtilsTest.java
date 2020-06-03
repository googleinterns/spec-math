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

  //  @Test
  //  void testMergeMaps() throws FileNotFoundException {
  //    Map<String, Object> map1 =
  //        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore.yaml");
  //    Map<String, Object> map2 =
  //        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore2.yaml");
  //    Map<String, Object> expected =
  //        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstoremerged.yaml");
  //
  //    assertEquals(expected, mapUtils.mergeMaps(map1, map2));
  //  }

  @Test
  void testConvertMapToYaml() throws IOException {
    Map<String, Object> map1 =
        yamlUtils.convertYamlFileToMap("src/test/resources/simplepetstore.yaml");

    String content =
        new String(Files.readAllBytes(Paths.get("src/test/resources/simplepetstore.yaml")));

    assertEquals(content, mapUtils.convertMapToYaml(map1));
  }
}
