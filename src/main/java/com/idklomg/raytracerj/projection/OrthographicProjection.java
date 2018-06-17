package com.idklomg.raytracerj.projection;

import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;

public final class OrthographicProjection implements Projection {

  @Override
  public Ray createRay(Point3D origin) {
    return Ray.newBuilder()
        .setOrigin(origin)
        .setDirection(Vector3D.create(0, 0, -1))
        .build();
  }
}
