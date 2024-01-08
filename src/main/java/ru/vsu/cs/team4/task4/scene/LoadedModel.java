package ru.vsu.cs.team4.task4.scene;


import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.model.*;
import ru.vsu.cs.team4.task4.rasterization.ColorIntARGB;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class LoadedModel {

    private String id;
    private ModelTriangulated model;
    private String modelPath;
    private boolean disableSmoothing = true;
    private boolean disableTexture = true;
    private boolean disableMesh = true;

    private ColorIntARGB[][] textureARGB;

    private Vector3f rotateV = new Vector3f(0, 0, 0);
    private Vector3f translateV = new Vector3f(0, 0, 0);
    private Vector3f scaleV = new Vector3f(1, 1, 1);

    public Vector3f getRotateV() {
        return rotateV;
    }

    public Vector3f getTranslateV() {
        return translateV;
    }

    public Vector3f getScaleV() {
        return scaleV;
    }

    public void setRotateV(Vector3f rotateV) {
        this.rotateV = rotateV;
    }

    public void setTranslateV(Vector3f translateV) {
        this.translateV = translateV;
    }

    public void setScaleV(Vector3f scaleV) {
        this.scaleV = scaleV;
    }


    public LoadedModel(ModelTriangulated model, String modelPath) throws IOException {
        this.model = model;
    }

    public LoadedModel(Model model, String modelPath) throws IOException {
        ModelTriangulated newModel = new ModelTriangulated(model);
        List<Vector3f> normals = NormalCalculator.recalculateNormals(newModel.getVertices(), newModel.getPolygons());
        for (Polygon polygon : newModel.getPolygons()) {
            polygon.setNormalIndices(new ArrayList<>(polygon.getVertexIndices()));
        }
        newModel.setNormals(normals);
        this.model = newModel;
        this.modelPath = modelPath;
        this.textureARGB = new ColorIntARGB[][]{{new ColorIntARGB(255, 255,255,255)}};
    }

    public String getModelName() {
        // Extract the last word after the backslash in the model path
        String[] pathSegments = modelPath.split("\\\\");
        return pathSegments[pathSegments.length - 1];
    }

    public void setModel(ModelTriangulated model) {
        this.model = model;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }


    public ModelTriangulated getModel() {
        return model;
    }


    public String getModelPath() {
        return modelPath;
    }

    public ColorIntARGB[][] getTextureARGB() {
        return textureARGB;
    }

    public void setTextureARGB(ColorIntARGB[][] textureARGB) {
        this.textureARGB = textureARGB;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isDisableSmoothing() {
        return disableSmoothing;
    }

    public boolean getDisableTexture() {
        return disableTexture;
    }



    public void setDisableTexture(boolean disableTexture) {
        this.disableTexture = disableTexture;
    }

    public void setDisableSmoothing(boolean disableSmoothing) {
        this.disableSmoothing = disableSmoothing;
    }


    public boolean getDisableMesh() {
        return disableMesh;
    }

    public void setDisableMesh(boolean disableMesh) {
        this.disableMesh = disableMesh;
    }
}
