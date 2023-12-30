package ru.vsu.cs.team4.task4.math.vector;

public interface Vector<T extends Vector<T>> {

    Vector add(T v);

    void add(float num);

    Vector sub(T v);

    Vector sub(float num);

    Vector mul(float num);

    void div(float num);

    float len();

    Vector normalized();

   float dotProduct(T v);

}
