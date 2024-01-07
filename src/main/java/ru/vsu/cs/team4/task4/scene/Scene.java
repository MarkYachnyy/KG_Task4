package ru.vsu.cs.team4.task4.scene;


import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.scene.LoadedModel;

import java.util.ArrayList;
import java.util.List;

public class Scene {


    private ArrayList<LoadedModel> models;
    private List<String> activeModels = new ArrayList<>();
    private List<String> editableModels = new ArrayList<>();

    private Vector3f light;

    public Scene() {
        this.models = new ArrayList<>();
        this.light = new Vector3f(-1,-1,-1).normalized();
    }

    public void addModel(LoadedModel loadedModel){
        models.add(loadedModel);
    }

    public Vector3f getLight() {
        return light;
    }

    public List<LoadedModel> getModels() {
        return models;
    }

    public void addActiveModel(String id) {
        activeModels.add(id);
    }

    public List<String> getActiveModels() {
        return activeModels;
    }

    public List<String> getEditableModels() {
        return editableModels;
    }

    public void addEditableModel(String id) {
        editableModels.add(id);
    }

    public boolean containsActive(String id) {
        return activeModels.contains(id);
    }

    public boolean containsEditable(String id) {
        return editableModels.contains(id);
    }

    public LoadedModel getModelByID(String id) {
        return models.get(Integer.parseInt(id));
    }


}
