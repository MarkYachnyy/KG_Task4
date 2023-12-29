package ru.vsu.cs.team4.task4.Affine;

public class AffineExceptions extends NullPointerException {
    public AffineExceptions(String message) {
        super("Exception in affine transformation: " + message);
    }
}
