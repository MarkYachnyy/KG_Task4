package main.matrix;

import main.Utils;
import main.vector.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix3f implements Matrix<Matrix3f> {

    private static final int LENGTH = 9;
    private static final int MINOR_LENGTH = 4;
    private float[] values = new float[LENGTH];

    private Matrix3f(float[] values) {
        this.values = values.clone();
    }

    public Matrix3f(float[][] values) {
        this.values = arrFromMatrix(values).clone();
    }

    public Matrix3f() {}

    public static Matrix3f zeroMatrix() {
        return new Matrix3f();
    }

    public static Matrix3f identityMatrix() {
        return new Matrix3f(new float[] {1.0f, 0.0f, 0.0f,
                                         0.0f, 1.0f, 0.0f,
                                         0.0f, 0.0f, 1.0f});
    }

    public float[][] getValues() {return matrixFromArray(this.values.clone());}


    public void setValues(float[][] values) {this.values = arrFromMatrix(values).clone();}


    @Override
    public Matrix3f sum(Matrix3f m) {
        Matrix3f resM = new Matrix3f(this.values.clone());
        for(int i = 0; i < LENGTH; i++) {
            resM.values[i] += m.values[i];
        }
        return resM;
    }

    public void sumMut(Matrix3f m) {
        for (int i = 0; i < LENGTH; i++) {
            this.values[i] += m.values[i];
        }
    }

    @Override
    public Matrix3f sub(Matrix3f m) {
        Matrix3f resM = new Matrix3f(this.values.clone());
        for (int i = 0; i < LENGTH; i++) {
            resM.values[i] -= m.values[i];
        }
        return resM;
    }

    public void subMut(Matrix3f m) {
        for (int i = 0; i < LENGTH; i++) {
            this.values[i] -= m.values[i];
        }
    }

    @Override
    public Matrix3f mul(Matrix3f m) {
        Matrix3f resM = new Matrix3f();
        int currRow = 0;
        int currCol = 0;
        for(int i = 0; i < LENGTH; i++) {
            currRow = i / 3;
            currCol = i % 3;
            resM.values[i] = this.values[currRow * 3] * m.values[currCol] + this.values[currRow * 3 + 1] * m.values[currCol + 3]
                    + this.values[currRow * 3 + 2] * m.values[currCol + 6];
        }
        return resM;
    }

    /*public Matrix3f mul(Matrix3f m) {
        Matrix3f resM = new Matrix3f();
        int currRow = 0;
        int currCol = 0;
        for(int i = 0; i < LENGTH; i++) {
            if (i / 3 > currRow) {
                currRow++;
                currCol = 0;
            }
            resM.values[i] = this.values[currRow * 3] * m.values[currCol] + this.values[currRow * 3 + 1] * m.values[currCol + 3]
                    + this.values[currRow * 3 + 2] * m.values[currCol + 6];
            currCol++;
        }
        return resM;
    }*/


    public void mulMut(Matrix3f m) {
        int currRow = 0;
        int currCol = 0;
        float[] resValues = new float[LENGTH];
        for(int i = 0; i < LENGTH; i++) {
            currRow = i / 3;
            currCol = i % 3;
            resValues[i] = this.values[currRow * 3] * m.values[currCol] + this.values[currRow * 3 + 1] * m.values[currCol + 3]
                    + this.values[currRow * 3 + 2] * m.values[currCol + 6];
        }
        this.values = resValues;
    }

    @Override
    public Matrix3f trs() {
        return trs(this);
    }

    public static Matrix3f trs(Matrix3f m) {
        Matrix3f resM = new Matrix3f(m.values.clone());
        resM.trsMut();
        return resM;
    }

    public void trsMut() {
        this.swapElement(1, 3);
        this.swapElement(2, 6);
        this.swapElement(5, 7);
    }

    public Vector3f mulV(Vector3f v) {
        float[] vectorValues = new float[3];
        int currRow = 0;
        for (int i = 0; i < LENGTH / 3; i++) {
            vectorValues[i] = this.values[currRow * 3] * v.getX() + this.values[currRow * 3 + 1] * v.getY() +
                    this.values[currRow * 3 + 2] * v.getZ();
            currRow++;
        }
        return new Vector3f(vectorValues);
    }

    @Override
    public float det() {
        return this.values[0] * this.values[4] * this.values[8] + this.values[1] * this.values[5] * this.values[6] +
                this.values[3] * this.values[7] * this.values[2] - this.values[2] * this.values[4] * this.values[6] -
                this.values[0] * this.values[5] * this.values[7] - this.values[3] * this.values[1] * this.values[8];
    }

    @Override
    public Matrix3f inverseMatrix() {
        float det = this.det();
        float[] resValues = new float[LENGTH];
        float[] minor;
        Matrix3f resMatrix = this.trs();
        for (int i = 0; i < LENGTH; i++) {
            int sign = i % 2 == 0 ? 1 : -1;
            minor = getMinor(resMatrix.values.clone(), i);
            resValues[i] = 1 / det * sign * (minor[0] * minor[3] - minor[1] * minor[2]);
        }
        resMatrix.values = resValues;
        return resMatrix;
    }


    private static float[] getMinor(float[] matrix, float index) {
        float[] minor = new float[MINOR_LENGTH];
        int currNum = 0;
        int indexRow = (int) index / 3;
        int indexCol = (int) (index % 3);
        int currRow = 0;
        int currCol = 0;
        for (int i = 0; i < LENGTH; i++) {
            if (i / 3 > currRow) {
                currRow++;
                currCol = 0;
            }
            if (currRow != indexRow && currCol != indexCol) {
                minor[currNum] = matrix[i];
                currNum++;
            }
            currCol++;

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

}
