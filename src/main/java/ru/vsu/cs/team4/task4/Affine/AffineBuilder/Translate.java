package ru.vsu.cs.baklanova.Affine.AffineBuilder;

import ru.vsu.cs.baklanova.Affine.AffineMatrix;
import ru.vsu.cs.baklanova.Affine.AxisEnum;
import ru.vsu.cs.baklanova.math.vector.Vector3f;
public class Translate {
    AffineBuilder builder;
    public Translate(AffineBuilder builder) {
        this.builder = builder;
    }

    public Translate byVector(Vector3f translateVector) {
        builder.addTranslate(AffineMatrix.translateMatrix(translateVector));

        return this;
    }
    public Translate byAxis(AxisEnum axisEnum, float num) {
        switch (axisEnum) {
            case X -> byX(num);
            case Y -> byY(num);
            case Z -> byZ(num);
        }

        return this;
    }

    public Translate byX(float num) {
        byVector(new Vector3f(num, 0, 0));
        return this;
    }

    public Translate byY(float num) {
        byVector(new Vector3f(0, num, 0));
        return this;
    }

    public Translate byZ(float num) {
        byVector(new Vector3f(0, 0, num));
        return this;
    }

    public AffineBuilder close() {
        return builder;
    }
}
