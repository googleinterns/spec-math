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

package org.specmath.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import org.yaml.snakeyaml.Yaml;

/** Provides functions for converting YAML files and strings into Maps */
public class YamlStringToSpecTreeConverter {

  /**
   * Deserializes a YAML file at the given {@code pathname} into a map.
   *
   * @param pathname a path to a local file
   * @return a map representing the YAML file at {@code pathname}
   * @throws FileNotFoundException
   */
  public static LinkedHashMap<String, Object> convertYamlFileToSpecTree(String pathname)
      throws FileNotFoundException {
    Yaml yaml = new Yaml();

    File file = new File(pathname);
    InputStream stream = new FileInputStream(file);

    LinkedHashMap<String, Object> yamlMap = yaml.load(stream);

    return yamlMap;
  }

  /**
   * Deserializes a YAML string {@code yamlString} into a map.
   *
   * @param yamlString a YAML string
   * @return a map representing the YAML string
   * @throws FileNotFoundException
   */
  public static LinkedHashMap<String, Object> convertYamlStringToSpecTree(String yamlString) {
    Yaml yaml = new Yaml();

    LinkedHashMap<String, Object> yamlMap;

    if (yamlString.isEmpty()) {
      yamlMap = new LinkedHashMap<>();
    } else {
      yamlMap = yaml.load(yamlString);
    }

    return yamlMap;
  }
}
