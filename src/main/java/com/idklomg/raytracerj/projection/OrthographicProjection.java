package com.idklomg.raytracerj.projection;

import com.idklomg.raytracerj.math.Point2D;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;

public final class OrthographicProjection implements Projection {

  @Override
  public Ray createRay(Point2D point) {
    return Ray.newBuilder()
        .setOrigin(Point3D.create(point.getX(), point.getY(), 0))
        .setDirection(Vector3D.create(0, 0, -1))
        .build();
  }
}
