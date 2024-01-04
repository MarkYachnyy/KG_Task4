package ru.vsu.cs.team4.task4.affine;

import ru.vsu.cs.team4.task4.affine.affineComposite.*;

public class AffineBuilder {
    private Affine affine;

    public AffineBuilder() {
        affine = new Affine();
    }

    public AffineBuilder move(float dx, float dy, float dz) {
        affine.add(new Translate(dx, dy, dz));
        return this;
    }

    public AffineBuilder scale(float sx, float sy, float sz) {
        affine.add(new Scale(sx, sy, sz));
        return this;
    }

    public AffineBuilder rotateX(double thetaX) {
        affine.add(new RotateX(thetaX));
        return this;
    }

    public AffineBuilder rotateY(double thetaY) {
        affine.add(new RotateY(thetaY));
        return this;
    }

    public AffineBuilder rotateZ(double thetaZ) {
        affine.add(new RotateZ(thetaZ));
        return this;
    }

    public AffineBuilder apply(AffineComposite affineComposite) {
        affine.add(affineComposite);
        return this;
    }

    public Affine build() {
        return affine;
    }
}
