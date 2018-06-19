package com.idklomg.raytracerj.projection;

import com.idklomg.raytracerj.math.Point2D;
import com.idklomg.raytracerj.math.Ray;

public interface Projection {

  Ray createRay(Point2D point);
}
