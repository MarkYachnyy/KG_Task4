package ru.vsu.cs.team4.task4.model;

import ru.vsu.cs.team4.task4.objio.exceptions.FaceWordIndexException;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private ArrayList<Integer> vertexIndices;
    private ArrayList<Integer> textureVertexIndices;
    private ArrayList<Integer> normalIndices;
    private int lineIndex;


    public Polygon() {
        vertexIndices = new ArrayList<Integer>();
        textureVertexIndices = new ArrayList<Integer>();
        normalIndices = new ArrayList<Integer>();
    }

    public boolean hasTexture(){
        return textureVertexIndices == null || textureVertexIndices.size() == 0;
    }

    public void setVertexIndices(List<Integer> vertexIndices) {
        assert vertexIndices.size() >= 3;
        this.vertexIndices = (ArrayList<Integer>) vertexIndices;
    }

    public void setTextureVertexIndices(List<Integer> textureVertexIndices) {
        assert textureVertexIndices.size() >= 3;
        this.textureVertexIndices = (ArrayList<Integer>) textureVertexIndices;
    }

    public void setNormalIndices(List<Integer> normalIndices) {
        assert normalIndices.size() >= 3;
        this.normalIndices = (ArrayList<Integer>) normalIndices;
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public List<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public List<Integer> getNormalIndices() {
        return normalIndices;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public void checkIndices(int verticesSize, int textureVerticesSize, int normalsSize) {
        for (int i = 0; i < vertexIndices.size(); i++) {
            int vertexIndex = vertexIndices.get(i);
            if (vertexIndex >= verticesSize || vertexIndex < 0) {
                throw new FaceWordIndexException("vertex", lineIndex, i + 1);
            }
        }

        for (int i = 0; i < textureVertexIndices.size(); i++) {
            int textureVertexIndex = textureVertexIndices.get(i);
            if (textureVertexIndex >= textureVerticesSize || textureVertexIndex < 0) {
                throw new FaceWordIndexException("texture vertex", lineIndex, i + 1);
            }
        }

        for (int i = 0; i < normalIndices.size(); i++) {
            int normalIndex = normalIndices.get(i);
            if (normalIndex >= normalsSize || normalIndex < 0) {
                throw new FaceWordIndexException("normal", lineIndex, i + 1);
            }
        }
    }
}
