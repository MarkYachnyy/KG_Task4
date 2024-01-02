package ru.vsu.cs.team4.task4.Affine.affineComposite;


import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;

public class RotateX implements AffineComposite {
    Matrix4f rotationMatrix;
    public RotateX(double thetaX) {
        rotationMatrix = new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, (float) Math.cos(thetaX), (float) Math.sin(thetaX), 0},
                {0, (float) -Math.sin(thetaX), (float) Math.cos(thetaX), 0},
                {0, 0, 0, 1}
        });
    }

    @Override
    public Matrix4f getMatrix() {
        return rotationMatrix;
    }
}
