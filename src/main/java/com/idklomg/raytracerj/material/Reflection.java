package com.idklomg.raytracerj.material;

import com.google.auto.value.AutoValue;
import com.idklomg.raytracerj.math.Ray;

@AutoValue
public abstract class Reflection {

  public static Reflection.Builder newBuilder() {
    return new AutoValue_Reflection.Builder();
  }

  public abstract Color getAttenuation();
  public abstract Ray getScattered();

  @AutoValue.Builder
  public abstract static class Builder {
    abstract Builder setAttenuation(Color color);
    abstract Builder setScattered(Ray scattered);

    public abstract Reflection build();
  }
}
