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

import com.google.auto.value.AutoValue;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Used to provide additional parameters when calling functions of the
 * org.specmath.library.SpecTreesUnionizer class.
 */
@AutoValue
public abstract class UnionizerUnionParams {
  public static Builder builder() {
    return new AutoValue_UnionizerUnionParams.Builder()
        .defaults(new LinkedHashMap<String, Object>())
        .conflictResolutions(new HashMap<String, Object>());
  }

  public abstract LinkedHashMap<String, Object> defaults();

  public abstract HashMap<String, Object> conflictResolutions();

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract Builder defaults(LinkedHashMap<String, Object> defaults);

    public abstract Builder conflictResolutions(HashMap<String, Object> conflictResolutions);

    public abstract UnionizerUnionParams build();
  }
}
