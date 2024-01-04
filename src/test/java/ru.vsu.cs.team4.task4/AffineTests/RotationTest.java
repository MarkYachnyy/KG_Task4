package ru.vsu.cs.team4.task4.AffineTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.Affine.AffineBuilder;
import ru.vsu.cs.team4.task4.Affine.affineComposite.Affine;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.math.vector.Vector4f;

public class RotationTest {
    @Test
    public void affineBuilderRotateTest1() throws Exception {
        Vector4f v = new Vector4f(-2.0f, 1.0f, 0.1f, 1.0f);
        double angle = Math.toRadians(1000);
        Affine affine = new AffineBuilder().rotateX(angle).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        float x = -2.0f;
        float y = (float) (Math.cos(angle) + 0.1 * Math.sin(angle));
        float z =  (float) (- Math.sin(angle) + 0.1 * Math.cos(angle));
        Vector3f expectedResult = new Vector3f(x, y ,z);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderRotateTest2() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().rotateX(Math.PI / 2).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(5.0f, 7.0f, -6.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderRotateTest3() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().rotateY(Math.PI / 2).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(7.0f, 6.0f, -5.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderRotateTest4() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().rotateZ(Math.PI / 2).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(6.0f, -5.0f, 7.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderRotateTest5() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().rotateX(0)
                .rotateY(0)
                .rotateZ(0)
                .build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(5.0f, 6.0f, 7.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderRotateTest6() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().rotateX(Math.PI / 2)
                .rotateY(Math.PI / 2)
                .rotateZ(Math.PI / 2)
                .build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(7.0f, 6.0f, -5.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderRotateTest7() throws Exception {
        Vector4f v = new Vector4f(-2.0f, 1.0f, 0.1f, 1.0f);
        double angle = Math.toRadians(99);
        Affine affine = new AffineBuilder().rotateY(angle).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        float x = (float) (-2.0 * Math.cos(angle) + 0.1 * Math.sin(angle));
        float y = 1.0f;
        float z = (float) (2 * Math.sin(angle) + 0.1 * Math.cos(angle));
        Vector3f expectedResult = new Vector3f(x, y ,z);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderRotateTest8() throws Exception {
        Vector4f v = new Vector4f(-2.0f, 1.0f, 0.1f, 1.0f);
        double angle = Math.toRadians(-36);
        Affine affine = new AffineBuilder().rotateZ(angle).build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        float x = (float) (-2.0 * Math.cos(angle) + Math.sin(angle));
        float y = (float) (2.0 * Math.sin(angle) + Math.cos(angle));
        float z = 0.1f;
        Vector3f expectedResult = new Vector3f(x, y ,z);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }
    @Test
    public void affineBuilderRotateTest9() throws Exception {
        Vector4f v = new Vector4f(-2.0f, 1.0f, 0.1f, 1.0f);
        Affine affine = new AffineBuilder().rotateX(Math.toRadians(90.0f))
                .rotateY(Math.toRadians(90.0f))
                .rotateZ(Math.toRadians(90.0f))
                .build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(0.1f, 1.0f, 2.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void rotateWithoutChangingTest1() throws Exception {
        Vector4f v = new Vector4f(-2.0f, 0.0f, 0.1f, 1.0f);
        Affine affine = new AffineBuilder().rotateX(Math.toRadians(360.0f))
                .rotateY(Math.toRadians(360.0f))
                .rotateZ(Math.toRadians(360.0f))
                .build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(-2.0f, 0.0f, 0.1f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void rotateWithoutChangingTest2() throws Exception {
        Vector4f v = new Vector4f(-2.0f, 0.0f, 0.1f, 1.0f);
        Affine affine = new AffineBuilder().rotateX(Math.toRadians(100.0f))
                .rotateX(Math.toRadians(-100.0f)).build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(-2.0f, 0.0f, 0.1f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

}
