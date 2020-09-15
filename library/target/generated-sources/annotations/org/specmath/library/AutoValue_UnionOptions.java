

package org.specmath.library;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_UnionOptions extends UnionOptions {

  private final String defaults;

  private final String conflictResolutions;

  private AutoValue_UnionOptions(
      String defaults,
      String conflictResolutions) {
    this.defaults = defaults;
    this.conflictResolutions = conflictResolutions;
  }

  @Override
  public String defaults() {
    return defaults;
  }

  @Override
  public String conflictResolutions() {
    return conflictResolutions;
  }

  @Override
  public String toString() {
    return "UnionOptions{"
         + "defaults=" + defaults + ", "
         + "conflictResolutions=" + conflictResolutions
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof UnionOptions) {
      UnionOptions that = (UnionOptions) o;
      return (this.defaults.equals(that.defaults()))
           && (this.conflictResolutions.equals(that.conflictResolutions()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= defaults.hashCode();
    h$ *= 1000003;
    h$ ^= conflictResolutions.hashCode();
    return h$;
  }

  static final class Builder extends UnionOptions.Builder {
    private String defaults;
    private String conflictResolutions;
    Builder() {
    }
    @Override
    public UnionOptions.Builder defaults(String defaults) {
      if (defaults == null) {
        throw new NullPointerException("Null defaults");
      }
      this.defaults = defaults;
      return this;
    }
    @Override
    public UnionOptions.Builder conflictResolutions(String conflictResolutions) {
      if (conflictResolutions == null) {
        throw new NullPointerException("Null conflictResolutions");
      }
      this.conflictResolutions = conflictResolutions;
      return this;
    }
    @Override
    public UnionOptions build() {
      String missing = "";
      if (this.defaults == null) {
        missing += " defaults";
      }
      if (this.conflictResolutions == null) {
        missing += " conflictResolutions";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_UnionOptions(
          this.defaults,
          this.conflictResolutions);
    }
  }

}
