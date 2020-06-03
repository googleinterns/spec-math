import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class YamlUtils {

  public Map<String, Object> convertYamlFileToMap(String pathname) throws FileNotFoundException {
    Yaml yaml = new Yaml();

    File file = new File(pathname);
    InputStream stream = new FileInputStream(file);

    Map<String, Object> yamlMap = yaml.load(stream);

    return yamlMap;
  }
}
