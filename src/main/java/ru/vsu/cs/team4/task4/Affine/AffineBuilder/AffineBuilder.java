package ru.vsu.cs.team4.task4.Affine.AffineBuilder;

import ru.vsu.cs.team4.task4.Affine.AffineExceptions;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.math.vector.Vector4f;
import ru.vsu.cs.team4.task4.model.Model;
public class AffineBuilder {
    private Matrix4f finalMatrix;

    private void resetFinalMatrix() {
        this.finalMatrix = new Matrix4f(new float[][]{  {1, 0, 0, 0},
                                                        {0, 1, 0, 0},
                                                        {0, 0, 1, 0},
                                                        {0, 0, 0, 1},});
    }

    public AffineBuilder() {resetFinalMatrix();}

    public AffineBuilder(AffineBuilder builder) {
        this.finalMatrix = new Matrix4f(builder.finalMatrix.getValues());
    }

    public Scale scale() {
        return new Scale(this);
    }

    protected void addScale(Matrix4f newScale) {
        finalMatrix = newScale.mul(finalMatrix);
    }

    public Rotate rotate() {
        return new Rotate(this);
    }
    protected void addRotate(Matrix4f newRotate) throws Exception {
        finalMatrix = newRotate.mul(finalMatrix);
    }

    public Translate translate() {
        return new Translate(this);
    }

    protected void addTranslate(Matrix4f newTranslate) {
        for (int i = 0; i < newTranslate.getValues().length; i++) {
            newTranslate.setValue(0, i, i);
        }
        finalMatrix = newTranslate.sum(finalMatrix);
    }
    public void changeModel(Model model) {
        if (model.vertices != null) {
            model.vertices.replaceAll(this::changeDot);
        }
        if (model.normals != null) {
            model.normals.replaceAll(this::changeDot);
        }
    }

    private Vector3f changeDot(Vector3f dot) {
        if (dot == null) {
            throw new AffineExceptions("Dot in model is null");
        }
        return returnVector3f(dot);
    }

    public Model returnChangedModel(Model model1) {
        if (model1 == null) {
            throw new AffineExceptions("Model is null");
        }

        Model model = new Model(model1);

        changeModel(model);

        return model;
    }

    private Vector3f returnVector3f(Vector3f v) {
        Vector4f vector = new Vector4f(v);
        vector = returnFinalMatrix().mulV(vector);

        return new Vector3f(vector.getX(), vector.getY(), vector.getZ());
    }

    public Matrix4f returnFinalMatrix() {
        return finalMatrix;
    }
}
