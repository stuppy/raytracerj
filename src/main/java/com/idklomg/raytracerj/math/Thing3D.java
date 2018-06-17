package com.idklomg.raytracerj.math;

interface Thing3D {
  double getX();
  double getY();
  double getZ();

  default double dot(Thing3D other) {
    return getX() * other.getX() + getY() * other.getY() + getZ() * other.getZ();
  }
}
