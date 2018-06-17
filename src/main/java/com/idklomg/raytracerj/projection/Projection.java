package com.idklomg.raytracerj.projection;

import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;

public interface Projection {

  Ray createRay(Point3D origin);
}
