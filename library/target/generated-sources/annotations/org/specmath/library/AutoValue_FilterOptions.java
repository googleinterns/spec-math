

package org.specmath.library;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_FilterOptions extends FilterOptions {

  private final String defaults;

  private AutoValue_FilterOptions(
      String defaults) {
    this.defaults = defaults;
  }

  @Override
  public String defaults() {
    return defaults;
  }

  @Override
  public String toString() {
    return "FilterOptions{"
         + "defaults=" + defaults
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof FilterOptions) {
      FilterOptions that = (FilterOptions) o;
      return (this.defaults.equals(that.defaults()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= defaults.hashCode();
    return h$;
  }

  static final class Builder extends FilterOptions.Builder {
    private String defaults;
    Builder() {
    }
    @Override
    public FilterOptions.Builder defaults(String defaults) {
      if (defaults == null) {
        throw new NullPointerException("Null defaults");
      }
      this.defaults = defaults;
      return this;
    }
    @Override
    public FilterOptions build() {
      String missing = "";
      if (this.defaults == null) {
        missing += " defaults";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_FilterOptions(
          this.defaults);
    }
  }

}
