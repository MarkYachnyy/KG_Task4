package ru.vsu.cs.team4.task4.Affine.affineComposite;


import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;

public class RotateY implements AffineComposite {
    private Matrix4f rotationMatrix;
    public RotateY(double thetaY) {
        rotationMatrix = new Matrix4f(new float[][]{
                {(float) Math.cos(thetaY), 0, (float) Math.sin(thetaY), 0},
                {0, 1, 0, 0},
                {(float) -Math.sin(thetaY), 0, (float) Math.cos(thetaY), 0},
                {0, 0, 0, 1}
        });
    }

    @Override
    public Matrix4f getMatrix() {
        return rotationMatrix;
    }
}
