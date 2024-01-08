package ru.vsu.cs.team4.task4.math.matrix;

import ru.vsu.cs.team4.task4.math.vector.Vector4f;

import java.util.Arrays;

public class Matrix4f implements Matrix<Matrix4f>{

    private static final int LENGTH = 16;
    private static final int MINOR_LENGTH = 3;
    private float[] values = new float[LENGTH];

    private Matrix4f(float[] values) {
        this.values = values.clone();
    }

    public Matrix4f(float[][] values) {this.values = arrFromMatrix(values).clone();}

    public Matrix4f() {}

    public static Matrix4f zeroMatrix() {
        return new Matrix4f();
    }

    public static Matrix4f identityMatrix() {
        return new Matrix4f(new float[] {1.0f, 0.0f, 0.0f, 0.0f,
                                         0.0f, 1.0f, 0.0f, 0.0f,
                                         0.0f, 0.0f, 1.0f, 0.0f,
                                         0.0f, 0.0f, 0.0f, 1.0f});
    }

    public float[][] getValues() {return matrixFromArray(values);}

    public float get(int i, int j){
        return values[4 * i + j];
    }

    public void setValues(float[][] values) {this.values = arrFromMatrix(values.clone());}

    public void setValue(float num, int i, int j) {
        this.values[i * 4 + j] = num;
    }

    @Override
    public Matrix4f sum(Matrix4f m) {
        Matrix4f resM = new Matrix4f(this.values.clone());
        for (int i = 0; i < LENGTH; i++) {
            resM.values[i] += m.values[i];
        }
        return resM;
    }

    public void sumMut(Matrix4f m) {
        for (int i = 0; i < LENGTH; i++) {
            this.values[i] += m.values[i];
        }
    }

    @Override
    public Matrix4f sub(Matrix4f m) {
        Matrix4f resM = new Matrix4f(this.values.clone());
        for (int i = 0; i < LENGTH; i++) {
            resM.values[i] -= m.values[i];
        }
        return resM;
    }

    public void subMut(Matrix4f m) {
        for (int i = 0; i < LENGTH; i++) {
            this.values[i] -= m.values[i];
        }
    }

    @Override
    public Matrix4f mul(Matrix4f m) {
        Matrix4f resM = new Matrix4f();
        int currRow = 0;
        int currCol = 0;
        for(int i = 0; i < LENGTH; i++) {
            currRow = i / 4;
            currCol = i % 4;
            resM.values[i] = this.values[currRow * 4] * m.values[currCol] + this.values[currRow * 4 + 1] * m.values[currCol + 4]
                    + this.values[currRow * 4 + 2] * m.values[currCol + 8] + this.values[currRow * 4 + 3] * m.values[currCol + 12];
        }
        return resM;
    }

    public void mulMut(Matrix4f m) {
        int currRow = 0;
        int currCol = 0;
        float[] resValues = new float[LENGTH];
        for(int i = 0; i < LENGTH; i++) {
            currRow = i / 4;
            currCol = i % 4;
            resValues[i] = this.values[currRow * 4] * m.values[currCol] + this.values[currRow * 4 + 1] * m.values[currCol + 4]
                    + this.values[currRow * 4 + 2] * m.values[currCol + 8] + this.values[currRow * 4 + 3] * m.values[currCol + 12];
        }
        this.values = resValues;
    }

    @Override
    public Matrix4f trs() {
        Matrix4f resM = new Matrix4f(this.values.clone());
        resM.swapElement(1, 4);
        resM.swapElement(2, 8);
        resM.swapElement(3, 12);
        resM.swapElement(6, 9);
        resM.swapElement(7, 13);
        resM.swapElement(11, 14);
        return resM;
    }

    public void trsMut() {
        swapElement(1, 4);
        swapElement(2, 8);
        swapElement(3, 12);
        swapElement(6, 9);
        swapElement(7, 13);
        swapElement(11, 14);
    }

    public Vector4f mulV(Vector4f v) {
        float[] vectorValues = new float[4];
        int currRow = 0;
        for (int i = 0; i < LENGTH / 4; i++) {
            vectorValues[i] = this.values[currRow * 4] * v.getX() + this.values[currRow * 4 + 1] * v.getY() +
                    this.values[currRow * 4 + 2] * v.getZ() + this.values[currRow * 4 + 3] * v.getW();
            currRow++;
        }
        return new Vector4f(vectorValues);
    }

    @Override
    public float det() {
        float det = 0;
        for (int i = 0; i < 4; i++) {
            Matrix3f minor = new Matrix3f(getMinor(this.values.clone(), i));
            int sign = i % 2 == 0 ? 1 : -1;
            det += this.values[i] * sign * minor.det();
        }
        return det;
    }

    @Override
    public Matrix4f inverseMatrix() {
        float det = this.det();
        float[] resValues = new float[LENGTH];
        Matrix3f minor = new Matrix3f();
        Matrix4f resMatrix = this.trs();
        for (int i = 0; i < LENGTH; i++) {
            int sign = (i / 4 + i % 4) % 2 == 0 ? 1 : -1;
            minor.setValues(getMinor(resMatrix.values.clone(), i));
            resValues[i] = 1 / det * sign * minor.det();
        }
        resMatrix.values = resValues;
        return resMatrix;
    }

    private static float[][] getMinor(float[] matrix, float index) {
        float[][] minor = new float[MINOR_LENGTH][MINOR_LENGTH];
        int currNum = 0;
        int indexRow = (int) index / 4;
        int indexCol = (int) (index % 4);
        int currRow = 0;
        int currCol = 0;
        for (int i = 0; i < LENGTH; i++) {
            currRow = i / 4;
            currCol = i % 4;
            if (currRow != indexRow && currCol != indexCol) {
                minor[currNum / 3][currNum % 3] = matrix[i];
                currNum++;
            }

        }
        return minor;
    }

    private void swapElement(int i1, int i2) {
        float num1 = this.values[i1];
        this.values[i1] = this.values[i2];
        this.values[i2] = num1;
    }

    private static float[] arrFromMatrix(float[][] matrix) {
        float[] arr = new float[LENGTH];
        int index = 0;
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, arr, index, matrix[i].length);
            index += matrix[i].length;
        }
        return arr;
    }

    private static float[][] matrixFromArray(float[] arr) {
        int length = (int) Math.sqrt(LENGTH);
        float[][] matrix = new float[length][length];
        for (int i = 0; i < arr.length; i++) {
            matrix[i / length][i % length] = arr[i];
        }
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (float[] f: this.getValues()){
            res.append(Arrays.toString(f)).append('\n');
        }
        return res.toString();
    }

    public Matrix3f getMatrix3f(){
        float[][] res = new float[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                res[i][j] = this.get(i,j);
            }
        }
        return new Matrix3f(res);
    }
}
