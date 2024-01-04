package ru.vsu.cs.team4.task4.affine.affineComposite;


import ru.vsu.cs.team4.task4.math.matrix.Matrix3f;
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

    public Matrix3f getMatrix3f(){
        Matrix4f matrix4f = rotationMatrix;
        Matrix3f res = new Matrix3f();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                res.set(i, j, matrix4f.get(i, j));
            }
        }
        return res;
    }
}
