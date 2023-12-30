package cgvsu.AffineTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.Affine.AffineBuilder.AffineBuilder;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.model.Model;

import java.util.ArrayList;

public class RotationTest {
    @Test
    public void affineBuilderRotateTest1() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel = new AffineBuilder().rotate().byX(1000).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        double angle = Math.toRadians(1000);
        float x = -2.0f;
        float y = (float) (Math.cos(angle) + 0.1 * Math.sin(angle));
        float z =  (float) (- Math.sin(angle) + 0.1 * Math.cos(angle));
        Vector3f expected = new Vector3f(x, y ,z);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f,expected));
        }
    }

    @Test
    public void affineBuilderRotateTest2() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel = new AffineBuilder().rotate().byY(99).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        double angle = Math.toRadians(99);
        float x = (float) (-2.0 * Math.cos(angle) + 0.1 * Math.sin(angle));
        float y = 1.0f;
        float z = (float) (2 * Math.sin(angle) + 0.1 * Math.cos(angle));
        Vector3f expected = new Vector3f(x, y ,z);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void affineBuilderRotateTest3() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Model resultModel = new AffineBuilder().rotate().byZ(-36).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        double angle = Math.toRadians(-36);
        float x = (float) (-2.0 * Math.cos(angle) + Math.sin(angle));
        float y = (float) (2.0 * Math.sin(angle) + Math.cos(angle));
        float z = 0.1f;
        Vector3f expected = new Vector3f(x, y ,z);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }
    @Test
    public void affineBuilderRotateTest4() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().XYZ(rotate).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(0.1f, 1.0f, 2.0f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void affineBuilderRotateTest5() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().XZY(rotate).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(-1.0f, 2.0f, -0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void affineBuilderRotateTest6() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().YXZ(rotate).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(2.0f, -0.1f, -1.0f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void affineBuilderRotateTest7() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().YZX(rotate).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(1.0f, 2.0f, 0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void affineBuilderRotateTest8() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().ZXY(rotate).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(-2.0f, 0.1f, -1.0f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void affineBuilderRotateTest9() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 90.0f, 90.0f);
        Model resultModel = new AffineBuilder().rotate().ZYX(rotate).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(0.1f, -1.0f, -2.0f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void affineBuilderRotateTest10() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 1.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Vector3f rotate = new Vector3f(90.0f, 0.0f, 0.0f);
        Model resultModel1 = new AffineBuilder().rotate().ZYX(rotate).close().returnChangedModel(model);
        ArrayList<Vector3f> result1 = resultModel1.vertices;
        Model resultModel2 = new AffineBuilder().rotate().YZX(rotate).close().returnChangedModel(model);
        ArrayList<Vector3f> result2 = resultModel2.vertices;

        Assertions.assertEquals(result1.size(), result2.size());
        for (int i = 0; i < result1.size(); i++) {
            Assertions.assertTrue(Utils.equals(result1.get(i), result2.get(i)));
        }
    }

    @Test
    public void rotateWithoutChangingTest1() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder affineBuilder = new AffineBuilder();

        Model resultModel = affineBuilder.rotate().XZY(new Vector3f(360.0f, 360.0f, 360.0f)).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void rotateWithoutChangingTest2() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder affineBuilder = new AffineBuilder();

        Model resultModel = affineBuilder.rotate().byX(100.0f).byX(-100.0f).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }

    @Test
    public void rotateWithoutChangingTest3() throws Exception {
        ArrayList<Vector3f> vertex = new ArrayList<>();
        vertex.add(new Vector3f(-2.0f, 0.0f, 0.1f));

        Model model = new Model(vertex, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        AffineBuilder affineBuilder = new AffineBuilder();

        Model resultModel = affineBuilder.rotate().byX(120).byX(120).XZY(new Vector3f(120, 0, 0)).close().returnChangedModel(model);
        ArrayList<Vector3f> result = resultModel.vertices;

        Vector3f expected = new Vector3f(-2.0f, 0.0f, 0.1f);

        Assertions.assertEquals(result.size(), vertex.size());
        for (Vector3f vector3f : result) {
            Assertions.assertTrue(Utils.equals(vector3f, expected));
        }
    }
    @Test
    public void testRotateModelWithNullVector() {
        try {
            new AffineBuilder().rotate().XZY(null).close()
                    .changeModel(new Model(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        } catch (Exception ex) {
            String expectedError = "Exception in affine transformation: Angles vector is null";
            Assertions.assertEquals(expectedError, ex.getMessage());
        }
    }
}
