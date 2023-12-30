package ru.vsu.cs.team4.task4.math.vector;

public class Vector2f implements Vector<Vector2f>{

    private float x;
    private float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vector2f add(Vector2f v) {
        this.x += v.getX();
        this.y += v.getY();
        return this;
    }

    public static Vector2f sum(Vector2f v1, Vector2f v2) {
        return new Vector2f(v1.x + v2.x, v1.y + v2.y);
    }


    @Override
    public void add(float num) {
        this.x += num;
        this.y += num;
    }

    public static Vector2f sum(Vector2f v, float num) {
        return new Vector2f(v.x + num, v.y + num);
    }


    @Override
    public Vector2f sub(Vector2f v) {
        this.x -= v.getX();
        this.y -= v.getY();
        return this;
    }

    public static Vector2f sub(Vector2f v1, Vector2f v2) {
        return new Vector2f(v1.x - v2.x, v1.y - v2.y);
    }


    @Override
    public Vector2f sub(float num) {
        this.x -= num;
        this.y -= num;
        return this;
    }

    public static Vector2f sub(Vector2f v, float num) {
        return new Vector2f(v.x - num, v.y - num);
    }


    @Override
    public Vector2f mul(float num) {
        this.x *= num;
        this.y *= num;
        return this;
    }

    public static Vector2f mul(Vector2f v, float num) {
        return new Vector2f(v.x * num, v.y * num);
    }

    @Override
    public void div(float num) {
        this.x /= num;
        this.y /= num;
    }

    public static Vector2f div(Vector2f v, float num) {
        return new Vector2f(v.x / num, v.y / num);
    }

    @Override
    public float len() {
        return (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }


    @Override
    public Vector2f normalized() {
        if (this.x == 0 && this.y == 0) return this;
        float len = this.len();
        this.x /= len;
        this.y /= len;
        return this;
    }

    public static Vector2f normalize(Vector2f v) {
        if (v.x == 0 && v.y == 0) return v;
        float len = v.len();
        return new Vector2f(v.x / len, v.y / len);
    }

    @Override
    public float dotProduct(Vector2f v) {
        return this.x * v.getX() + this.y * getY();
    }

    public String coordstoStringSplitBySpace() {
        return x + " " + y;
    }
}
