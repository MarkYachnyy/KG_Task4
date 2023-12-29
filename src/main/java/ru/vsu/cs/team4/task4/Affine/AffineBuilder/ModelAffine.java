package ru.vsu.cs.baklanova.Affine.AffineBuilder;


import ru.vsu.cs.baklanova.math.matrix.Matrix4f;
import ru.vsu.cs.baklanova.math.vector.Vector3f;
import ru.vsu.cs.baklanova.model.Model;

public class ModelAffine {
    private Vector3f scale, rotate, translate;
    private Rotate.RotateWayEnum rotateWay;
    private AffineBuilder builder = new AffineBuilder();

    public ModelAffine() {
        this.scale = new Vector3f(1,1,1);
        this.rotateWay = Rotate.RotateWayEnum.XYZ;
        this.rotate = new Vector3f(0,0,0);
        this.translate = new Vector3f(0,0,0);
    };

    public ModelAffine(ModelAffine m) {
        this.scale = m.getScale();
        this.rotateWay = m.getRotateWay();
        this.rotate = m.getRotate();
        this.translate = m.getTranslate();
    }
    public ModelAffine(Vector3f scale, Vector3f rotate, Vector3f translate) {
        this.scale = scale;
        this.rotateWay = Rotate.RotateWayEnum.XYZ;
        this.rotate = rotate;
        this.translate = translate;
    }
    public ModelAffine(Vector3f scale, Rotate.RotateWayEnum way, Vector3f rotate, Vector3f translate) {
        this.scale = scale;
        this.rotateWay = way;
        this.rotate = rotate;
        this.translate = translate;
    }
    public Matrix4f modelMatrix() throws Exception {
        builder = new AffineBuilder();
        if (scale != null) {
            builder.scale().byVector(scale).close();
        }

        if (rotate != null) {
            builder.rotate().inOrder(rotateWay, rotate).close();
        }

        if (translate != null) {
            builder.translate().byVector(translate).close();
        }

        return builder.returnFinalMatrix();
    }

    public void changeModel(Model model) {
        builder.changeModel(model);
    }

    public Model newChangedModel(Model model) {
        return builder.returnChangedModel(model);
    }

    public ModelAffine setRotate(Rotate.RotateWayEnum rotateWay, Vector3f rotate) {
        this.rotate = rotate;
        this.rotateWay = rotateWay;

        return this;
    }

    public ModelAffine setScale(Vector3f scale) throws Exception {
        this.scale = scale;

        return this;
    }

    public ModelAffine setTranslate(Vector3f translate) throws Exception {
        this.translate = translate;

        return this;
    }

    public Rotate.RotateWayEnum getRotateWay() {
        return rotateWay;
    }

    public Vector3f getRotate() {
        return rotate;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Vector3f getTranslate() {
        return translate;
    }
}
