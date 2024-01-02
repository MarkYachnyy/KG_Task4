package ru.vsu.cs.team4.task4.affineTransformation.affineComposite;


import ru.vsu.cs.team4.task4.affineTransformation.AffineApplier;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.math.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Affine implements AffineComposite {
    List<AffineComposite> affineComposites = new ArrayList<>();
    private Matrix4f base;
    public Affine() {
        base = new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}});
    }

    public void add(AffineComposite af) {
        affineComposites.add(af);
    }

    @Override
    public void applyTransform(Matrix4f m) {
        for (int i = affineComposites.size() - 1; i >= 0; i--) {
            affineComposites.get(i).applyTransform(m);
        }
    }

    public AffineApplier createApplier() {
        Matrix4f affineTransform = new Matrix4f(base.getValues());
        applyTransform(affineTransform);
        return new AffineApplier(affineTransform);
    }

    public Vector3f applyAffineToVector(Vector3f v) {
        Matrix4f affineTransform = new Matrix4f(base.getValues());
        applyTransform(affineTransform);

        Vector4f v4 = new Vector4f(v.getX(), v.getY(), v.getZ(), 1);
        Vector4f result = affineTransform.mulV(v4);
        return new Vector3f(result.getX(), result.getY(), result.getZ());
    }
}
