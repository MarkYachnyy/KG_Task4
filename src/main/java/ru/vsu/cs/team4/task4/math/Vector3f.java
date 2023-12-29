package ru.vsu.cs.team4.task4.math;

// Это заготовка для собственной библиотеки для работы с линейной алгеброй
public class Vector3f {
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Vector3f other) {
        // todo: желательно, чтобы это была глобальная константа
        final float eps = 1e-7f;
        return Math.abs(x - other.x) < eps && Math.abs(y - other.y) < eps && Math.abs(z - other.z) < eps;
    }

    public Vector3f add(Vector3f v2){
        this.x += v2.x;
        this.y += v2.y;
        this.z += v2.z;
        return this;
    }


    public static Vector3f residual(Vector3f v1, Vector3f v2){
        return new Vector3f(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public Vector3f multiply(float a){
        this.x *= a;
        this.y *= a;
        this.z *= a;
        return this;
    }

    public float length(){
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public Vector3f normalized() {
        float length = this.length();
        return new Vector3f(x / length, y / length, z / length);
    }

    public static Vector3f crossProduct(Vector3f v1, Vector3f v2) {
        float x = v1.y * v2.z - v2.y * v1.z;
        float y = v1.z * v2.x - v1.x * v2.z;
        float z = v1.x * v2.y - v1.y * v2.x;
        return new Vector3f(x, y, z);
    }

    public float x, y, z;

    public String toString() {
        return "{" + x + ", " + y + ", " + z + "}";
    }

    public String coordsToStringSplitBySpace() {
        return x+" "+y+" "+z;
    }
}
