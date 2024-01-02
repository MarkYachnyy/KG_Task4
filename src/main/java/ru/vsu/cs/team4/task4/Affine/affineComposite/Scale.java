package ru.vsu.cs.team4.task4.Affine.affineComposite;


import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;

public class Scale implements AffineComposite {
    private Matrix4f scaleMatrix;

    public Scale(float sX, float sY, float sZ) {
        scaleMatrix = new Matrix4f(new float[][]{
                {sX, 0, 0, 0},
                {0, sY, 0, 0},
                {0, 0, sZ, 0},
                {0, 0, 0, 1}
        });
    }

    @Override
    public Matrix4f getMatrix() {
        return scaleMatrix;
    }
}
