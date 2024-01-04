package ru.vsu.cs.team4.task4.AffineTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.Affine.AffineBuilder;
import ru.vsu.cs.team4.task4.Affine.affineComposite.Affine;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.math.vector.Vector4f;

public class TranslationTest {

    @Test
    public void affineBuilderTranslateTest1() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().move(5, 5, 5).build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(10.0f, 11.0f, 12.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderTranslateTest2() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().move(0, 0, 0).build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(5.0f, 6.0f, 7.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderTranslateTest3() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().move(-5, -5, -5).build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(0.0f, 1.0f, 2.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderTranslateTest4() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().move(0, 0, 5).build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(5.0f, 6.0f, 12.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderTranslateTest5() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().move(0, 5, 0).build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(5.0f, 11.0f, 7.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

    @Test
    public void affineBuilderTranslateTest6() {
        Vector4f v = new Vector4f(5.0f, 6.0f, 7.0f, 1.0f);
        Affine affine = new AffineBuilder().move(5, 0, 0).build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(10.0f, 6.0f, 7.0f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }


    @Test
    public void affineBuilderTranslateTest7() {
        Vector4f v = new Vector4f(-2.0f, 0.0f, 0.1f, 1.0f);
        Affine affine = new AffineBuilder().move(2.0f, -0.133f, 0.0f).build();
        Vector3f result = new Vector3f(affine.getMatrix().mulV(v));
        Vector3f expectedResult = new Vector3f(0.0f, -0.133f, 0.1f);
        Assertions.assertTrue(Utils.equals(result, expectedResult));
    }

}
