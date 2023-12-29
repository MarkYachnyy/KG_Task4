package ru.vsu.cs.team4.task4.model;


import ru.vsu.cs.team4.task4.math.vector.Vector2f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;

import java.util.ArrayList;


public class Model {

    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();
    public ArrayList<Polygon> polygons = new ArrayList<>();

    public Model(ArrayList<Vector3f> vertices, ArrayList<Vector2f> textureVertices, ArrayList<Vector3f> normals, ArrayList<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }

    public Model(Model model) {
        ArrayList<Vector3f> checkList = model.vertices;
        if (checkList != null) {
            this.vertices.addAll(model.vertices);
        }
        ArrayList<Vector2f> checkList2 = model.textureVertices;
        if (checkList2 != null) {
            this.textureVertices.addAll(model.textureVertices);
        }

        checkList = model.normals;
        if (checkList != null) {
            this.normals.addAll(model.normals);
        }

        ArrayList<Polygon> checkListP = model.polygons;
        if (checkListP != null) {
            this.polygons.addAll(model.polygons);
        }
    }

    public Model() {}

    public Vector3f getCenter() {
        Vector3f center = new Vector3f();
        for (Vector3f v : this.vertices) {
            Vector3f.sum(center, v);
        }
        Vector3f.div(center, this.vertices.size());
        return center;
    }

    public float getMaxDistanceFromCenter() {
        Vector3f center = this.getCenter();
        float maxLength = 0;
        for (Vector3f v : vertices) {
            v.sub(center);
            float thisLength = v.len();
            maxLength = Math.max(thisLength, maxLength);
        }

        return maxLength;
    }
}
