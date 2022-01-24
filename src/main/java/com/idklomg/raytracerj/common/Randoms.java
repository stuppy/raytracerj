package com.idklomg.raytracerj.common;

import com.idklomg.raytracerj.math.Vector3D;
import java.util.concurrent.ThreadLocalRandom;

public final class Randoms {
  public static final double INFINITY = Double.MAX_VALUE;

  private static final ThreadLocalRandom random = ThreadLocalRandom.current();

  public static double randomDouble() {
    return random.nextDouble();
  }

  public static double randomDouble(double min, double max) {
    return min + (max - min) * randomDouble();
  }

  private static Vector3D randomVector(double min, double max) {
    return Vector3D.create(randomDouble(min, max), randomDouble(min, max), randomDouble(min, max));
  }

  public static Vector3D randomVectorInUnitSphere() {
    for (int attempt = 0; attempt < 1000; ++attempt) {
      Vector3D p = randomVector(-1, 1);
      if (p.dot() < 1) {
        return p;
      }
    }
    throw new RuntimeException("Failed to generate a random");
  }

  public static Vector3D randomUnitVector() {
    return randomVectorInUnitSphere().toUnitVector();
  }

  public static Vector3D randomVectorInHemisphere(Vector3D normal) {
    Vector3D inUnitSphere = randomVectorInUnitSphere();
    if (inUnitSphere.dot(normal) > 0.0) { // In the same hemisphere as the normal
      return inUnitSphere;
    } else {
      return inUnitSphere.negate();
    }
  }

  public static Vector3D randomVectorInUnitDisk() {
    for (int attempt = 0; attempt < 1000; ++attempt) {
      Vector3D p = Vector3D.create(randomDouble(-1, 1), randomDouble(-1, 1), 0);
      if (p.dot() < 1) {
        return p;
      }
    }
    throw new RuntimeException("Failed to generate a random");
  }
}
