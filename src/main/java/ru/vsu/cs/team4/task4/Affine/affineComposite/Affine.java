package ru.vsu.cs.team4.task4.Affine.affineComposite;


import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;

public class Affine implements AffineComposite {

    private Matrix4f matrix;

    public Affine() {
        matrix = new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}});
    }

    public void add(AffineComposite af) {
        matrix = new Matrix4f(af.getMatrix().getValues()).mul(matrix);
    }

    @Override
    public Matrix4f getMatrix() {
        return matrix;
    }
}
