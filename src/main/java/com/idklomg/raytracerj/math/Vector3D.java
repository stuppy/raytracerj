package com.idklomg.raytracerj.math;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;

@AutoValue
public abstract class Vector3D implements Thing3D {

  private static Vector3D.Builder newBuilder() {
    return new AutoValue_Vector3D.Builder();
  }

  public static Vector3D create(double x, double y, double z) {
    // TODO(stuppy): Always normalize()?
    return newBuilder().setX(x).setY(y).setZ(z).build();
  }


  public static Vector3D reflect(Vector3D uv, Vector3D normal) {
    return uv.subtract(normal.scale(2 * uv.dot(normal)));
  }

  public static Vector3D refract(Vector3D uv, Vector3D normal, double etaiOverEtat) {
    double cosTheta = Double.min(uv.negate().dot(normal), 1.0);
    Vector3D rOutPerp =  uv.add(normal.scale(cosTheta)).scale(etaiOverEtat);
    Vector3D rOutParallel = normal.scale(-1 * Math.sqrt(Math.abs(1.0 - rOutPerp.dot())));
    return rOutPerp.add(rOutParallel);
  }

  public abstract double getX();
  public abstract double getY();
  public abstract double getZ();

  public Vector3D cross(Vector3D other) {
    return Vector3D
        .create(
            getY() * other.getZ() - other.getY() * getZ(),
            getZ() * other.getX() - other.getZ() * getX(),
            getX() * other.getY() - other.getX() * getY())
        .toUnitVector();
  }

  public Vector3D scale(double scalar) {
    return Vector3D.create(getX() * scalar, getY() * scalar, getZ() * scalar);
  }

  public Vector3D unscale(double scalar) {
    return Vector3D.create(getX() / scalar, getY() / scalar, getZ() / scalar);
  }

  public Vector3D add(Thing3D other) {
    return Vector3D.create(getX() + other.getX(), getY() + other.getY(), getZ() + other.getZ());
  }

  public Vector3D subtract(Thing3D other) {
    return Vector3D.create(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ());
  }

  @Memoized
  public double length() {
    return Math.sqrt(dot());
  }

  public double dot(Vector3D other) {
    return getX() * other.getX() + getY() * other.getY() + getZ() * other.getZ();
  }

  @Memoized
  public double dot() {
    return dot(this);
  }

  public Vector3D toUnitVector() {
    return unscale(length());
  }

  public Point3D toPoint3D() {
    return Point3D.create(getX(), getY(), getZ());
  }

  public Vector3D negate() {
    return scale(-1);
  }

  public boolean nearZero() {
    // Return true if the vector is close to zero in all dimensions.
    double s = 1e-8;
    return (Math.abs(getX()) < s)
        && (Math.abs(getY()) < s)
        && (Math.abs(getZ()) < s);
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setX(double x);
    abstract Builder setY(double y);
    abstract Builder setZ(double z);

    public abstract Vector3D build();
  }
}
