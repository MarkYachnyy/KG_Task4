package ru.vsu.cs.baklanova.Affine;

public class AffineExceptions extends NullPointerException {
    public AffineExceptions(String message) {
        super("Exception in affine transformation: " + message);
    }
}
