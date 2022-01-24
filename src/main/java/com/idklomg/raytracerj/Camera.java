package com.idklomg.raytracerj;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.idklomg.raytracerj.common.Randoms;
import com.idklomg.raytracerj.math.Point3D;
import com.idklomg.raytracerj.math.Ray;
import com.idklomg.raytracerj.math.Vector3D;

@AutoValue
abstract class Camera {

  public static Camera.Builder newBuilder() {
    return new AutoValue_Camera.Builder();
  }

  abstract Point3D getLookFrom();
  abstract Point3D getLookAt();
  abstract Vector3D getVerticalUp();
  abstract double getFieldOfViewInRadians();
  abstract double getAspectRatio();
  abstract double getAperture();
  abstract double getFocusDistance();


  @Memoized
  double getViewPortHeight() {
    double h = Math.tan(getFieldOfViewInRadians() / 2);
    return 2.0 * h;
  }

  @Memoized
  double getViewPortWidth() {
    return getAspectRatio() * getViewPortHeight();
  }

  @Memoized
  Vector3D getHorizontal() {
    return getU().scale(getViewPortWidth() * getFocusDistance());
  }

  @Memoized
  Vector3D getVertical() {
    return getV().scale(getViewPortHeight() * getFocusDistance());
  }

  @Memoized
  Point3D getOrigin() {
    return getLookFrom();
  }

  @Memoized
  Point3D getLowerLeftCorner() {
    return getOrigin()
        .subtract(getHorizontal().unscale(2))
        .subtract(getVertical().unscale(2))
        .subtract(getW().scale(getFocusDistance()))
        .toPoint3D();
  }

  @Memoized
  double getLensRadius() {
    return getAperture() / 2.0;
  }

  @Memoized
  Vector3D getW() {
    return getLookFrom().subtract(getLookAt()).toUnitVector();
  }

  @Memoized
  Vector3D getU() {
    return getVerticalUp().cross(getW()).toUnitVector();
  }

  @Memoized
  Vector3D getV() {
    return getW().cross(getU());
  }

  Ray getRay(double s, double t) {
    Vector3D rd = Randoms.randomVectorInUnitDisk().scale(getLensRadius());
    Vector3D offset = getU().scale(rd.getX()).add(getV().scale(rd.getY()));
    Point3D rayOrigin = getOrigin().add(offset);
    return Ray.create(
        rayOrigin,
        getLowerLeftCorner()
            .add(getHorizontal().scale(s))
            .add(getVertical().scale(t))
            .subtract(rayOrigin));
  }

  @AutoValue.Builder
  abstract static class Builder {
    Builder setFieldOfViewInDegrees(double fovDegrees) {
      return setFieldOfViewInRadians(Math.toRadians(fovDegrees));
    }
    abstract Builder setLookFrom(Point3D lookFrom);
    abstract Builder setLookAt(Point3D lookAt);
    abstract Builder setVerticalUp(Vector3D verticalUp);
    abstract Builder setFieldOfViewInRadians(double fovRadians);
    abstract Builder setAspectRatio(double aspectRatio);
    abstract Builder setAperture(double aperture);
    abstract Builder setFocusDistance(double focusDistance);
    abstract Camera build();
  }
}
