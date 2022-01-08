package com.idklomg.raytracerj.sampling;

import com.idklomg.raytracerj.math.Point2D;
import java.util.Iterator;

public final class RegularSampler implements Sampler {

  private final int size;

  public RegularSampler(int size) {
    this.size = size;
  }

  @Override
  public Iterator<Point2D> getSamples(int x, int y) {
    return new Iterator<Point2D>() {

      private int row = 0;
      private int col = 0;

      @Override
      public boolean hasNext() {
        return row < size;
      }

      @Override
      public Point2D next() {
        Point2D next = Point2D.create(x + (row + 0.5) / size, y + (col + 0.5) / size);
        col += 1;
        if (col == size) {
          col = 0;
          row += 1;
        }
        return next;
      }
    };
  }
}
