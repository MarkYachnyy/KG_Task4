package ru.vsu.cs.baklanova.Affine;


import ru.vsu.cs.baklanova.Math.matrix.Matrix4f;
import ru.vsu.cs.baklanova.Math.vector.Vector3f;

public class AffineMatrix {
    public static Matrix4f scaleMatrix(Vector3f vector) {
        if (vector == null) {
            throw new AffineExceptions("Vector is null");
        }
        final int SIZE = 3;
        float[][] matrix = new float[SIZE + 1][SIZE + 1];

        for (int i = 0; i < SIZE; i++) {
            float n = 0;
            switch (i) {
                case 0 -> n = vector.getX();
                case 1 -> n = vector.getY();
                case 2 -> n = vector.getZ();
            }
            matrix[i][i] = n;
        }
        matrix[SIZE][SIZE] = 1;


        return new Matrix4f(matrix);
    }

    public static Matrix4f translateMatrix(Vector3f vector) {
        if (vector == null) {
            throw new AffineExceptions("Vector is null");
        }
        final int SIZE = 4;
        float[][] arr = new float[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            arr[i][i] = 1;
        }
        arr[0][SIZE - 1] = vector.getX();
        arr[1][SIZE - 1] = vector.getY();
        arr[2][SIZE - 1] = vector.getZ();

        return new Matrix4f(arr);
    }

    public static Matrix4f rotateMatrix(AxisEnum axisEnum, double fi) throws Exception {
        final int SIZE = 3;

        int axis = axisEnum.getaNum();
        final double EXP = 10e-15;
        float[][] arr = new float[SIZE + 1][SIZE + 1];
        float sinFi = (float) Math.sin(fi);
        if (Math.abs(sinFi) < EXP) {
            sinFi = 0;
        }
        float cosFi = (float) Math.cos(fi);
        if (Math.abs(cosFi) < EXP) {
            cosFi = 0;
        }

        int k = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i == axis && j == axis) {
                    arr[i][j] = 1;
                } else if (i == axis || j == axis) {
                    arr[i][j] = 0;
                } else {
                    switch (k) {
                        case 0, 3 -> arr[i][j] = cosFi;
                        case 1 -> arr[i][j] = sinFi;
                        case 2 -> arr[i][j] = -sinFi;
                    }
                    k++;
                }
            }
        }

        arr[SIZE][SIZE] = 1;

        return new Matrix4f(arr);
    }
}
