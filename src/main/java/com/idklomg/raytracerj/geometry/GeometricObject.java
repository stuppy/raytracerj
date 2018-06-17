package com.idklomg.raytracerj.geometry;

import java.util.Optional;

import com.idklomg.raytracerj.material.Color;
import com.idklomg.raytracerj.math.Ray;

public interface GeometricObject {

  static final double HIT_THRESHOLD = 10E-9;

  abstract Color getColor();
  abstract Optional<Double> hit(Ray ray);
}
