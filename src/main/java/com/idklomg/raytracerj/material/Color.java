package com.idklomg.raytracerj.material;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Color {

  public static Color.Builder newBuilder() {
    return new AutoValue_Color.Builder();
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

  public Color divide(int scalar) {
    return newBuilder()
        .setR((int) Math.round((double) getR() / scalar))
        .setG((int) Math.round((double) getG() / scalar))
        .setB((int) Math.round((double) getB() / scalar))
        .build();
  }

  public int toRgb() {
    return getR() << 16 | getG() << 8 | getB();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    abstract Builder setR(int r);
    abstract Builder setG(int g);
    abstract Builder setB(int b);

    public abstract Color build();
  }
}
