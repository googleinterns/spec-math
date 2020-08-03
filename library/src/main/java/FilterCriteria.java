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

import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.List;

/** Used to provide a piece of filtering criteria for the SpecTreeFilterer. */
@AutoValue
public abstract class FilterCriteria {
  public static Builder builder() {
    return new AutoValue_FilterCriteria.Builder().
        path("").
        operations(new ArrayList<String>()).
        tags(new ArrayList<String>()).
        removableTags(new ArrayList<String>());
  }

  public abstract String path();
  public abstract List<String> operations();
  public abstract List<String> tags();
  public abstract List<String> removableTags();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder path(String path);
    public abstract Builder operations(List<String> operations);
    public abstract Builder removableTags(List<String> removableTags);
    public abstract Builder tags(List<String> tags);

    public abstract FilterCriteria build();
  }
}

