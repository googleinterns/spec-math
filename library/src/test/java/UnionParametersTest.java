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

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

class UnionParametersTest {
  @Test
  void testUseUnionOptionsBuilderBuilds() {
    UnionOptions unionOptions =
        UnionOptions.builder()
            .conflictResolutions("INVALID CONFLICT RESOLUTIONS")
            .defaults("INVALID DEFAULTS")
            .build();

    assertThat(unionOptions.defaults()).isEqualTo("INVALID DEFAULTS");
    assertThat(unionOptions.conflictResolutions()).isEqualTo("INVALID CONFLICT RESOLUTIONS");
  }

  @Test
  void testUseUnionOptionsBuilderDefaultsEmpty() {
    UnionOptions unionOptions =
        UnionOptions.builder().conflictResolutions("INVALID CONFLICT RESOLUTIONS").build();

    assertThat(unionOptions.defaults()).isEqualTo("");
    assertThat(unionOptions.conflictResolutions()).isEqualTo("INVALID CONFLICT RESOLUTIONS");
  }

  @Test
  void testUseUnionOptionsBuilderConflictResolutionsEmpty() {
    UnionOptions unionOptions =
        UnionOptions.builder().defaults("INVALID DEFAULTS").build();

    assertThat(unionOptions.conflictResolutions()).isEqualTo("");
    assertThat(unionOptions.defaults()).isEqualTo("INVALID DEFAULTS");
  }

  @Test
  void testUseUnionOptionsBuilderEmpty() {
    UnionOptions unionOptions = UnionOptions.builder().build();

    assertThat(unionOptions.defaults()).isEqualTo("");
    assertThat(unionOptions.conflictResolutions()).isEqualTo("");
  }
}
