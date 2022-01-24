package com.idklomg.raytracerj.math;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Ray {

  public static Ray create(Point3D origin, Vector3D direction) {
    return new AutoValue_Ray.Builder()
        .setOrigin(origin)
        .setDirection(direction)
        .build();
  }

  public abstract Point3D getOrigin();
  public abstract Vector3D getDirection();

  public Point3D at(double t) {
    return getOrigin().add(getDirection().scale(t).toPoint3D());
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setOrigin(Point3D origin);
    public abstract Builder setDirection(Vector3D direction);

    public abstract Ray build();
  }
}
