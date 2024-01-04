package ru.vsu.cs.team4.task4;


import ru.vsu.cs.team4.task4.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private List<LoadedModel> models;
    private Vector3f light;

    public Scene() {
        this.models = new ArrayList<>();
        this.light = new Vector3f(0,-1,0).normalized();
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
}
