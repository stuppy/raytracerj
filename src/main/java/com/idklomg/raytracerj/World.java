package com.idklomg.raytracerj;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.idklomg.raytracerj.common.Randoms;
import com.idklomg.raytracerj.geometry.GeometricObject;
import com.idklomg.raytracerj.geometry.Hit;
import com.idklomg.raytracerj.math.Ray;
import java.util.Optional;

@AutoValue
abstract class World {

  static World.Builder newBuilder() {
    return new AutoValue_World.Builder()
        .setShapes(ImmutableList.of());
  }

  abstract ImmutableList<GeometricObject> getShapes();

  Optional<Hit> hit(Ray ray) {
    return hit(ray, GeometricObject.HIT_THRESHOLD, Randoms.INFINITY);
  }

  Optional<Hit> hit(Ray ray, double tMin, double tMax) {
    Optional<Hit> closest = Optional.empty();
    for (GeometricObject shape : getShapes()) {
      Optional<Hit> hit = shape.hit(ray, tMin, closest.map(Hit::getT).orElse(tMax));
      if (hit.isPresent()) {
        closest = hit;
      }
    }
    return closest;
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setShapes(Iterable<GeometricObject> shapes);

    public Builder setShapes(GeometricObject shape, GeometricObject... otherShapes) {
      return setShapes(
          ImmutableList.<GeometricObject>builder().add(shape).add(otherShapes).build());
    }

    public abstract World build();
  }
}
