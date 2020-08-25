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

import java.io.IOException;
import java.util.List;
import org.specmath.library.AllUnmatchedFilterException;
import org.specmath.library.FilterOptions;
import org.specmath.library.UnexpectedTypeException;
import org.specmath.library.UnionConflictException;
import org.specmath.library.UnionOptions;

/** Functions from the Spec Math Library which are used by the CLI. */
class SpecMathService {
  String union(List<String> specsToMerge, UnionOptions unionOptions)
      throws IOException, UnionConflictException, UnexpectedTypeException {
    return org.specmath.library.SpecMath.union(specsToMerge, unionOptions);
  }

  String filter(String specToFilter, String filterCriteriaList, FilterOptions filterOptions)
      throws IOException, UnionConflictException, AllUnmatchedFilterException,
          UnexpectedTypeException {
    return org.specmath.library.SpecMath.filter(specToFilter, filterCriteriaList, filterOptions);
  }

  String applyOverlay(String overlay, String spec) throws UnexpectedTypeException {
    return org.specmath.library.SpecMath.applyOverlay(overlay, spec);
  }
}
