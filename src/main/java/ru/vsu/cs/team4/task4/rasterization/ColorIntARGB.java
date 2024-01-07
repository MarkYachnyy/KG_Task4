package ru.vsu.cs.team4.task4.rasterization;

import javafx.scene.paint.Color;

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
    public ColorIntARGB(Color color) {
        int b = (int)color.getBlue() * 255;
        int r = (int)color.getRed() * 255;
        int g = (int)color.getGreen() * 255;
        int a = (int)color.getOpacity() * 255;

        this.blue = b;
        this.red = r;
        this.green = g;
        this.alpha = a;

    }
}
