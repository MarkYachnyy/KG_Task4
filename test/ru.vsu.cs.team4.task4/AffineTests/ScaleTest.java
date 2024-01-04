package ru.vsu.cs.team4.task4.AffineTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.model.Model;

import java.util.ArrayList;

public class ScaleTest {
    public final double DELTA = 10e-15;

    @Test
    public void affineBuilderScaleTest1() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f scale = new Vector3f(0.0f, 0.0f, 0.0f);
        Model resultModel = new AffineBuilder().scale().byVector(scale).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(0.0f, 0.0f, 0.0f);

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void affineBuilderScaleTest2() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f scale = new Vector3f(2.0f, 0.0f, -9.1f);
        Model resultModel = new AffineBuilder().scale().byVector(scale).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(-4.0f, 0.0f, -0.91f);

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void affineBuilderFewScaleTest() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f scale1 = new Vector3f(2.0f, 1.0f, -9.1f);
        Vector3f scale2 = new Vector3f(0.0f, 17.0f, -10.0f);
        Model resultModel = new AffineBuilder().scale().byVector(scale1).byVector(scale2)
                .byZ(2.0f).byX(3.0f).byY(0.5f).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(0.0f, 8.5f, 18.2f);

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void scaleWithoutChangingTest() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder affineBuilder = new AffineBuilder();

        Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
        Model resultModel = affineBuilder.scale().byVector(scale).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void testScaleModelWithNullVector() {
        try {
            new AffineBuilder().scale().byVector(null).close()
                    .changeModel(new Model(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        } catch (NullPointerException ex) {
            String expectedError = "Exception in affine transformation: Vector is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }

    @Test
    public void testScaleMatrixWithNullVector() {
        try {
            AffineMatrix.scaleMatrix(null);
        } catch (NullPointerException ex) {
            String expectedError = "Exception in affine transformation: Vector is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }

    @Test
    public void testScaleMatrix() {
        Matrix4f matrix = AffineMatrix.scaleMatrix(new Vector3f(-0.67f, 123.0f, 0.0f));

        float[][] result = matrix.getValues();
        float[][] expectedResult = new float[][]{{-0.67f, 0.0f, 0.0f, 0.0f},
                                                    {0.0f, 123.0f, 0.0f, 0.0f},
                                                    {0.0f, 0.0f, 0.0f, 0.0f},
                                                    {0.0f, 0.0f, 0.0f, 1.0f}};

        int expectedLength = expectedResult.length;
        int expectedDepth = expectedResult[0].length;
        Assertions.assertEquals(result.length, expectedLength);
        Assertions.assertEquals(result[0].length, expectedDepth);

        for (int i = 0; i < expectedLength; i++) {
            for (int j = 0; j < expectedDepth; j++) {
                Assertions.assertEquals(expectedResult[i][j], result[i][j], DELTA);
            }
        }
    }
}