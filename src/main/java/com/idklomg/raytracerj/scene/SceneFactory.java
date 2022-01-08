package com.idklomg.raytracerj.scene;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.idklomg.raytracerj.geometry.GeometricObject;
import com.idklomg.raytracerj.geometry.Plane;
import com.idklomg.raytracerj.geometry.Sphere;
import com.idklomg.raytracerj.material.Color;
import com.idklomg.raytracerj.math.Normal;
import com.idklomg.raytracerj.math.Point3D;

public final class SceneFactory {

  public static ImmutableList<GeometricObject> generate(int width, int height) {
    Random random = new Random();
    ImmutableList.Builder<GeometricObject> shapes = new ImmutableList.Builder<>();
    shapes.add(
        Sphere.newBuilder()
            .setCenter(Point3D.create(0, 0, -2000))
            .setRadius(300)
            .setColor(Color.create(0xFF0000))
            .build(),
        Sphere.newBuilder()
            .setCenter(Point3D.create(-1300, 0, -2000))
            .setRadius(400)
            .setColor(Color.create(0x00FF00))
            .build(),
        Sphere.newBuilder()
            .setCenter(Point3D.create(1000, 1000, -2000))
            .setRadius(400)
            .setColor(Color.create(0x0000FF))
            .build(),
        Sphere.newBuilder()
            .setCenter(Point3D.create(-1000, -1000, -2700))
            .setRadius(700)
            .setColor(Color.create(0xff6600))
            .build(),
        Sphere.newBuilder()
            .setCenter(Point3D.create(1000, -1000, -2000))
            .setRadius(800)
            .setColor(Color.create(0xff66ff))
            .build(),
        Sphere.newBuilder()
            .setCenter(Point3D.create(150, -20, -1600))
            .setRadius(50)
            .setColor(Color.create(0x00FFFF))
            .build(),
        Sphere.newBuilder()
            .setCenter(Point3D.create(180, 20, -1000))
            .setRadius(50)
            .setColor(Color.create(0xFFFF00))
            .build(),
        Plane.newBuilder()
            .setPoint(Point3D.create(0, -50, -5000))
            .setNormal(Normal.create(0.2, 1, 0.5))
            .setColor(Color.create(0xFFFFFF))
            .build());
    for (int i = 0; i < 3; i++) {
      shapes.add(
          Sphere.newBuilder()
              .setCenter(
                  Point3D.create(
                      random.nextInt(width) - width / 2,
                      random.nextInt(height) - height / 2,
                      -1 * random.nextInt(10000) - 50))
              .setRadius(random.nextInt(1000))
              .setColor(Color.create(random.nextInt(0x1000000)))
              .build());
    }
    return shapes.build();
  }
}
