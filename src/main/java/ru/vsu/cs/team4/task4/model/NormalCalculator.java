package ru.vsu.cs.team4.task4.model;

import ru.vsu.cs.team4.task4.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class NormalCalculator {

    public static List<Vector3f> recalculateNormals(List<Vector3f> vertices, List<Polygon> polygons) {
        ArrayList<Vector3f> res = new ArrayList<>();

        ArrayList<ArrayList<Vector3f>> vertexNormalAccumulator = new ArrayList<>(vertices.size());
        for (int i = 0; i < vertices.size(); i++) {
            vertexNormalAccumulator.add(new ArrayList<>());
        }

        for (int i = 0; i < polygons.size(); i++) {
            Polygon polygon = polygons.get(i);
            Vector3f normal = calculateNormalForPolygon(polygon, vertices);
            for (Integer verId : polygon.getVertexIndices()) {
                vertexNormalAccumulator.get(verId).add(normal);
            }
        }

        for (ArrayList<Vector3f> normalsAround : vertexNormalAccumulator) {
            Vector3f vertexNormal = normalsAround.stream().
                    collect(() -> new Vector3f(0, 0, 0), Vector3f::add, Vector3f::add).
                    multiply(1f / normalsAround.size()).normalized();

            res.add(vertexNormal);
        }

        return res;
    }

    public static Vector3f calculateNormalForPolygon(Polygon polygon, List<Vector3f> vertices) {
        int ver1Id = polygon.getVertexIndices().get(0);
        int ver2Id = polygon.getVertexIndices().get(1);
        int ver3Id = polygon.getVertexIndices().get(2);
        Vector3f vec1 = Vector3f.residual(vertices.get(ver2Id), vertices.get(ver1Id));
        Vector3f vec2 = Vector3f.residual(vertices.get(ver3Id), vertices.get(ver1Id));
        return Vector3f.crossProduct(vec1, vec2).normalized();
    }
}
