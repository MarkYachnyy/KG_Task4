package ru.vsu.cs.team4.task4.vector;


import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector4f;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Vector4fTest {

    @Test
    void testSumVectors() {
        Vector4f v = new Vector4f(2.0f, 3.0f, -1.0f, 5.0f);
        Vector4f v1 = new Vector4f(3.0f, -4.0f, 0.0f, -1.0f);
        v.add(v1);
        assertTrue(Utils.equals(v, new Vector4f(5.0f, -1.0f, -1.0f, 4.0f)));
    }

    @Test
    void testStaticSumVectors() {
        Vector4f v = new Vector4f(3.0f, 0.0f, -7.0f, -11.0f);
        Vector4f v1 = new Vector4f(7.0f, 14.0f, 0.0f, -1.0f);
        Vector4f vRes = Vector4f.sum(v, v1);
        assertTrue(Utils.equals(vRes, new Vector4f(10.0f, 14.0f, -7.0f, -12.0f)));
        assertTrue(Utils.equals(v, new Vector4f(3.0f, 0.0f, -7.0f, -11.0f)));
    }

    @Test
    void testSumWithValue() {
        Vector4f v = new Vector4f(-1.0f, 0.0f, -3.0f, 7.0f);
        v.add(5.0f);
        assertTrue(Utils.equals(v, new Vector4f(4.0f, 5.0f, 2.0f, 12.0f)));
    }

    @Test
    void testStaticSumWithValue() {
        Vector4f v = new Vector4f(-5.6f, 0.0f, 8.0f, -12.0f);
        Vector4f vRes = Vector4f.sum(v, 13.2f);
        assertTrue(Utils.equals(vRes, new Vector4f(7.6f, 13.2f, 21.2f, 1.2f)));
    }

    @Test
    void testSubVectors() {
        Vector4f v = new Vector4f(8.0f, -2.0f, 5.0f, 3.0f);
        Vector4f v1 = new Vector4f(-9.0f, -3.0f, 0.0f, 2.0f);
        v.sub(v1);
        assertTrue(Utils.equals(v, new Vector4f(17.0f, 1.0f, 5.0f, 1.0f)));
    }

    @Test
    void testStaticSubVectors() {
        Vector4f v = new Vector4f(12.0f, -1.0f, 8.0f, -13.0f);
        Vector4f v1 = new Vector4f(9.0f, 18.0f, 5.5f, -1.2f);
        Vector4f vRes = Vector4f.sub(v, v1);
        assertTrue(Utils.equals(vRes, new Vector4f(3.0f, -19.0f, 2.5f, -11.8f)));
        assertTrue(Utils.equals(v, new Vector4f(12.0f, -1.0f, 8.0f, -13.0f)));
    }

    @Test
    void testSubWithValue() {
        Vector4f v = new Vector4f(0.0f, 3.0f, 4.0f, -2.0f);
        v.sub(4.0f);
        assertTrue(Utils.equals(v, new Vector4f(-4.0f, -1.0f, 0.0f, -6.0f)));
    }

    @Test
    void testStaticSubWithValue() {
        Vector4f v = new Vector4f(-13.2f, 2.1f, 6.9f, -7.0f);
        Vector4f vRes = Vector4f.sub(v, 8.1f);
        assertTrue(Utils.equals(vRes, new Vector4f(-21.3f, -6.0f, -1.2f, -15.1f)));
    }

    @Test
    void testMulWithValue() {
        Vector4f v = new Vector4f(2.0f, -1.0f, 0.0f, 5.0f);
        v.mul(3.0f);
        assertTrue(Utils.equals(v, new Vector4f(6.0f, -3.0f, 0.0f, 15.0f)));
    }

    @Test
    void testStaticMulWithValue() {
        Vector4f v = new Vector4f(-6.6f, 3.1f, -1.0f, 9.9f);
        Vector4f vRes = Vector4f.mul(v, -2.0f);
        assertTrue(Utils.equals(vRes, new Vector4f(13.2f, -6.2f, 2.0f, -19.8f)));
    }

    @Test
    void testDivOnValue() {
        Vector4f v = new Vector4f(-5.0f, 3.0f, -1.0f, 0.0f);
        v.div(2.0f);
        assertTrue(Utils.equals(v, new Vector4f(-2.5f, 1.5f, -0.5f, 0.0f)));
    }

    @Test
    void testStaticDivWithValue() {
        Vector4f v = new Vector4f(-7.6f, -2.5f, 9.0f, 17.3f);
        Vector4f vRes = Vector4f.div(v, 2.0f);
        assertTrue(Utils.equals(vRes, new Vector4f(-3.8f, -1.25f, 4.5f, 8.65f)));
    }

    @Test
    void testLength() {
        Vector4f v = new Vector4f(4.0f, -4.0f, 2.0f, -8.0f);
        assertTrue(Utils.epsEquals(10.0f, v.len()));
    }

    @Test
    void testNormalize() {
        Vector4f v = new Vector4f(4.0f, -4.0f, 2.0f, -8.0f);
        v.normalized();
        assertTrue(Utils.equals(v, new Vector4f(0.4f, -0.4f, 0.2f, -0.8f)));
    }

    @Test
    void testNormalizedVectorLength() {
        Vector4f v = new Vector4f(1.0f, 1.0f, 7.0f, 0.0f);
        v.normalized();
        assertTrue(Utils.epsEquals(1.0f, v.len()));
    }

    @Test
    void testScalarMulVectors() {
        Vector4f v = new Vector4f(3.0f, 0.0f, -2.0f, 7.0f);
        Vector4f v1 = new Vector4f(-5.0f, 2.0f, -1.0f, 1.0f);
        assertTrue(Utils.epsEquals(-6.0f, v.dotProduct(v1)));
    }
}
