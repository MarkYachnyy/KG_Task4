package ru.vsu.cs.team4.task4.math.vector;

public class Vector3f implements Vector<Vector3f> {

    private float x;
    private float y;
    private float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector2f v ) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = 1;
    }

    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3f(float[] values) {
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vector3f add(Vector3f v) {
        this.x += v.getX();
        this.y += v.getY();
        this.z += v.getZ();
        return this;
    }

    public static Vector3f sum(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    @Override
    public void add(float num) {
        this.x += num;
        this.y += num;
        this.z += num;
    }

    public static Vector3f sum(Vector3f v, float num) {
        return new Vector3f(v.x + num, v.y + num, v.z + num);
    }

    @Override
    public Vector3f sub(Vector3f v) {
        this.x -= v.getX();
        this.y -= v.getY();
        this.z -= v.getZ();
        return this;
    }

    public static Vector3f residual(Vector3f v1, Vector3f v2) {
        return new Vector3f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    @Override
    public Vector3f sub(float num) {
        this.x -= num;
        this.y -= num;
        this.z -= num;
        return this;
    }

    public static Vector3f residual(Vector3f v, float num) {
        return new Vector3f(v.x - num, v.y - num, v.z - num);
    }


    @Override
    public Vector3f mul(float num) {
        this.x *= num;
        this.y *= num;
        this.z *= num;
        return this;
    }

    public static Vector3f mul(Vector3f v, float num) {
        return new Vector3f(v.x * num, v.y * num, v.z * num);
    }

    @Override
    public void div(float num) {
        this.x /= num;
        this.y /= num;
        this.z /= num;
    }

    public static Vector3f div(Vector3f v, float num) {
        return new Vector3f(v.x / num, v.y / num, v.z / num);
    }

    @Override
    public float len() {
        return (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    @Override
    public Vector3f normalized() {
        if (this.x == 0 && this.y == 0 && this.z == 0) return this;
        float len = this.len();
        this.x /= len;
        this.y /= len;
        this.z /= len;
        return this;
    }

    public static Vector3f normalize(Vector3f v) {
        if (v.x == 0 && v.y == 0 && v.z == 0) return v;
        float len = v.len();
        return new Vector3f(v.x / len, v.y / len, v.z / len);
    }

    @Override
    public float dotProduct(Vector3f v) {
        return this.x * v.getX() + this.y * v.getY() + this.z * v.getZ();
    }

    /*public Vector3f vectorMul(Vector3f v2) {
        float resX = this.getY() * v2.getZ() - this.getZ() * v2.getY();
        float resY = this.getZ() * v2.getX() - this.getX() * v2.getZ();
        float resZ = this.getX() * v2.getY() - this.getY() * v2.getX();
        return new Vector3f(resX, resY, resZ);
    }*/

    public static Vector3f crossProduct(Vector3f v1, Vector3f v2) {
        float resX = v1.y * v2.z - v1.z * v2.y;
        float resY = v1.z * v2.x - v1.x * v2.z;
        float resZ = v1.x * v2.y - v1.y * v2.x;
        return new Vector3f(resX, resY, resZ);
    }

    public String coordstoStringSplitBySpace() {
        return x + " " + y + " " + z;
    }
}
