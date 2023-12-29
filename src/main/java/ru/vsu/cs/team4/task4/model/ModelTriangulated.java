package ru.vsu.cs.team4.task4.model;

import ru.vsu.cs.team4.task4.math.Vector2f;
import ru.vsu.cs.team4.task4.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ModelTriangulated extends Model {

    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector2f> textureVertices = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();
    private List<Polygon> polygons = new ArrayList<>();

    public ModelTriangulated(Model model) {
        vertices.addAll(model.getVertices());
        textureVertices.addAll(model.getTextureVertices());
        normals.addAll(model.getNormals());

        for (Polygon polygon : model.getPolygons()) {
            List<Integer> vertexIndices = polygon.getVertexIndices();
            List<Integer> textureIndices = polygon.getTextureVertexIndices();
            List<Integer> normalIndices = polygon.getNormalIndices();

            if (vertexIndices.size() >= 3) {
                boolean hasTexture = textureIndices.size() >= 3;
                boolean hasNormal = normalIndices.size() >= 3;

                int firstVertex = vertexIndices.get(0);
                int firstTextureVertex = hasTexture ? textureIndices.get(0) : 0;
                int firstNormal = hasNormal ? normalIndices.get(0) : 0;
                for (int i = 1; i < vertexIndices.size() - 1; i++) {
                    TriangularPolygon triangle = new TriangularPolygon();
                    triangle.setVertexIndices(new int[]{firstVertex, vertexIndices.get(i), vertexIndices.get(i + 1)});

                    if (hasTexture) {
                        triangle.setTextureVertexIndices(new int[]{firstTextureVertex, textureIndices.get(i), textureIndices.get(i + 1)});
                    }

                    if (hasNormal) {
                        triangle.setNormalIndices(new int[]{firstNormal, normalIndices.get(i), normalIndices.get(i + 1)});
                    }

                    polygons.add(triangle);
                }
            }
        }
    }

    @Override
    public List<Vector3f> getVertices() {
        return vertices;
    }

    @Override
    public List<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    @Override
    public List<Vector3f> getNormals() {
        return normals;
    }

    @Override
    public List<Polygon> getPolygons() {
        return polygons;
    }

    @Override
    public int getTextureVerticesSize() {
        return textureVertices.size();
    }

    @Override
    public int getVerticesSize() {
        return vertices.size();
    }

    @Override
    public int getNormalsSize() {
        return normals.size();
    }

    @Override
    public int getPolygonsSize() {
        return polygons.size();
    }


}
