package com.idklomg.raytracerj.sampling;

import com.idklomg.raytracerj.math.Point2D;
import java.util.Iterator;

public interface Sampler {

  Iterator<Point2D> getSamples(int x, int y);
}
