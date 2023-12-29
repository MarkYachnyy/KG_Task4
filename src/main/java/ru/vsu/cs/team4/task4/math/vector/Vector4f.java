package main.vector;

import java.nio.file.Paths;

public class Vector4f implements Vector<Vector4f>{

    private float x;
    private float y;
    private float z;
    private float w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vector3f v) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
        this.w = 1;
    }

    public Vector4f(float[] values) {
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
        this.w = values[3];
    }

    public Vector4f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.w = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getW() {
        return w;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setW(float w) {
        this.w = w;
    }
    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }


    @Override
    public void sum(Vector4f v) {
        this.x += v.getX();
        this.y += v.getY();
        this.z += v.getZ();
        this.w += v.getW();
    }

    public static Vector4f sum(Vector4f v1, Vector4f v2) {
        return new Vector4f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z, v1.w + v2.w);
    }


    @Override
    public void sum(float num) {
        this.x += num;
        this.y += num;
        this.z += num;
        this.w += num;
    }

    public static Vector4f sum(Vector4f v, float num) {
        return new Vector4f(v.x + num, v.y + num, v.z + num, v.w + num);
    }

    @Override
    public void sub(Vector4f v) {
        this.x -= v.getX();
        this.y -= v.getY();
        this.z -= v.getZ();
        this.w -= v.getW();
    }

    public static Vector4f sub(Vector4f v1, Vector4f v2) {
        return new Vector4f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z, v1.w - v2.w);
    }

    @Override
    public void sub(float num) {
        this.x -= num;
        this.y -= num;
        this.z -= num;
        this.w -= num;
    }

    public static Vector4f sub(Vector4f v, float num) {
        return new Vector4f(v.x - num, v.y - num, v.z - num, v.w - num);
    }

    @Override
    public void mul(float num) {
        this.x *= num;
        this.y *= num;
        this.z *= num;
        this.w *= num;
    }

    public static Vector4f mul(Vector4f v, float num) {
        return new Vector4f(v.x * num, v.y * num, v.z * num, v.w * num);
    }

    @Override
    public void div(float num) {
        this.x /= num;
        this.y /= num;
        this.w /= num;
        this.z /= num;
    }

    public static Vector4f div(Vector4f v, float num) {
        return new Vector4f(v.x / num, v.y / num, v.z / num, v.w / num);
    }

    @Override
    public float len() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }

    @Override
    public void normalize() {
        if (this.x == 0 && this.y == 0 && this.z == 0 && this.w == 0) return;
        float len = this.len();
        this.x /= len;
        this.y /= len;
        this.z /= len;
        this.w /= len;
    }

    public static Vector4f normalize(Vector4f v) {
        if (v.x == 0 && v.y == 0 && v.z == 0 && v.w == 0) return v;
        float len = v.len();
        return new Vector4f(v.x / len, v.y / len, v.z / len, v.w / len);
    }

    @Override
    public float scalarMul(Vector4f v) {
        return this.x * v.getX() + this.y * v.getY() + this.z * v.getZ() + this.w * v.getW();
    }
}
