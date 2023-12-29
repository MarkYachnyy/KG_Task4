package main;

import main.matrix.Matrix3f;
import main.matrix.Matrix4f;
import main.vector.Vector2f;
import main.vector.Vector3f;
import main.vector.Vector4f;

public class Utils {

    public static final float EPSILON = 1E-4f;

    public static boolean epsEquals(float num1, float num2) {
        return Math.abs(num1 - num2) < EPSILON;
    }

    public static boolean equals(Matrix4f m1, Matrix4f m2) {
        float[][] num1 = m1.getValues();
        float[][] num2 = m2.getValues();
        for (int i = 0; i < num1.length; i++) {
            for (int j = 0; j < num1[i].length; j++) {
                if (!epsEquals(num1[i][j], num2[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean equals(Matrix3f m1, Matrix3f m2) {
        float[][] num1 = m1.getValues();
        float[][] num2 = m2.getValues();
        for (int i = 0; i < num1.length; i++) {
            for(int j = 0; j < num1[i].length; j++) {
                if (!epsEquals(num1[i][j], num2[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean equals(Vector2f v1, Vector2f v2) {
        return epsEquals(v1.getX(), v2.getX()) && epsEquals(v1.getY(), v2.getY());
    }

    public static boolean equals(Vector3f v1, Vector3f v2) {
        return epsEquals(v1.getX(), v2.getX()) && epsEquals(v1.getY(), v2.getY()) && epsEquals(v1.getZ(), v2.getZ());
    }
    public static boolean equals(Vector4f v1, Vector4f v2) {
        return epsEquals(v1.getX(), v2.getX()) && epsEquals(v1.getY(), v2.getY()) && epsEquals(v1.getZ(), v2.getZ()) && epsEquals(v1.getW(), v2.getW());
    }

}
