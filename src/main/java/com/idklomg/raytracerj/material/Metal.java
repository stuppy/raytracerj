package com.idklomg.raytracerj.material;

import com.google.auto.value.AutoValue;
import com.idklomg.raytracerj.common.Randoms;
import com.idklomg.raytracerj.geometry.Hit;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;
import java.util.Optional;

@AutoValue
public abstract class Metal implements Material {

  public static Metal create(Color color, double fuzz) {
    return new AutoValue_Metal(color, fuzz);
  }

  abstract Color getColor();
  abstract double getFuzz();

  @Override
  public Optional<Reflection> scatter(Ray in, Hit hit) {
    Vector3D reflected = Vector3D.reflect(in.getDirection().toUnitVector(), hit.getNormal());
    Ray scattered =
        Ray.create(
            hit.getPoint(),
            reflected.add(Randoms.randomVectorInUnitSphere().scale(getFuzz())));
    return Optional.of(
        Reflection.newBuilder()
          .setAttenuation(getColor())
          .setScattered(scattered)
          .build());
  }
}
