package main.vector;

public interface Vector<T extends Vector<T>> {

    void sum(T v);

    void sum(float num);

    void sub(T v);

    void sub(float num);

    void mul(float num);

    void div(float num);

    float len();

    void normalize();

   float scalarMul(T v);

}
