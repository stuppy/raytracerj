package com.idklomg.raytracerj.material;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.idklomg.raytracerj.common.Randoms;

@AutoValue
public abstract class Color {

  public static final Color BLACK = Color.create(0);
  public static final Color WHITE = Color.create(0xFFFFFF);

  public static Color.Builder newBuilder() {
    return new AutoValue_Color.Builder();
  }

  // 255 = 1
  private static final double M = 255.9999999;
  private static final double M2 = Math.pow(M, 2);

  public static Color random() {
    return Color.create(Randoms.randomDouble(), Randoms.randomDouble(), Randoms.randomDouble());
  }

  public static Color random(double min, double max) {
    return Color.create(
        Randoms.randomDouble(min, max),
        Randoms.randomDouble(min, max),
        Randoms.randomDouble(min, max));
  }

  public static Color create(int rgb) {
    return newBuilder()
        .setR(rgb >> 16)
        .setG((rgb >> 8) & 0xFF)
        .setB(rgb & 0xFF)
        .build();
  }

  public static Color create(int r, int g, int b) {
    return newBuilder().setR(r).setG(g).setB(b).build();
  }

  public static Color create(double r, double g, double b) {
    return newBuilder()
        .setR((int) (r * M))
        .setG((int) (g * M))
        .setB((int) (b * M))
        .build();
  }

  abstract int getR();
  abstract int getG();
  abstract int getB();

  public Color add(Color color) {
    return newBuilder()
        .setR(getR() + color.getR())
        .setG(getG() + color.getG())
        .setB(getB() + color.getB())
        .build();
  }

  public Color multiply(double scalar) {
    return newBuilder()
        .setR((int) (getR() * scalar))
        .setG((int) (getG() * scalar))
        .setB((int) (getB() * scalar))
        .build();
  }

  public Color darkenBy(Color color) {
    return create(
        getR() * color.getR() / M2,
        getG() * color.getG() / M2,
        getB() * color.getB() / M2);
  }

  public Color darken() {
    return darkenBy(this);
  }

  public Color divide(double scalar) {
    return newBuilder()
        .setR((int) (getR() / scalar))
        .setG((int) (getG() / scalar))
        .setB((int) (getB() / scalar))
        .build();
  }

  @Memoized
  public Color gamma2() {
    return create(
        Math.sqrt((double) clamp(getR()) / M),
        Math.sqrt((double) clamp(getG()) / M),
        Math.sqrt((double) clamp(getB()) / M));
  }

  public int toRgb() {
    return clamp(getR()) << 16 | clamp(getG()) << 8 | clamp(getB());
  }

  private static int clamp(int x) {
    if (x < 0) {
      return 0;
    }
    if (x > 255) {
      return 255;
    }
    return x;
  }

  @AutoValue.Builder
  public abstract static class Builder {
    abstract Builder setR(int r);
    abstract Builder setG(int g);
    abstract Builder setB(int b);

    public abstract Color build();
  }
}
