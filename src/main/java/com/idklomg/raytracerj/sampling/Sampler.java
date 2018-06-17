package com.idklomg.raytracerj.sampling;

import java.util.Iterator;

import com.idklomg.raytracerj.math.Point2D;

public interface Sampler {

  Iterator<Point2D> getSamples(int x, int y);
}
