package com.idklomg.raytracerj.projection;

import com.idklomg.raytracerj.math.Point2D;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;

public final class PerspectiveProjection implements Projection {

  private static final Vector3D UP = Vector3D.create(0, 1.0, 0);

  private final Point3D eye;
  private final Point3D lookat;
  private final double distance;
  private final Vector3D u;
  private final Vector3D v;
  private final Vector3D w;

  public static PerspectiveProjection create(Point3D eye, Point3D lookat, double distance) {
    Vector3D w = eye.subtract(lookat).normalize();
    Vector3D u = UP.cross(w).normalize();
    Vector3D v = w.cross(u).normalize();
    return new PerspectiveProjection(eye, lookat, distance, u, v, w);
  }

  PerspectiveProjection(
      Point3D eye,
      Point3D lookat,
      double distance,
      Vector3D u,
      Vector3D v,
      Vector3D w) {
    this.eye = eye;
    this.lookat = lookat;
    this.distance = distance;
    this.u = u;
    this.v = v;
    this.w = w;
  }

  @Override
  public Ray createRay(Point2D point) {
    Vector3D uS = u.scale(point.getX());
    Vector3D vS = v.scale(point.getY());
    Vector3D wS = w.scale(-distance);
    Vector3D direction = uS.add(vS).add(wS).normalize();
    return Ray.newBuilder()
        .setOrigin(eye)
        .setDirection(direction)
        .build();
  }
}
