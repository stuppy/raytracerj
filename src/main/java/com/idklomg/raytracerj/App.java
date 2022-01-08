package com.idklomg.raytracerj;

import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.idklomg.raytracerj.geometry.GeometricObject;
import com.idklomg.raytracerj.material.Color;
import com.idklomg.raytracerj.math.Point2D;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.projection.PerspectiveProjection;
import com.idklomg.raytracerj.projection.Projection;
import com.idklomg.raytracerj.sampling.RegularSampler;
import com.idklomg.raytracerj.sampling.Sampler;
import com.idklomg.raytracerj.scene.SceneFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.imageio.ImageIO;
import javax.inject.Inject;

public final class App {

  private static final String FILENAME = "Image.png";
  private static final int WIDTH = 800;
  private static final int HEIGHT = 800;
  private static final String IMAGE_FORMAT = "PNG";
  private static final Color BACKGROUND_COLOR = Color.create(0x99cc99);

  private final Projection projection;
  private final Sampler sampler;

  @Inject
  App(
      Projection projection,
      Sampler sampler) {
    this.projection = projection;
    this.sampler = sampler;
  }

  void run() {
    Stopwatch watch = Stopwatch.createStarted();

    BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    Iterable<GeometricObject> shapes = SceneFactory.generate(WIDTH, HEIGHT);

    AtomicLong count = new AtomicLong();
    int width = img.getWidth();
    int height = img.getHeight();
    int dimensions = width * height;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        // Start with black/no color.
        Color color = Color.create(0x000000);
        boolean hit = false;

        Iterator<Point2D> samples = sampler.getSamples((x - width / 2), (y - height / 2));
        int numSamples = 0;
        while (samples.hasNext()) {
          Point2D sample = samples.next();
          numSamples++;

          Ray ray = projection.createRay(sample);

          double closest = Double.MAX_VALUE;
          // Default to the background color unless a closer color is found.
          Color closestColor = BACKGROUND_COLOR;
          for (GeometricObject shape : shapes) {
            Optional<Double> t = shape.hit(ray);
            if (t.isPresent() && t.get() < closest) {
              closest = t.get();
              closestColor = shape.getColor();
              hit = true;
            }
          }
          if (closestColor != null) {
            color = color.add(closestColor);
          }
        }
        if (hit) {
          img.setRGB(x, y, color.divide(numSamples).toRgb());
        } else {
          img.setRGB(x, y, BACKGROUND_COLOR.toRgb());
        }
        long c = count.incrementAndGet();
        if (c % 1000 == 0 || c == dimensions) {
          System.out.printf("\r%d%%", Math.round(100.0 * c / dimensions));
        }
      }
    }

    System.out.println();

    try {
      ImageIO.write(img, IMAGE_FORMAT, new File(FILENAME));
    } catch (IOException e) {
      throw new RuntimeException("ImageIO.write failed", e);
    }

    System.out.printf("Finished in %s ms%n", watch.elapsed(TimeUnit.MILLISECONDS));
  }

  private static final class Module extends AbstractModule {

    @Override
    protected void configure() {
      PerspectiveProjection perspectiveProjection =
          PerspectiveProjection.create(
              Point3D.create(0, 0, 4000),
              Point3D.create(0, 0, 0),
              (HEIGHT / 2) / Math.tan(70.0 / 2));
      bind(Projection.class).toInstance(perspectiveProjection);
      bind(Sampler.class).toInstance(new RegularSampler(1));
    }
  }

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new Module());
    App app = injector.getInstance(App.class);
    app.run();
  }
}
