package com.idklomg.raytracerj.geometry;

import com.google.auto.value.AutoValue;
import com.idklomg.raytracerj.material.Color;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;
import java.util.Optional;

@AutoValue
public abstract class Plane implements GeometricObject {

  public static Plane.Builder newBuilder() {
    return new AutoValue_Plane.Builder();
  }

  abstract Point3D getPoint();
  abstract Vector3D getNormal();
  public abstract Color getColor();

  @Override
  public Optional<Hit> hit(Ray ray, double tMin, double tMax) {
    double t =
        getPoint().subtract(ray.getOrigin()).dot(getNormal()) / ray.getDirection().dot(getNormal());
    if (t < tMin || t > tMax) {
      return Optional.empty();
    }
    return Optional.of(
        Hit.newBuilder()
            .setT(t)
            .setPoint(ray.at(t))
            .setNormal(getNormal()) // maybe?
            .setFrontFace(true) // maybe?
            .build());
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setPoint(Point3D point);
    public abstract Builder setNormal(Vector3D normal);
    public abstract Builder setColor(Color color);

    public abstract Plane build();
  }
}
