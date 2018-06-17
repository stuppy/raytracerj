package com.idklomg.raytracerj.math;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Point2D {

  public static Point2D.Builder newBuilder() {
    return new AutoValue_Point2D.Builder();
  }

  public static Point2D create(double x, double y) {
    return newBuilder().setX(x).setY(y).build();
  }

  public abstract double getX();
  public abstract double getY();

  @AutoValue.Builder
  public abstract static class Builder {
    abstract Builder setX(double x);
    abstract Builder setY(double y);

    public abstract Point2D build();
  }
}
