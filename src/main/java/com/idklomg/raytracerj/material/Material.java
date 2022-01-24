package com.idklomg.raytracerj.material;

import com.idklomg.raytracerj.geometry.Hit;
import com.idklomg.raytracerj.math.Ray;
import java.util.Optional;

public interface Material {

  Optional<Reflection> scatter(Ray in, Hit hit);
}
