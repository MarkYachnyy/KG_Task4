package ru.vsu.cs.team4.task4.math;

// Это заготовка для собственной библиотеки для работы с линейной алгеброй
public class Vector2f {
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float x, y;

    public String coordsToStringSplitBySpace() {
        return x+" "+y;
    }

}