package ru.vsu.cs.team4.task4.math;


import ru.vsu.cs.team4.task4.math.vector.Vector3f;

public class Point2f {
    private int x;
    private int y;

    public Point2f(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
