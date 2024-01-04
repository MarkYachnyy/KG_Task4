package ru.vsu.cs.team4.task4.affine.affineComposite;

import ru.vsu.cs.team4.task4.math.matrix.Matrix3f;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;

public class RotateCustom implements AffineComposite {

    private Matrix4f rotationMatrix;

    @Override
    public Matrix4f getMatrix() {
        return null;
    }

    public RotateCustom(Vector3f axis, float theta) {
        axis.normalized();
        float c = (float) Math.cos(theta);
        float s = (float) Math.sin(theta);
        float x = axis.getX();
        float y = axis.getY();
        float z = axis.getZ();
        rotationMatrix = new Matrix4f(new float[][]{{c + (1 - c) * x * x, (1 - c) * x * y - s * z, (1 - c) * z * x + s * y, 0},
                {(1 - c) * x * y + s * z, c + (1 - c) * y * y, (1 - c) * z * y - s * x, 0},
                {(1 - c) * x * z - s * y, (1 - c) * y * z + s * x, c + (1 - c) * z * z, 0},
                {0, 0, 0, 1}});

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
