package ru.vsu.cs.team4.task4.rasterization;

public class ColorIntARGB {
    int alpha;
    int red;
    int green;
    int blue;

    public ColorIntARGB(int alpha, int red, int green, int blue) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int toInt(){
        return alpha << 24 | red << 16 | green << 8 | blue;
    }
}
