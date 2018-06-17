package com.idklomg.raytracerj.math;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Normal implements Thing3D {

  public static Normal.Builder newBuilder() {
    return new AutoValue_Normal.Builder();
  }

  public static Normal create(double x, double y, double z) {
    return newBuilder().setX(x).setY(y).setZ(z).build();
  }

  public abstract double getX();
  public abstract double getY();
  public abstract double getZ();

  @AutoValue.Builder
  public abstract static class Builder {
    abstract Builder setX(double x);
    abstract Builder setY(double y);
    abstract Builder setZ(double z);

    public abstract Normal build();
  }
}
