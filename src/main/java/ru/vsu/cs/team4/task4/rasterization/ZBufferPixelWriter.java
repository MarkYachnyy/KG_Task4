package ru.vsu.cs.team4.task4.rasterization;

public interface ZBufferPixelWriter {
    void setRGB(int x, int y, float z, int color);
}
