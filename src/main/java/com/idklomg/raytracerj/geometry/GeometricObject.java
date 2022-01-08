package com.idklomg.raytracerj.geometry;

import com.idklomg.raytracerj.material.Color;
import com.idklomg.raytracerj.math.Ray;
import java.util.Optional;

public interface GeometricObject {

  static final double HIT_THRESHOLD = 10E-9;

  abstract Color getColor();
  abstract Optional<Double> hit(Ray ray);
}
