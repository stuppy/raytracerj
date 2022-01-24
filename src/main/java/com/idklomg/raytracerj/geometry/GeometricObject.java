package com.idklomg.raytracerj.geometry;

import com.idklomg.raytracerj.math.Ray;
import java.util.Optional;

public interface GeometricObject {

  static final double HIT_THRESHOLD = 0.001;

  abstract Optional<Hit> hit(Ray ray, double tMin, double tMax);
}
