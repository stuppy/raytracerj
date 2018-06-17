package com.idklomg.raytracerj.geometry;

import java.util.Optional;

import com.google.auto.value.AutoValue;
import com.idklomg.raytracerj.material.Color;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;

@AutoValue
public abstract class Sphere implements GeometricObject {

  public static Sphere.Builder newBuilder() {
    return new AutoValue_Sphere.Builder();
  }

  abstract Point3D getCenter();
  abstract double getRadius();
  public abstract Color getColor();

  @Override
  public Optional<Double> hit(Ray ray) {
    double a = ray.getDirection().dot(ray.getDirection());
    double b = 2 * ray.getOrigin().subtract(getCenter()).dot(ray.getDirection());
    double c =
        ray.getOrigin().subtract(getCenter()).dot(ray.getOrigin().subtract(getCenter()))
          - (getRadius() * getRadius());

    double discriminant = (b * b) - (4 * a * c);
    // If discriminant is < 0, sqrt won't return because there's not a hit.
    if (discriminant < 0) {
      return Optional.empty();
    }

    // Skipping + because we only want the *closest* hit.
    double t = (-b - Math.sqrt(discriminant)) / (2 * a);
    if (t > HIT_THRESHOLD) {
      return Optional.of(t);
    } else {
      return Optional.empty();
    }
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setCenter(Point3D center);
    public abstract Builder setRadius(double radius);
    public abstract Builder setColor(Color color);

    public abstract Sphere build();
  }
}
