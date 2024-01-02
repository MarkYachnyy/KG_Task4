package ru.vsu.cs.team4.task4;


import javafx.scene.control.CheckBox;
import ru.vsu.cs.team4.task4.model.Model;
import ru.vsu.cs.team4.task4.model.ModelTriangulated;


public class LoadedModel {

    private ModelTriangulated model;
    private String modelPath;
    private CheckBox isActive;
    private CheckBox isEditable;
    private int[][] textureARGB;

    public CheckBox getActivationCheckbox() {
        return isActive;
    }

    public boolean isActive(){
        return isActive.isSelected();
    }

    public LoadedModel(ModelTriangulated model, String modelPath) {
        this.model = model;
        this.modelPath = modelPath;
        this.textureARGB = new int[][]{{111111}};
    }

    public LoadedModel(ModelTriangulated model, String modelPath, CheckBox isActive) {
        this.model = model;
        this.modelPath = modelPath;
        this.isActive = isActive;
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

    public void setIsEditable(CheckBox isEditable) {
        this.isEditable = isEditable;
    }

    public CheckBox getIsEditable() {
        return isEditable;
    }

    public ModelTriangulated getModel() {
        return model;
    }

    public void setIsActive(CheckBox isActive) {
        this.isActive = isActive;
    }

    public String getModelPath() {
        return modelPath;
    }

    public int[][] getTextureARGB() {
        return textureARGB;
    }

    public void setTextureARGB(int[][] textureARGB) {
        this.textureARGB = textureARGB;
    }
}
