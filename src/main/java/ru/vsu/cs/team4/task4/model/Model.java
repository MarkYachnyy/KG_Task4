package ru.vsu.cs.team4.task4.model;

import ru.vsu.cs.team4.task4.math.vector.Vector2f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector2f> textureVertices = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();
    private List<Polygon> polygons = new ArrayList<>();



    // Добавленные мной поля и методы
    // TODO: скопировать себе в проект все, что находится ниже



    private List<Group> groups = new ArrayList<>();

    public void addVertex(Vector3f vertex) {
        vertices.add(vertex);
    }

    public void addTextureVertex(Vector2f textureVertex) {
        textureVertices.add(textureVertex);
    }

    public void addNormal(Vector3f normal) {
        normals.add(normal);
    }

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public Polygon getFirstPolygon() {
        return polygons.get(0);
    }

    public void setVertices(List<Vector3f> vertices) {
        this.vertices = vertices;
    }

    public void setTextureVertices(List<Vector2f> textureVertices) {
        this.textureVertices = textureVertices;
    }

    public void setNormals(List<Vector3f> normals) {
        this.normals = normals;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public int getVerticesSize() {
        return vertices.size();
    }

    public int getTextureVerticesSize() {
        return textureVertices.size();
    }

    public int getNormalsSize() {
        return normals.size();
    }

    public int getPolygonsSize() {
        return polygons.size();
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Model(ArrayList<Vector3f> vertices, ArrayList<Vector2f> textureVertices, ArrayList<Vector3f> normals, ArrayList<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }

    public Model(Model model) {
        ArrayList<Vector3f> checkList = (ArrayList<Vector3f>) model.vertices;
        if (checkList != null) {
            this.vertices.addAll(model.vertices);
        }
        ArrayList<Vector2f> checkList2 = (ArrayList<Vector2f>) model.textureVertices;
        if (checkList2 != null) {
            this.textureVertices.addAll(model.textureVertices);
        }

        checkList = (ArrayList<Vector3f>) model.normals;
        if (checkList != null) {
            this.normals.addAll(model.normals);
        }

        ArrayList<Polygon> checkListP = (ArrayList<Polygon>) model.polygons;
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
