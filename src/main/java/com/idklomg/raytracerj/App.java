package com.idklomg.raytracerj;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.idklomg.raytracerj.geometry.GeometricObject;
import com.idklomg.raytracerj.geometry.Plane;
import com.idklomg.raytracerj.geometry.Sphere;
import com.idklomg.raytracerj.material.Color;
import com.idklomg.raytracerj.math.Normal;
import com.idklomg.raytracerj.math.Point2D;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.projection.OrthographicProjection;
import com.idklomg.raytracerj.projection.Projection;
import com.idklomg.raytracerj.sampling.RegularSampler;
import com.idklomg.raytracerj.sampling.Sampler;

public final class App {

  private static final String FILENAME = "Image.png";
  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;
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

    Random random = new Random();

    List<GeometricObject> shapes =
        new ArrayList<>(
          ImmutableList.of(
              Sphere.newBuilder()
                  .setCenter(Point3D.create(0, 0, -100))
                  .setRadius(50)
                  .setColor(Color.create(0xFF0000))
                  .build(),
              Sphere.newBuilder()
                  .setCenter(Point3D.create(-200, 0, -100))
                  .setRadius(100)
                  .setColor(Color.create(0x00FF00))
                  .build(),
              Sphere.newBuilder()
                  .setCenter(Point3D.create(100, -70, -220))
                  .setRadius(50)
                  .setColor(Color.create(0x0000FF))
                  .build(),
              Sphere.newBuilder()
                  .setCenter(Point3D.create(150, -20, -160))
                  .setRadius(50)
                  .setColor(Color.create(0x00FFFF))
                  .build(),
              Sphere.newBuilder()
                  .setCenter(Point3D.create(180, 20, -100))
                  .setRadius(50)
                  .setColor(Color.create(0xFFFF00))
                  .build(),
              Plane.newBuilder()
                  .setPoint(Point3D.create(0, 50, -100))
                  .setNormal(Normal.create(0.1, 1, 0.5))
                  .setColor(Color.create(0xFFFFFF))
                  .build()));
    for (int i = 0; i < 20; i++) {
      shapes.add(
          Sphere.newBuilder()
              .setCenter(
                  Point3D.create(
                      random.nextInt(WIDTH) - WIDTH / 2,
                      random.nextInt(HEIGHT) - HEIGHT / 2,
                      -1 * random.nextInt(1000) - 50))
              .setRadius(random.nextInt(75))
              .setColor(Color.create(random.nextInt(0x1000000)))
              .build());
    }

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

          Ray ray = projection.createRay(Point3D.create(sample.getX(), sample.getY(), 0));

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
      bind(Projection.class).to(OrthographicProjection.class);
      bind(Sampler.class).toInstance(new RegularSampler(8));
    }
  }

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new Module());
    App app = injector.getInstance(App.class);
    app.run();
  }
}
