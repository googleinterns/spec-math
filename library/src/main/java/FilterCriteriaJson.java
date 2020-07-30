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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.ArrayList;
import java.util.List;

/**
 * Used with Jackson's ObjectMapper to deserialize single JSON objects representing filter criteria.
 */
public class FilterCriteriaJson {
  @JsonProperty("pathRegex")
  public String pathRegex = "";

  @JsonProperty("tags")
  public List<String> tags = new ArrayList<String>();

  @JsonProperty("removableTags")
  public List<String> removableTags = new ArrayList<String>();

  @JsonProperty("operations")
  public List<String> operations = new ArrayList<String>();

  /** Used to set the pathRegex. In the case of null, it uses the default value, an empty string. */
  @JsonSetter("pathRegex")
  public void setPathRegex(String pathRegex) {
    if (pathRegex != null) {
      this.pathRegex = pathRegex;
    }
  }

  /** Used to set the tags list. In the case of null, it uses the default value, an empty list. */
  @JsonSetter("tags")
  public void setTags(List<String> tags) {
    if (tags != null) {
      this.tags = tags;
    }
  }

  /**
   * Used to set the removableTags list. In the case of null, it uses the default value, an empty
   * list.
   */
  @JsonSetter("removableTags")
  public void setRemovableTags(List<String> removableTags) {
    if (removableTags != null) {
      this.removableTags = removableTags;
    }
  }

  /**
   * Used to set the operations list. In the case of null, it uses the default value, an empty list.
   */
  @JsonSetter("operations")
  public void setOperations(List<String> operations) {
    if (operations != null) {
      this.operations = operations;
    }
  }
}
