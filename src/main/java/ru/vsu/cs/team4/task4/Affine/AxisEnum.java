package ru.vsu.cs.team4.task4.Affine;

public enum AxisEnum {
    X(0, 'x'),
    Y(1, 'y'),
    Z(2, 'z');

    private int aNum;

    private char aChar;

    AxisEnum(int aNum, char aChar) {
        this.aNum = aNum;
        this.aChar = aChar;
    }

    public int getaNum() {
        return aNum;
    }

    public char getaChar() {
        return aChar;
    }
}