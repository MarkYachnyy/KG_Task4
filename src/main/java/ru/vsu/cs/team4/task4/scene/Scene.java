package ru.vsu.cs.team4.task4.scene;


import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.render_engine.Camera;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scene {


    private ArrayList<LoadedModel> models;
    private List<String> activeModels = new ArrayList<>();
    private List<String> editableModels = new ArrayList<>();

    private Set<Camera> cameras;
    private int activeCameraId;
    private Vector3f light;

    public Scene() {
        this.cameras = new HashSet<>();
        this.addCamera();
        this.activeCameraId = 1;
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



    public Set<Camera> getCameras() {
        return cameras;
    }

    public Camera getActiveCamera() {
        for(Camera camera: cameras){
            if(camera.getId() == activeCameraId) return camera;
        }
        return null;
    }

    public void addCamera(){
        int newId = this.cameras.isEmpty() ? 1 :
                cameras.stream().mapToInt(Camera::getId).max().getAsInt() + 1;
        Camera camera = new Camera(newId,
                new Vector3f(100, 100, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 1F, 250);
        cameras.add(camera);
    }

    public void setActiveCameraId(int id){
        this.activeCameraId = id;
    }

}
