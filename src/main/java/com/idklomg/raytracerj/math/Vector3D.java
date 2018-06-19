package com.idklomg.raytracerj.math;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Vector3D implements Thing3D {

  private static Vector3D.Builder newBuilder() {
    return new AutoValue_Vector3D.Builder();
  }

  public static Vector3D create(double x, double y, double z) {
    // TODO(stuppy): Always normalize()?
    return newBuilder().setX(x).setY(y).setZ(z).build();
  }

  public abstract double getX();
  public abstract double getY();
  public abstract double getZ();

  public Vector3D normalize() {
    double x = getX();
    double y = getY();
    double z = getZ();
    double magnitude = Math.sqrt(x * x + y * y + z * z);
    return create(x / magnitude, y / magnitude, z / magnitude);
  }

  public Vector3D cross(Vector3D other) {
    return Vector3D
        .create(
            getY() * other.getZ() - other.getY() * getZ(),
            getZ() * other.getX() - other.getZ() * getX(),
            getX() * other.getY() - other.getX() * getY())
        .normalize();
  }

  public Vector3D scale(double scalar) {
    return Vector3D.create(getX() * scalar, getY() * scalar, getZ() * scalar);
  }

  public Vector3D add(Vector3D other) {
    return Vector3D.create(getX() + other.getX(), getY() + other.getY(), getZ() + other.getZ());
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setX(double x);
    abstract Builder setY(double y);
    abstract Builder setZ(double z);

    public abstract Vector3D build();
  }
}
