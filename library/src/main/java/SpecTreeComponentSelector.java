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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class SpecTreeComponentSelector {

  /**
   * Given a spec tree, returns only the relevant component refs for the paths present.
   *
   * @param spec a spec tree, which has typically been already filtered and might have unnecessary
   *     components
   * @return the components which are relevant to paths in {@code spec}
   */
  public static LinkedHashMap<String, Object> getRelevantComponents(
      LinkedHashMap<String, Object> spec) throws UnionConflictException, UnexpectedTypeException {
    var outputComponents = new LinkedHashMap<String, Object>();
    if (!spec.containsKey("components")) {
      return outputComponents;
    }

    var keypaths = new HashSet<String>();
    addRefsInSubtreeToKeypaths(
        ObjectCaster.castObjectToStringObjectMap(spec.get("paths")), keypaths);

    final int MAX_NUM_NESTED_COMPONENTS =
        ObjectCaster.castObjectToStringObjectMap(spec.get("components")).size();
    int prevSize = 0;
    for (int i = 0; i <= MAX_NUM_NESTED_COMPONENTS; i++) {
      // as soon as keypaths.prevSize() == prevSize, terminate because no new refs need to be looked
      // for. This is an optimization.
      if (prevSize == keypaths.size()) {
        break;
      }

      // set the prevSize to the current size of keypaths. If it does not change by the end of this
      // for loop, meaning that there are no new keypaths to search for, we will break at the start
      // of the next iteration.
      prevSize = keypaths.size();

      LinkedHashMap<String, Object> relevantComponents =
          expandComponentTree(spec, keypaths, new Stack<String>());
      outputComponents = SpecTreesUnionizer.union(outputComponents, relevantComponents);
    }

    return outputComponents;
  }

  /** Add all the refs in this subtree to {@code refKeyPaths}. */
  private static void addRefsInSubtreeToKeypaths(
      LinkedHashMap<String, Object> subtree, HashSet<String> refKeypaths) {
    for (Map.Entry<String, Object> entry : subtree.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      if (key.equals("$ref")) {
        String processedRefKeyPath = processRefKeyPath(value);
        if (!processedRefKeyPath.isEmpty()) {
          refKeypaths.add(processedRefKeyPath);
        }
      } else if (TypeChecker.isObjectMap(value)) {
        LinkedHashMap<String, Object> subtreeMap = ObjectCaster.castObjectToStringObjectMap(value);
        addRefsInSubtreeToKeypaths(subtreeMap, refKeypaths);
      }
    }
  }

  /**
   * Return a formatted version the ref. For example, "#/components/schemas/Pet" would be formatted
   * to "[components, schemas, Pet]"
   */
  private static String processRefKeyPath(Object value) {
    String refToProcess = (String) value;
    if (refToProcess.substring(0, 1).equals("#")) {
      // ignore the first two characters (#/)
      return Arrays.asList(refToProcess.substring(2).split("/")).toString();
    } else {
      return "";
    }
  }

  /**
   * Finds all components which match those in {@code keypaths} and outputs them. Also, add all refs
   * which components are dependent on to {@code keypaths}.
   */
  private static LinkedHashMap<String, Object> expandComponentTree(
      LinkedHashMap<String, Object> components, HashSet<String> keypaths, Stack<String> keypath) {

    var outputForThisLevel = new LinkedHashMap<String, Object>();

    for (Map.Entry<String, Object> entry : components.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      keypath.push(key);
      handleExpandSubtree(keypaths, keypath, outputForThisLevel, key, value);
      keypath.pop();
    }

    return outputForThisLevel;
  }

  private static void handleExpandSubtree(
      HashSet<String> keypaths,
      Stack<String> keypath,
      LinkedHashMap<String, Object> outputForThisLevel,
      String key,
      Object value) {
    if (keypaths.contains(keypath.toString())) {
      // add this subtree
      if (TypeChecker.isObjectMap(value)) {
        addSpecificSubtree(keypaths, outputForThisLevel, key, value);
      }
    } else if (TypeChecker.isObjectMap(value)) {
      visitAndAddRelevantSubtrees(keypaths, keypath, outputForThisLevel, key, value);
    }
  }

  private static void visitAndAddRelevantSubtrees(
      HashSet<String> keypaths,
      Stack<String> keypath,
      LinkedHashMap<String, Object> outputForThisLevel,
      String key,
      Object value) {
    LinkedHashMap<String, Object> subtreeMap = ObjectCaster.castObjectToStringObjectMap(value);
    LinkedHashMap<String, Object> outputForSubtree =
        expandComponentTree(subtreeMap, keypaths, keypath);

    if (!outputForSubtree.isEmpty()) {
      outputForThisLevel.put(key, outputForSubtree);
    }
  }

  private static void addSpecificSubtree(
      HashSet<String> keypaths,
      LinkedHashMap<String, Object> outputForThisLevel,
      String key,
      Object value) {
    LinkedHashMap<String, Object> subtreeMap = ObjectCaster.castObjectToStringObjectMap(value);
    addRefsInSubtreeToKeypaths(subtreeMap, keypaths);
    outputForThisLevel.put(key, subtreeMap);
  }
}
