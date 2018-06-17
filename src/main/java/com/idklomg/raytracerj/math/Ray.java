package com.idklomg.raytracerj.math;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Ray {

  public static Ray.Builder newBuilder() {
    return new AutoValue_Ray.Builder();
  }

  public abstract Point3D getOrigin();
  public abstract Vector3D getDirection();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setOrigin(Point3D origin);
    public abstract Builder setDirection(Vector3D direction);

    public abstract Ray build();
  }
}
