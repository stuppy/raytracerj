package com.idklomg.raytracerj.material;

import com.google.auto.value.AutoValue;
import com.idklomg.raytracerj.common.Randoms;
import com.idklomg.raytracerj.geometry.Hit;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;
import java.util.Optional;

@AutoValue
public abstract class Lambertian implements Material {

  public static Lambertian color(Color color) {
    return new AutoValue_Lambertian(color);
  }

  abstract Color getColor();

  @Override
  public Optional<Reflection> scatter(Ray in, Hit hit) {
    Vector3D scatterDirection = Randoms.randomVectorInHemisphere(hit.getNormal());
    // Catch degenerate scatter direction
    if (scatterDirection.nearZero()) {
      scatterDirection = hit.getNormal();
    }
    return Optional.of(
        Reflection.newBuilder()
          .setAttenuation(getColor())
          .setScattered(Ray.create(hit.getPoint(), scatterDirection))
          .build());
  }
}
