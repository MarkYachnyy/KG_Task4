package cgvsu.AffineTests;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.Affine.AffineBuilder.AffineBuilder;
import ru.vsu.cs.team4.task4.Affine.AffineExceptions;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.model.Model;

import java.util.ArrayList;

public class AffineBuilderTest {
    @Test
    public void vertexChangingTest() {
        ArrayList<Vector3f> vertex = new ArrayList<Vector3f>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model1 = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel1 = new AffineBuilder().scale().byVector(new Vector3f(3.0f, 2.0f, 1.0f))
                .close().returnChangedModel(model1);

        ArrayList<Vector3f> newVertexes = resultModel1.vertices;

        Vector3f expected = new Vector3f(-6.0f, 2.0f, 0.1f);

        for (Vector3f vertNum : newVertexes) {
            Assertions.assertTrue(Utils.equals(vertNum, expected));
        }
    }

    @Test
    public void normalsChangingTest() {

        ArrayList<Vector3f> normal = new ArrayList<Vector3f>();
        normal.add(new Vector3f(-4.0f, 2.0f, 0.2f));

        Model model2 = new Model(new ArrayList<>(), new ArrayList<>(), normal, new ArrayList<>());

        Model resultModel2 = new AffineBuilder().scale().byVector(new Vector3f(3.0f, 2.0f, 1.0f))
                .close().returnChangedModel(model2);

        ArrayList<Vector3f> newNormals = resultModel2.normals;

        Vector3f expected = new Vector3f(-12.0f, 4.0f, 0.2f);

        for (Vector3f normNum : newNormals) {
            Assertions.assertTrue(Utils.equals(normNum, expected));
        }
    }

    @Test
    public void nullModelChangingTest() {
        Model model = null;
        try {
            Model result = new AffineBuilder().returnChangedModel(model);
        } catch (AffineExceptions ex) {
            String expectedError = "Exception in affine transformation: Model is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }

    @Test
    public void emptyModelChangingTest() throws Exception {
        Model model = new Model(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Model result = new AffineBuilder().scale().byVector(new Vector3f(1.0f, 0.0f, -16.7f)).close().rotate().byX(90).close()
                .translate().byVector(new Vector3f(0.0f, -11.0f, 0.4f)).close().returnChangedModel(model);

        Assertions.assertEquals(result.vertices.size(), 0.0f);
        Assertions.assertEquals(result.textureVertices.size(), 0.0f);
        Assertions.assertEquals(result.normals.size(), 0.0f);
        Assertions.assertEquals(result.polygons.size(), 0.0f);
    }

    @Test
    public void emptyAffineBuilderReturnTest() {
        ArrayList<Vector3f> vertex = new ArrayList<Vector3f>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel = new AffineBuilder().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void testAffineBuilderOnModelWithNullVertexes1() {
        Model model = new Model(null, null, null, null);
        new AffineBuilder().changeModel(model);
        Assertions.assertNull(model.vertices);
        Assertions.assertNull(model.normals);
    }

    @Test
    public void testAffineBuilderOnModelWithNullVertexes2() {
        Model model = new Model(null, null, null, null);
        model = new AffineBuilder().returnChangedModel(model);
        Assertions.assertEquals(model.vertices.size(), 0);
        Assertions.assertEquals(model.normals.size(), 0);
    }

    @Test
    public void testAffineBuilderOnModelWithNullVertex() {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(null);
        vertex.add(new Vector3f(0.0f, 0.0f, 0.0f));
        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        try {
            new AffineBuilder().changeModel(model);
        } catch (AffineExceptions ex) {
            String expectedError = "Exception in affine transformation: Dot in model is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }

    @Test
    public void affinesWithoutChangingTest() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<Vector3f>();
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

        Vector3f translate = new Vector3f(0.0f, 0.0f, 0.0f);
        resultModel = affineBuilder.translate().byVector(translate).close().returnChangedModel(model);
        result = resultModel.vertices;

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }

        resultModel = affineBuilder.rotate().XYZ(new Vector3f(0.0f, 360.0f, -42.3f))
                .byZ( 42.3f).close().returnChangedModel(model);
        result = resultModel.vertices;

        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void differentAffines1() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<Vector3f>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder builder = new AffineBuilder();

        Model resultM1 = builder.scale().byX(2).close().
                rotate().byX(90).close().translate().byX(3).close().returnChangedModel(model);
        Model resultM2 = builder.translate().byX(3).close().scale().byX(2).close().
                rotate().byX(90).close().returnChangedModel(model);

        Vector3f result1 = resultM1.vertices.get(0);
        Vector3f result2 = resultM2.vertices.get(0);

        Assertions.assertFalse(Utils.equals(result1, result2));
    }
}
