package com.idklomg.raytracerj.math;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Point3D implements Thing3D {

  public static Point3D.Builder newBuilder() {
    return new AutoValue_Point3D.Builder();
  }

  public static Point3D create(double x, double y, double z) {
    return newBuilder().setX(x).setY(y).setZ(z).build();
  }

  public abstract double getX();
  public abstract double getY();
  public abstract double getZ();

  public Point3D add(Point3D point) {
    return newBuilder()
        .setX(getX() + point.getX())
        .setY(getY() + point.getY())
        .setZ(getZ() + point.getZ())
        .build();
  }

  public Point3D subtract(Point3D point) {
    return newBuilder()
        .setX(getX() - point.getX())
        .setY(getY() - point.getY())
        .setZ(getZ() - point.getZ())
        .build();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    abstract Builder setX(double x);
    abstract Builder setY(double y);
    abstract Builder setZ(double z);

    public abstract Point3D build();
  }
}
