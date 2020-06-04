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
