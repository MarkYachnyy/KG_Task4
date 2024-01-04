package cgvsu.matrix;


import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector4f;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Matrix4fTest {

    @Test
    void testSum() {
        Matrix4f m1 = new Matrix4f(new float[][] {{2.0f, -1.0f, 5.0f, 3.0f},
                {0.0f, 3.0f, -4.0f, -1.0f},
                {7.0f, 8.0f, 9.0f, -2.0f},
                {0.0f, -2.0f, 3.0f, 1.0f}});
        Matrix4f m2 = new Matrix4f(new float[][] {{3.0f, -5.0f, 0.0f, 1.0f},
                {1.0f, -2.0f, -1.0f, 0.0f},
                {5.0f, -6.0f, 3.0f, -2.0f},
                {-2.0f, -3.0f, 5.0f, 2.0f}});
        Matrix4f mRes = m1.sum(m2);
        assertTrue(Utils.equals(mRes,new Matrix4f(new float[][] {{5.0f, -6.0f, 5.0f, 4.0f},
                {1.0f, 1.0f, -5.0f, -1.0f},
                {12.0f, 2.0f, 12.0f, -4.0f},
                {-2.0f, -5.0f, 8.0f, 3.0f}})));
    }

    @Test
    void testSumMut() {
        Matrix4f matrix4f = new Matrix4f(new float[][] {{4, 4, 4, 4},
                {4, 4, 4, 4},
                {4, 4, 4, 4},
                {4, 4, 4, 4}});
        Matrix4f mat = new Matrix4f(new float[][] {{2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2}});
        matrix4f.sumMut(mat);
        assertTrue(Utils.equals(matrix4f, new Matrix4f(new float[][] {{6, 6, 6, 6},
                {6, 6, 6, 6},
                {6, 6, 6, 6},
                {6, 6, 6, 6}})));

    }


    @Test
    void testSub() {
        Matrix4f m1 = new Matrix4f(new float[][] {{2.0f, -1.0f, 5.0f, 3.0f},
                {0.0f, 3.0f, -4.0f, -1.0f},
                {7.0f, 8.0f, 9.0f, -2.0f},
                {0.0f, -2.0f, 3.0f, 1.0f}});
        Matrix4f m2 = new Matrix4f(new float[][] {{3.0f, -5.0f, 0.0f, 1.0f},
                {1.0f, -2.0f, -1.0f, 0.0f},
                {5.0f, -6.0f, 3.0f, -2.0f},
                {-2.0f, -3.0f, 5.0f, 2.0f}});
        Matrix4f mRes = m1.sub(m2);
        assertTrue(Utils.equals(mRes, new Matrix4f(new float[][] {{-1.0f, 4.0f, 5.0f, 2.0f},
                {-1.0f, 5.0f, -3.0f, -1.0f},
                {2.0f, 14.0f, 6.0f, 0.0f},
                {2.0f, 1.0f, -2.0f, -1.0f}})));
    }

    @Test
    void testSubMut() {
        Matrix4f matrix4f = new Matrix4f(new float[][] {{6, 6, 6, 6},
                {6, 6, 6, 6},
                {6, 6, 6, 6},
                {6, 6, 6, 6}});
        Matrix4f mat = new Matrix4f(new float[][] {{4, 4, 4, 4},
                {4, 4, 4, 4},
                {4, 4, 4, 4},
                {4, 4, 4, 4}});
        matrix4f.subMut(mat);
        assertTrue(Utils.equals(matrix4f, new Matrix4f(new float[][] {{2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2}})));

    }

    @Test
    void testMul() {
        Matrix4f m1 = new Matrix4f(new float[][] {{2.0f, -1.0f, 5.0f, 3.0f},
                {0.0f, 3.0f, -4.0f, -1.0f},
                {7.0f, 8.0f, 9.0f, -2.0f},
                {0.0f, -2.0f, 3.0f, 1.0f}});
        Matrix4f m2 = new Matrix4f(new float[][] {{3.0f, -5.0f, 0.0f, 1.0f},
                {1.0f, -2.0f, -1.0f, 0.0f},
                {5.0f, -6.0f, 3.0f, -2.0f},
                {-2.0f, -3.0f, 5.0f, 2.0f}});
        Matrix4f mRes = m1.mul(m2);
        assertTrue(Utils.equals(mRes, new Matrix4f(new float[][] {{24.0f, -47.0f, 31.0f, -2.0f},
                {-15.0f, 21.0f, -20.0f, 6.0f},
                {78.0f, -99.0f, 9.0f, -15.0f},
                {11.0f, -17.0f, 16.0f, -4.0f}})));
    }

    @Test
    void testMulMut() {
        Matrix4f matrix4f = new Matrix4f(new float[][] {{1, 1, 1, 1},
                {3, 3, 3, 3},
                {6, 6, 6, 6},
                {9, 9, 9, 9}});
        Matrix4f mat = new Matrix4f(new float[][] {{9, 9, 9, 9},
                {6, 6, 6, 6},
                {3, 3, 3, 3},
                {1, 1, 1, 1}});
        matrix4f.mulMut(mat);
        assertTrue(Utils.equals(matrix4f, new Matrix4f(new float[][]{{19, 19, 19, 19},
                {57, 57, 57, 57},
                {114, 114, 114, 114},
                {171, 171, 171, 171}})));
    }

    @Test
    void testTrs() {
        Matrix4f m1 = new Matrix4f(new float[][] {{0.0f, 1.0f, 2.0f, 3.0f},
                {4.0f, 5.0f, 6.0f, 7.0f},
                {8.0f, 9.0f, 10.0f, 11.0f},
                {12.0f, 13.0f, 14.0f, 15.0f}});
        Matrix4f mRes = m1.trs();
        assertTrue(Utils.equals(mRes, new Matrix4f(new float[][] {{0.0f, 4.0f, 8.0f, 12.0f},
                {1.0f, 5.0f, 9.0f, 13.0f},
                {2.0f, 6.0f, 10.0f, 14.0f},
                {3.0f, 7.0f, 11.0f, 15.0f}})));
    }

    @Test
    void testMulVector() {
        Matrix4f m1 = new Matrix4f(new float[][] {{3.0f, -2.0f, 5.0f, 0.0f},
                {1.0f, -5.0f, 7.0f, -4.0f},
                {-1.0f, -3.0f, 2.0f, 9.0f},
                {-2.0f, 5.0f, -10.0f, 11.0f}});
        Vector4f v = m1.mulV(new Vector4f(3.0f, -2.0f, 1.0f, 5.0f));
        assertTrue(Utils.equals(v, new Vector4f(18, 0, 50, 29)));
    }

    @Test
    void testDet1() {
        Matrix4f m1 = new Matrix4f(new float[][] {{2.0f, -1.0f, 5.0f, 3.0f},
                {0.0f, 3.0f, -4.0f, -1.0f},
                {7.0f, 8.0f, 9.0f, -2.0f},
                {0.0f, -2.0f, 3.0f, 1.0f}});
        assertTrue(Utils.epsEquals(31, m1.det()));
    }

    @Test
    void testDet2() {
        Matrix4f matrix4f = new Matrix4f(new float[][] {{6, 2, 3, 9},
                {2, 6, 2, 3},
                {3, 2, 6, 2},
                {9, 3, 2, 6}});
        assertEquals(-1235, matrix4f.det());
    }

    @Test
    void testInverseMatrix1() {
        Matrix4f m1 = new Matrix4f(new float[][] {{1.0f, 2.0f, 3.0f, 4.0f},
                {2.0f, 3.0f, 1.0f, 2.0f},
                {1.0f, 1.0f, 1.0f, -1.0f},
                {1.0f, 0.0f, -2.0f, -6.0f}});
        Matrix4f mRes = m1.inverseMatrix();
        assertTrue(Utils.equals(mRes, new Matrix4f(new float[][] {{22.0f, -6.0f, -26.0f, 17.0f},
                {-17.0f, 5.0f, 20.0f, -13.0f},
                {-1.0f, 0.0f, 2.0f, -1.0f},
                {4.0f, -1.0f, -5.0f, 3.0f}})));
    }

    @Test
    void testInverseMatrix2() {
        Matrix4f matrix4f = new Matrix4f(new float[][] {{0, 1, 3, 1},
                {2, 3, 5, 1},
                {3, 5, 7, 2},
                {1, 1, 1, 2}});

        assertTrue(Utils.equals(matrix4f.inverseMatrix(), new Matrix4f(new float[][] {{-0.75f, 1.75f, -1, 0.5f},
                {0, -2, 1.5f, -0.5f},
                {0.25f, 0.75f, -0.5f, 0},
                {0.25f, -0.25f, 0, 0.5f}})));

    }
}