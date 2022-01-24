package com.idklomg.raytracerj.material;

import com.google.auto.value.AutoValue;
import com.idklomg.raytracerj.common.Randoms;
import com.idklomg.raytracerj.geometry.Hit;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;
import java.util.Optional;

@AutoValue
public abstract class Dielectric implements Material {

  public static Dielectric create(double indexOfRefraction) {
    return new AutoValue_Dielectric(indexOfRefraction);
  }

  abstract double getIndexOfRefraction();

  @Override
  public Optional<Reflection> scatter(Ray in, Hit hit) {
    double refractionRatio =
        hit.isFrontFace() ? (1.0 / getIndexOfRefraction()) : getIndexOfRefraction();
    Vector3D unitDirection = in.getDirection().toUnitVector();
    double cosTheta = Double.min(unitDirection.negate().dot(hit.getNormal()), 1.0);
    double sinTheta = Math.sqrt(1.0 - Math.pow(cosTheta, 2));
    boolean cannotRefract = refractionRatio * sinTheta > 1.0;
    Vector3D direction;
    if (cannotRefract || reflectance(cosTheta, refractionRatio) > Randoms.randomDouble()) {
      direction = Vector3D.reflect(unitDirection, hit.getNormal());
    } else {
      direction = Vector3D.refract(unitDirection, hit.getNormal(), refractionRatio);
    }
    Ray scattered = Ray.create(hit.getPoint(), direction);
    return Optional.of(
        Reflection.newBuilder()
          .setAttenuation(Color.WHITE)
          .setScattered(scattered)
          .build());
  }

  private static double reflectance(double cosine, double refIdx) {
      // Use Schlick's approximation for reflectance.
    double r0 = Math.pow((1 - refIdx) / (1 + refIdx), 2);
    return r0 + (1 - r0) * Math.pow(1 - cosine, 5);
  }
}
