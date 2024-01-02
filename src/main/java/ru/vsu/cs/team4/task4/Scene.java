package ru.vsu.cs.team4.task4;


import java.util.ArrayList;
import java.util.List;

public class Scene {

    private List<LoadedModel> models;

    public Scene() {
        this.models = new ArrayList<>();
    }

    public void addModel(LoadedModel loadedModel){
        models.add(loadedModel);
    }

    public List<LoadedModel> getModels() {
        return models;
    }
}
