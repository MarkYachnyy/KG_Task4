package ru.vsu.cs.team4.task4.vector;

import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector2f;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Vector2fTest {

    @Test
    void testSumVectors() {
        Vector2f v = new Vector2f(2.0f, 3.0f);
        Vector2f v1 = new Vector2f(3.0f, -4.0f);
        v.sum(v1);
        assertTrue(Utils.equals(v, new Vector2f(5.0f, -1.0f)));
    }

    @Test
    void testStaticSumVectors() {
        Vector2f v = new Vector2f(-5.0f, -7.0f);
        Vector2f v1 = new Vector2f(0.0f, 9.0f);
        Vector2f vRes = Vector2f.sum(v, v1);
        assertTrue(Utils.equals(vRes, new Vector2f(-5.0f, 2.0f)));
    }

    @Test
    void testSumWithValue() {
        Vector2f v = new Vector2f(-1.0f, 0.0f);
        v.sum(5.0f);
        assertTrue(Utils.equals(v, new Vector2f(4.0f, 5.0f)));
    }

    @Test
    void testStaticSumWithValue() {
        Vector2f v = new Vector2f(-5.0f, -7.0f);
        Vector2f vRes = Vector2f.sum(v, 3.0f);
        assertTrue(Utils.equals(vRes, new Vector2f(-2.0f, -4.0f)));
    }

    @Test
    void testSubVectors() {
        Vector2f v = new Vector2f(5.0f, 4.0f);
        Vector2f v1 = new Vector2f(3.0f, 4.0f);
        v.sub(v1);
        assertTrue(Utils.equals(v, new Vector2f(2.0f, 0.0f)));
    }

    @Test
    void testStaticSubVectors() {
        Vector2f v = new Vector2f(9.0f, -4.0f);
        Vector2f v1 = new Vector2f(3.0f, -5.0f);
        Vector2f vRes = Vector2f.sub(v, v1);
        assertTrue(Utils.equals(vRes, new Vector2f(6.0f, 1.0f)));
    }

    @Test
    void testSubWithValue() {
        Vector2f v = new Vector2f(3.0f, 5.0f);
        v.sub(4.0f);
        assertTrue(Utils.equals(v, new Vector2f(-1.0f, 1.0f)));
    }

    @Test
    void testStaticSubWithValue() {
        Vector2f v = new Vector2f(-10.0f, -3.0f);
        Vector2f vRes = Vector2f.sub(v, 7.0f);
        assertTrue(Utils.equals(vRes, new Vector2f(-17.0f, -10.0f)));
        assertTrue(Utils.equals(v, new Vector2f(-10.0f, -3.0f)));
    }

    @Test
    void testMulWithValue() {
        Vector2f v = new Vector2f(2.0f, -1.0f);
        v.mul(3.0f);
        assertTrue(Utils.equals(v, new Vector2f(6.0f, -3.0f)));
    }

    @Test
    void testStaticMulWithValue() {
        Vector2f v = new Vector2f(-2.0f, 4.0f);
        Vector2f vRes = Vector2f.mul(v, 3.0f);
        assertTrue(Utils.equals(vRes, new Vector2f(-6.0f, 12.0f)));
        assertTrue(Utils.equals(v, new Vector2f(-2.0f, 4.0f)));
    }

    @Test
    void testDivOnValue() {
        Vector2f v = new Vector2f(6.0f, -2.0f);
        v.div(-2.0f);
        assertTrue(Utils.equals(v, new Vector2f(-3.0f, 1.0f)));
    }

    @Test
    void testStaticDivOnValue() {
        Vector2f v = new Vector2f(-8.0f, 5.0f);
        Vector2f vRes = Vector2f.div(v, 4.0f);
        assertTrue(Utils.equals(vRes, new Vector2f(-2.0f, 1.25f)));
        assertTrue(Utils.equals(v, new Vector2f(-8.0f, 5.0f)));
    }

    @Test
    void testLength() {
        Vector2f v = new Vector2f(3.0f, -4.0f);
        assertTrue(Utils.epsEquals(5.0f, v.len()));
    }

    @Test
    void testNormalize() {
        Vector2f v = new Vector2f(3.0f, -4.0f);
        v.normalize();
        assertTrue(Utils.equals(v, new Vector2f(0.6f, -0.8f)));
    }

    @Test
    void testNormalizedVectorLength() {
        Vector2f v = new Vector2f(5.0f, -2.0f);
        v.normalize();
        assertTrue(Utils.epsEquals(1.0f, v.len()));
    }

    @Test
    void testScalarMulVectors() {
        Vector2f v = new Vector2f(3.0f, 0.0f);
        Vector2f v1 = new Vector2f(-5.0f, 2.0f);
        assertTrue(Utils.epsEquals(-15.0f, v.scalarMul(v1)));
    }
}
