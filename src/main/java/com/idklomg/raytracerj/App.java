package com.idklomg.raytracerj;

import com.google.auto.value.AutoValue;
import com.google.common.base.Stopwatch;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.idklomg.raytracerj.common.Randoms;
import com.idklomg.raytracerj.geometry.GeometricObject;
import com.idklomg.raytracerj.geometry.Hit;
import com.idklomg.raytracerj.geometry.Sphere;
import com.idklomg.raytracerj.material.Color;
import com.idklomg.raytracerj.material.Dielectric;
import com.idklomg.raytracerj.material.Lambertian;
import com.idklomg.raytracerj.material.Material;
import com.idklomg.raytracerj.material.Metal;
import com.idklomg.raytracerj.material.Reflection;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

public final class App {

  private static final String FILENAME = "Image.png";
  private static final double FIELD_OF_VIEW_IN_DEGREES = 20;
  private static final double ASPECT_RATIO = 3.0 / 2.0;
  private static final Point3D LOOK_FROM = Point3D.create(13, 2, 3);
  private static final Point3D LOOK_AT = Point3D.create(0, 0, 0);
  private static final Vector3D VERTICAL_UP = Vector3D.create(0, 1, 0);
  private static final double DIST_TO_FOCUS = 10.0;
  private static final double APERTURE = 0.1;
  private static final int IMAGE_WIDTH = 200;
  private static final int IMAGE_HEIGHT = (int) (IMAGE_WIDTH / ASPECT_RATIO);
  private static final String IMAGE_FORMAT = "PNG";
  private static final Color WHITE = Color.create(0xFFFFFF);
  private static final Color SAMPLE = Color.create(0.5, 0.7, 1.0);
  private static final int SAMPLES_PER_PIXEL = 500;
  private static final int MAX_DEPTH = 50;

  App() {
  }

  @AutoValue
  abstract static class Todo {
    abstract int getI();
    abstract int getJ();
  }

  void run() {
    Camera camera =
        Camera.newBuilder()
            .setLookFrom(LOOK_FROM)
            .setLookAt(LOOK_AT)
            .setVerticalUp(VERTICAL_UP)
            .setFieldOfViewInDegrees(FIELD_OF_VIEW_IN_DEGREES)
            .setAspectRatio(ASPECT_RATIO)
            .setAperture(APERTURE)
            .setFocusDistance(DIST_TO_FOCUS)
            .build();

    BufferedImage img = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    List<Todo> todos = new ArrayList<>();
    for (int j = IMAGE_HEIGHT - 1; j >= 0; --j) {
      for (int i = 0; i < IMAGE_WIDTH; ++i) {
        todos.add(new AutoValue_App_Todo(i, j));
      }
    }
    int count = todos.size();
    System.out.printf("Processing %d scanpoints%n", count);
    Stopwatch sw = Stopwatch.createStarted();
    int n = 1_000;
    AtomicInteger doneCount = new AtomicInteger();
    todos.parallelStream().forEach(
        todo -> {
          int i = todo.getI();
          int j = todo.getJ();
          Color color = Color.BLACK;
          for (int s = 0; s < SAMPLES_PER_PIXEL; ++s) {
            double u = ((double) i + Randoms.randomDouble()) / (IMAGE_WIDTH - 1);
            double v = ((double) j + Randoms.randomDouble()) / (IMAGE_HEIGHT - 1);
            Ray ray = camera.getRay(u, v);
            Color sample = rayColor(ray, MAX_DEPTH);
            color = color.add(sample);
          }
          img.setRGB(i, IMAGE_HEIGHT - 1 - j, color.divide(SAMPLES_PER_PIXEL).gamma2().toRgb());
          int done = doneCount.incrementAndGet();
          if (done % n == 0) {
            synchronized (sw) {
              double msForN = sw.elapsed(TimeUnit.MILLISECONDS);
              sw.reset().start();
              int togo = count - done;
              long msForTogo = (long) ((double) togo * msForN / n);
              Duration estimate = Duration.ofMillis(msForTogo);
              System.out.printf("Scanpoints remaining: %d (~%s)%n", togo, estimate);
            }
          }
        });
//    for (int j = IMAGE_HEIGHT - 1; j >= 0; --j) {
//      for (int i = 0; i < IMAGE_WIDTH; ++i) {
//        Color color = Color.BLACK;
//        for (int s = 0; s < SAMPLES_PER_PIXEL; ++s) {
//          double u = ((double) i + Randoms.randomDouble()) / (IMAGE_WIDTH - 1);
//          double v = ((double) j + Randoms.randomDouble()) / (IMAGE_HEIGHT - 1);
//          Ray ray = camera.getRay(u, v);
//          Color sample = rayColor(ray, MAX_DEPTH);
//          color = color.add(sample);
//        }
//        img.setRGB(i, IMAGE_HEIGHT - 1 - j, color.divide(SAMPLES_PER_PIXEL).gamma2().toRgb());
//      }
//      System.out.printf("Scanlines remaining: %s%n", j);
//    }
    System.out.printf("Writing to file: %s%n", FILENAME);
    try {
      ImageIO.write(img, IMAGE_FORMAT, new File(FILENAME));
    } catch (IOException e) {
      throw new RuntimeException("ImageIO.write failed", e);
    }
  }

  private static final World world;
  static {
    Material groundMaterial = Lambertian.color(Color.create(0.5, 0.5, 0.5));
    List<GeometricObject> shapes = new ArrayList<>();
    shapes.add(
        Sphere.newBuilder()
            .setCenter(0,-1000,0)
            .setRadius(1000.0)
            .setMaterial(groundMaterial)
            .build());
    Point3D checkPoint = Point3D.create(4, 0.2, 0);
    for (int a = -11; a < 11; a++) {
      for (int b = -11; b < 11; b++) {
        double chooseMat = Randoms.randomDouble();
        Point3D center =
            Point3D.create(
                a + 0.9 * Randoms.randomDouble(),
                0.2,
                b + 0.9 * Randoms.randomDouble());
        if (center.subtract(checkPoint).length() > 0.9) {
          Material sphereMaterial;
          if (chooseMat < 0.8) {
            // diffuse
            Color albedo = Color.random().darken();
            sphereMaterial = Lambertian.color(albedo);
          } else if (chooseMat < 0.95) {
            // metal
            Color albedo = Color.random(0.5, 1);
            double fuzz = Randoms.randomDouble(0, 0.5);
            sphereMaterial = Metal.create(albedo, fuzz);
          } else {
            // glass
            sphereMaterial = Dielectric.create(1.5);
          }
          shapes.add(
              Sphere.newBuilder()
                  .setCenter(center)
                  .setRadius(0.2)
                  .setMaterial(sphereMaterial)
                  .build());
        }
      }
    }
    shapes.add(
        Sphere.newBuilder()
            .setCenter(0, 1, 0)
            .setRadius(1.0)
            .setMaterial(Dielectric.create(1.5))
            .build());
    shapes.add(
        Sphere.newBuilder()
            .setCenter(-4, 1, 0)
            .setRadius(1.0)
            .setMaterial(Lambertian.color(Color.create(0.4, 0.2, 0.1)))
            .build());
    shapes.add(
        Sphere.newBuilder()
            .setCenter(4, 1, 0)
            .setRadius(1.0)
            .setMaterial(Metal.create(Color.create(0.7, 0.6, 0.5), 0))
            .build());
    world = World.newBuilder().setShapes(shapes).build();
  }

  private Color rayColor(Ray ray, int depth) {
    if (depth <= 0) {
      return Color.BLACK;
    }

    Optional<Hit> optClosest = world.hit(ray);
    if (optClosest.isPresent()) {
      Hit hit = optClosest.get();
      Optional<Reflection> optReflection = hit.getMaterial().scatter(ray, hit);
      return optReflection
          .map(
              reflection ->
                reflection
                    .getAttenuation()
                    .darkenBy(rayColor(reflection.getScattered(), depth - 1)))
          .orElse(Color.BLACK);
    }
    return background(ray);
  }

  @SuppressWarnings("unused")
  private Color background(Ray ray) {
    if (false) {
      return quadrants(ray.getDirection().getX(), ray.getDirection().getY());
    }
    Vector3D unitDirection = ray.getDirection().toUnitVector();
    double t = 0.5 * (unitDirection.getY() + 1.0);
    return WHITE.multiply(1.0 - t).add(SAMPLE.multiply(t));
  }

  private Color quadrants(double x, double y) {
    if (x >= 0) {
      if (y >= 0) {
        return Color.create(0xFF0000);
      } else {
        return Color.create(0x00FF00);
      }
    } else if (y >= 0) {
      return Color.create(0x0000FF);
    } else {
      return Color.create(0xFFFF00);
    }
  }

  private static final class Module extends AbstractModule {
  }

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new Module());
    App app = injector.getInstance(App.class);
    app.run();
  }
}
