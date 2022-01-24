package com.idklomg.raytracerj.geometry;

import com.google.auto.value.AutoValue;
import com.idklomg.raytracerj.material.Material;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Vector3D;

@AutoValue
public abstract class Hit {

  public static Hit.Builder newBuilder() {
    return new AutoValue_Hit.Builder();
  }

  public abstract double getT();
  public abstract Point3D getPoint();
  public abstract Vector3D getNormal();
  public abstract boolean isFrontFace();
  public abstract Material getMaterial();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setT(double t);
    public abstract Builder setPoint(Point3D point);
    public abstract Builder setNormal(Vector3D normal);
    public abstract Builder setFrontFace(boolean frontFace);
    public abstract Builder setMaterial(Material material);

    public abstract Hit build();
  }
}
