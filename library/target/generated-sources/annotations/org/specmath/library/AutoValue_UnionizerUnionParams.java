

package org.specmath.library;

import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_UnionizerUnionParams extends UnionizerUnionParams {

  private final LinkedHashMap<String, Object> defaults;

  private final HashMap<String, Object> conflictResolutions;

  private AutoValue_UnionizerUnionParams(
      LinkedHashMap<String, Object> defaults,
      HashMap<String, Object> conflictResolutions) {
    this.defaults = defaults;
    this.conflictResolutions = conflictResolutions;
  }

  @Override
  public LinkedHashMap<String, Object> defaults() {
    return defaults;
  }

  @Override
  public HashMap<String, Object> conflictResolutions() {
    return conflictResolutions;
  }

  @Override
  public String toString() {
    return "UnionizerUnionParams{"
         + "defaults=" + defaults + ", "
         + "conflictResolutions=" + conflictResolutions
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof UnionizerUnionParams) {
      UnionizerUnionParams that = (UnionizerUnionParams) o;
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

  static final class Builder extends UnionizerUnionParams.Builder {
    private LinkedHashMap<String, Object> defaults;
    private HashMap<String, Object> conflictResolutions;
    Builder() {
    }
    @Override
    public UnionizerUnionParams.Builder defaults(LinkedHashMap<String, Object> defaults) {
      if (defaults == null) {
        throw new NullPointerException("Null defaults");
      }
      this.defaults = defaults;
      return this;
    }
    @Override
    public UnionizerUnionParams.Builder conflictResolutions(HashMap<String, Object> conflictResolutions) {
      if (conflictResolutions == null) {
        throw new NullPointerException("Null conflictResolutions");
      }
      this.conflictResolutions = conflictResolutions;
      return this;
    }
    @Override
    public UnionizerUnionParams build() {
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
      return new AutoValue_UnionizerUnionParams(
          this.defaults,
          this.conflictResolutions);
    }
  }

}
