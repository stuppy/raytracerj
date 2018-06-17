package com.idklomg.raytracerj.geometry;

import java.util.Optional;

import com.google.auto.value.AutoValue;
import com.idklomg.raytracerj.material.Color;
import com.idklomg.raytracerj.math.Normal;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;

@AutoValue
public abstract class Plane implements GeometricObject {

  public static Plane.Builder newBuilder() {
    return new AutoValue_Plane.Builder();
  }

  abstract Point3D getPoint();
  abstract Normal getNormal();
  public abstract Color getColor();

  @Override
  public Optional<Double> hit(Ray ray) {
    double t =
        getPoint().subtract(ray.getOrigin()).dot(getNormal()) / ray.getDirection().dot(getNormal());
    if (t > HIT_THRESHOLD) {
      return Optional.of(t);
    } else {
      return Optional.empty();
    }
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setPoint(Point3D point);
    public abstract Builder setNormal(Normal normal);
    public abstract Builder setColor(Color color);

    public abstract Plane build();
  }
}
