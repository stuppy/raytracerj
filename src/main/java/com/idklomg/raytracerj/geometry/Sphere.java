package com.idklomg.raytracerj.geometry;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.idklomg.raytracerj.material.Material;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;
import java.util.Optional;

@AutoValue
public abstract class Sphere implements GeometricObject {

  public static Sphere.Builder newBuilder() {
    return new AutoValue_Sphere.Builder();
  }

  abstract Point3D getCenter();
  abstract double getRadius();
  public abstract Material getMaterial();

  @Override
  public Optional<Hit> hit(Ray ray, double tMin, double tMax) {
    Vector3D fromCenter = ray.getOrigin().subtract(getCenter());
    double a = ray.getDirection().dot();
    double halfB = fromCenter.dot(ray.getDirection());
    double c = fromCenter.dot() - getRadiusSquared();

    double discriminant = Math.pow(halfB, 2) - (a * c);
    // If discriminant is < 0, sqrt won't return because there's not a hit.
    if (discriminant < 0) {
      return Optional.empty();
    }

    double sqrtd = Math.sqrt(discriminant);
    double t = (-halfB - sqrtd) / a;
    if (t < tMin || t > tMax) {
      t = (-halfB + sqrtd) / a;
      if (t < tMin || t > tMax) {
        return Optional.empty();
      }
    }
    Point3D p = ray.at(t);
    Vector3D outwardNormal = p.subtract(getCenter()).unscale(getRadius());
    boolean frontFace = ray.getDirection().dot(outwardNormal) < 0;
    Vector3D normal = frontFace ? outwardNormal : outwardNormal.negate();
    return Optional.of(
        Hit.newBuilder()
            .setT(t)
            .setPoint(p)
            .setNormal(normal)
            .setFrontFace(frontFace)
            .setMaterial(getMaterial())
            .build());
  }

  @Memoized
  double getRadiusSquared() {
    return Math.pow(getRadius(), 2);
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setCenter(Point3D center);
    public Builder setCenter(double x, double y, double z) {
      return setCenter(Point3D.create(x, y, z));
    }
    public abstract Builder setRadius(double radius);
    public abstract Builder setMaterial(Material material);

    public abstract Sphere build();
  }
}
