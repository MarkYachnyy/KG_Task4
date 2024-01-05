package ru.vsu.cs.team4.task4.rasterization;

import ru.vsu.cs.team4.task4.math.vector.Vector3f;

public class PolygonVertex {
    private final int x, y;
    private final float z, tx, ty;
    private final Vector3f normal;

    public PolygonVertex(int x, int y, float z, float tx, float ty, Vector3f normal) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tx = tx;
        this.ty = ty;
        this.normal = normal;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getTx() {
        return tx;
    }

    public float getTy() {
        return ty;
    }

    public Vector3f getNormal() {
        return normal;
    }
}
