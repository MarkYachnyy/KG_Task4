package ru.vsu.cs.team4.task4;


import javafx.scene.control.CheckBox;
import ru.vsu.cs.team4.task4.model.Model;


public class LoadedModel {

    private Model model;
    private String modelPath;
    private CheckBox isActive;
    private CheckBox isEditable;
    private String modelName;

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public CheckBox getIsActive() {
        return isActive;
    }

    public LoadedModel(Model model, String modelPath) {
        this.model = model;
        this.modelPath = modelPath;
    }

    public LoadedModel() {
        this.model= new Model();
        this.modelPath = new String();
        this.isActive = new CheckBox();
    }

    public LoadedModel(Model model, String modelPath, CheckBox isActive) {
        this.model = model;
        this.modelPath = modelPath;
        this.isActive = isActive;
    }


    public String getModelName() {
        // Extract the last word after the backslash in the model path
        String[] pathSegments = modelPath.split("\\\\");
        return pathSegments[pathSegments.length - 1];
    }

    public void setModel(Model model) {
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

    public void setIsActive(CheckBox isActive) {
        this.isActive = isActive;
    }

    public String getModelPath() {
        return modelPath;
    }
}
