package ru.vsu.cs.team4.task4.affineTransformation.affineComposite;


import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;

public class RotateZ implements AffineComposite {
    private Matrix4f rotationMatrix;
    public RotateZ(double thetaZ) {
        rotationMatrix = new Matrix4f(new float[][]{
                {(float) Math.cos(thetaZ), (float) Math.sin(thetaZ), 0, 0},
                {(float) -Math.sin(thetaZ), (float) Math.cos(thetaZ), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    @Override
    public void applyTransform(Matrix4f m) {
        m.mul(rotationMatrix);
    }
}
