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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Provides functions for deserializing representations of {@code org.specmath.library.Conflict}
 * objects.
 */
public class ConflictStringToConflictMapConverter {

  /**
   * Takes a JSON string {@code conflictResolutions} which contains an array of {@code
   * org.specmath.library.Conflict} objects and converts it into a HashMap in which the key is the
   * keypath of a conflict and the value is the resolved value.
   *
   * @param conflictResolutions a JSON string which contains an array of {@code
   *     org.specmath.library.Conflict} objects
   * @return a HashMap which contains a mapping of keypath->value to resolve during union
   * @throws IOException if there was a parsing issue
   */
  public HashMap<String, Object> convertConflictResolutionsStringToConflictMap(
      String conflictResolutions) throws IOException {
    var mapper = new ObjectMapper();

    HashMap<String, Object> conflictMap = new HashMap<>();

    if (!conflictResolutions.isEmpty()) {
      List<Conflict> conflictObjs =
          mapper.readValue(conflictResolutions, new TypeReference<List<Conflict>>() {});
      for (Conflict conflictObj : conflictObjs) {
        String keypath = conflictObj.getKeypath();
        Object resolvedValue = conflictObj.getResolvedValue();
        conflictMap.put(keypath, resolvedValue);
      }
    }

    return conflictMap;
  }
}
