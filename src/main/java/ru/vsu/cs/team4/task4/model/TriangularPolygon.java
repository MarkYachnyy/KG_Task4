package ru.vsu.cs.team4.task4.model;

import ru.vsu.cs.team4.task4.objio.exceptions.FaceWordIndexException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TriangularPolygon extends Polygon {
    private int[] vertexIndices;
    private int[] textureVertexIndices;
    private int[] normalIndices;
    private int lineIndex;

    public TriangularPolygon() {
        vertexIndices = new int[0];
        textureVertexIndices = new int[0];
        normalIndices = new int[0];
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices.length != 0 ? Arrays.stream(vertexIndices)
                .boxed()
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    public void setVertexIndices(List<Integer> vertexIndices) {
        this.vertexIndices = vertexIndices.stream()
                .limit(3)
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public void setVertexIndices(int[] vertexIndices) {
        this.vertexIndices = vertexIndices;
    }

    public List<Integer> getTextureVertexIndices() {
        return textureVertexIndices.length != 0 ? Arrays.stream(textureVertexIndices)
                .boxed()
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    public void setTextureVertexIndices(List<Integer> textureVertexIndices) {
        this.textureVertexIndices = textureVertexIndices.stream()
                .limit(3)
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public void setTextureVertexIndices(int[] textureVertexIndices) {
        this.textureVertexIndices = textureVertexIndices;
    }

    public List<Integer> getNormalIndices() {
        return normalIndices.length != 0 ? Arrays.stream(normalIndices)
                .boxed()
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    public void setNormalIndices(List<Integer> normalIndices) {
        if (normalIndices.size() >= 3) {
            this.normalIndices = normalIndices.stream()
                    .limit(3)
                    .mapToInt(Integer::intValue)
                    .toArray();
        } else {
            this.normalIndices = new int[0];
        }
    }

    public void setNormalIndices(int[] normalIndices) {
        this.normalIndices = normalIndices;
    }

    public boolean hasTexture() {
        return textureVertexIndices.length > 0;
    }

    public void checkIndices(int verticesSize, int textureVerticesSize, int normalsSize) {
        for (int i = 0; i < vertexIndices.length; i++) {
            int vertexIndex = vertexIndices[i];
            if (vertexIndex >= verticesSize || vertexIndex < 0) {
                throw new FaceWordIndexException("vertex", lineIndex, i + 1);
            }
        }

        for (int i = 0; i < textureVertexIndices.length; i++) {
            int textureVertexIndex = textureVertexIndices[i];
            if (textureVertexIndex >= textureVerticesSize || textureVertexIndex < 0) {
                throw new FaceWordIndexException("texture vertex", lineIndex, i + 1);
            }
        }

        for (int i = 0; i < normalIndices.length; i++) {
            int normalIndex = normalIndices[i];
            if (normalIndex >= normalsSize || normalIndex < 0) {
                throw new FaceWordIndexException("normal", lineIndex, i + 1);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriangularPolygon triangle = (TriangularPolygon) o;
        return Arrays.equals(vertexIndices, triangle.vertexIndices) &&
                Arrays.equals(textureVertexIndices, triangle.textureVertexIndices) &&
                Arrays.equals(normalIndices, triangle.normalIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexIndices, textureVertexIndices, normalIndices);
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }
}
