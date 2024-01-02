package ru.vsu.cs.team4.task4.affineTransformation.affineComposite;


import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;

public interface AffineComposite {
    void applyTransform(Matrix4f m);
}
