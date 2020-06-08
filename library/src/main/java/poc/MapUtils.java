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

import java.util.ArrayList;
import java.util.List;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.StringWriter;
import java.util.Map;

public class MapUtils {

  public String convertMapToYaml(Map<String, Object> yamlMap) {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

    options.setIndent(4);
    options.setIndicatorIndent(2);
    Yaml yaml = new Yaml(options);

    StringWriter writer = new StringWriter();
    yaml.dump(yamlMap, writer);

    return writer.toString();
  }

  public Map<String, Object> mergeMaps(Map<String, Object> map1, Map<String, Object> map2) {
    // traverse map2
    for (Map.Entry<String, Object> entry : map2.entrySet()) {
      String key = entry.getKey();
      Object value2 = entry.getValue();
      if (map1.containsKey(entry.getKey())) {
        // they could both be values, they could both be maps,
        Object value1 = map1.get(key);
        if (value1 instanceof Map && value2 instanceof Map) {
          // need to process further
          Map<String, Object> nmap1 = (Map<String, Object>) value1;
          Map<String, Object> nmap2 = (Map<String, Object>) value2;
          if (!nmap1.equals(nmap2)) {
            // there is some diff in value1 and value2
            map1.put(key, mergeMaps(nmap1, nmap2));
          }
        } else if (value1 instanceof List && value2 instanceof List) {
          List<Object> output = new ArrayList<Object>((List<Object>) value2);
          output.addAll((List<Object>) value1);
          map1.put(key, output);
        } else {
          // ASSUMPTION: both are primitive values, so choose one of them
          // current rule is just pick the value from spec2
          map1.put(key, value2);
        }
      } else {
        // the original map didn't contain this key, its a new key so add to tree
        map1.put(key, value2);
      }
    }

    return map1;
  }
}
