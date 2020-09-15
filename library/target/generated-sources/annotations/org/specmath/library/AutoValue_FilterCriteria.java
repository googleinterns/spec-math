

package org.specmath.library;

import java.util.List;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_FilterCriteria extends FilterCriteria {

  private final String path;

  private final List<String> operations;

  private final List<String> tags;

  private final List<String> removableTags;

  private AutoValue_FilterCriteria(
      String path,
      List<String> operations,
      List<String> tags,
      List<String> removableTags) {
    this.path = path;
    this.operations = operations;
    this.tags = tags;
    this.removableTags = removableTags;
  }

  @Override
  public String path() {
    return path;
  }

  @Override
  public List<String> operations() {
    return operations;
  }

  @Override
  public List<String> tags() {
    return tags;
  }

  @Override
  public List<String> removableTags() {
    return removableTags;
  }

  @Override
  public String toString() {
    return "FilterCriteria{"
         + "path=" + path + ", "
         + "operations=" + operations + ", "
         + "tags=" + tags + ", "
         + "removableTags=" + removableTags
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof FilterCriteria) {
      FilterCriteria that = (FilterCriteria) o;
      return (this.path.equals(that.path()))
           && (this.operations.equals(that.operations()))
           && (this.tags.equals(that.tags()))
           && (this.removableTags.equals(that.removableTags()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= path.hashCode();
    h$ *= 1000003;
    h$ ^= operations.hashCode();
    h$ *= 1000003;
    h$ ^= tags.hashCode();
    h$ *= 1000003;
    h$ ^= removableTags.hashCode();
    return h$;
  }

  static final class Builder extends FilterCriteria.Builder {
    private String path;
    private List<String> operations;
    private List<String> tags;
    private List<String> removableTags;
    Builder() {
    }
    @Override
    public FilterCriteria.Builder path(String path) {
      if (path == null) {
        throw new NullPointerException("Null path");
      }
      this.path = path;
      return this;
    }
    @Override
    public FilterCriteria.Builder operations(List<String> operations) {
      if (operations == null) {
        throw new NullPointerException("Null operations");
      }
      this.operations = operations;
      return this;
    }
    @Override
    public FilterCriteria.Builder tags(List<String> tags) {
      if (tags == null) {
        throw new NullPointerException("Null tags");
      }
      this.tags = tags;
      return this;
    }
    @Override
    public FilterCriteria.Builder removableTags(List<String> removableTags) {
      if (removableTags == null) {
        throw new NullPointerException("Null removableTags");
      }
      this.removableTags = removableTags;
      return this;
    }
    @Override
    public FilterCriteria build() {
      String missing = "";
      if (this.path == null) {
        missing += " path";
      }
      if (this.operations == null) {
        missing += " operations";
      }
      if (this.tags == null) {
        missing += " tags";
      }
      if (this.removableTags == null) {
        missing += " removableTags";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_FilterCriteria(
          this.path,
          this.operations,
          this.tags,
          this.removableTags);
    }
  }

}
