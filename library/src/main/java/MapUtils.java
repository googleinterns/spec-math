import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtils {

  // TODO merge maps function

  //  public Map<String, Object> mergeMaps(Map<String, Object> map1, Map<String, Object> map2){
  //      Map<String, Object> mergedMap = new LinkedHashMap<String, Object>();
  //
  //      return mergedMap;
  //  }

  public String convertMapToYaml(Map<String, Object> yamlMap) {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

    Yaml yaml = new Yaml(options);

    StringWriter writer = new StringWriter();
    yaml.dump(yamlMap, writer);

    return writer.toString();
  }
}
