package ru.vsu.cs.team4.task4.AffineTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.Affine.AffineBuilder;
import ru.vsu.cs.team4.task4.Affine.affineComposite.Affine;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.math.vector.Vector4f;


public class ScaleTest {

    @Test
    public void affineBuilderScaleTest1() {
        Vector4f v = new Vector4f(-2.0f, 0.0f, 0.1f, 1);
        Affine affine = new AffineBuilder().scale(0.0f, 0.0f, 0.0f).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(0.0f, 0.0f, 0.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderScaleTest2() {
        Vector4f v = new Vector4f(5, 6, 7, 1);
        Affine affine = new AffineBuilder().scale(4, 3, 2).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(20, 18, 14);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderScaleTest3() {
        Vector4f v = new Vector4f(-2.0f, 0.0f, 0.1f, 1);
        Affine affine = new AffineBuilder().scale(2.0f, 0.0f, -9.1f).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(-4.0f, 0.0f, -0.91f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderFewScaleTest() {
        Vector4f v = new Vector4f(-2.0f, 1.0f, 0.1f, 1);
        Affine affine = new AffineBuilder().scale(2.0f, 1.0f, -9.1f)
                .scale(0.0f, 17.0f, -10.0f)
                .scale(3.0f, 0.5f, 2.0f).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(0.0f, 8.5f, 18.2f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void scaleWithoutChangingTest() {
        Vector4f v = new Vector4f(-2.0f, 0.0f, 0.1f, 1);
        Affine affine = new AffineBuilder().scale(1.0f, 1.0f, 1.0f).build();
        v = affine.getMatrix().mulV(v);
        Vector3f result = new Vector3f(v);
        Vector3f expectedResult = new Vector3f(-2.0f, 0.0f, 0.1f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

}