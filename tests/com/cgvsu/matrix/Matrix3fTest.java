package cgvsu.matrix;

import org.junit.jupiter.api.Test;
import ru.vsu.cs.team4.task4.math.matrix.Matrix3f;
import ru.vsu.cs.team4.task4.math.utils.Utils;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Matrix3fTest {

    @Test
    void testSum() {
        Matrix3f m1 = new Matrix3f(new float[][] {{2.0f, -1.0f, 5.0f}, {0.0f, 3.0f, -4.0f}, {7.0f, 8.0f, 9.0f}});
        Matrix3f m2 = new Matrix3f(new float[][] {{3.0f, -5.0f, 0.0f}, {1.0f, -2.0f, -1.0f}, {10.0f, 7.0f, 11.0f}});
        Matrix3f mRes = m1.sum(m2);
        assertTrue(Utils.equals(mRes,new Matrix3f(new float[][]{{5.0f, -6.0f, 5.0f}, {1.0f, 1.0f, -5.0f}, {17.0f, 15.0f, 20.0f}})));
    }

    @Test
    void testSub() {
        Matrix3f m1 = new Matrix3f(new float[][] {{7.0f, -3.0f, 0.0f}, {2.0f, -1.0f, -8.0f}, {9.0f,-11.0f, 10.0f}});
        Matrix3f m2 = new Matrix3f(new float[][] {{5.0f, -2.0f, 0.0f}, {-1.0f, -2.0f, 3.0f}, {12.0f, -8.0f, 7.0f}});
        Matrix3f mRes = m1.sub(m2);
        assertTrue(Utils.equals(mRes, new Matrix3f(new float[][] {{2.0f, -1.0f, 0.0f}, {3.0f, 1.0f, -11.0f}, {-3.0f, -3.0f, 3.0f}})));
    }

    @Test
    void testMul() {
        Matrix3f m1 = new Matrix3f(new float[][] {{0.0f, -1.0f, 1.0f}, {-2.0f, 1.0f, -4.0f}, {3.0f,-7.0f, -2.0f}});
        Matrix3f m2 = new Matrix3f(new float[][] {{5.0f, -2.0f, -1.0f}, {-3.0f, -2.0f, 3.0f}, {5.0f, -8.0f, 6.0f}});
        Matrix3f mRes = m1.mul(m2);
        assertTrue(Utils.equals(mRes, new Matrix3f(new float[][] {{8.0f, -6.0f, 3.0f}, {-33.0f, 34.0f, -19.0f}, {26.0f, 24.0f, -36.0f}})));
    }

    @Test
    void testTrs() {
        Matrix3f m1 = new Matrix3f(new float[][] {{0.0f, -1.0f, 1.0f}, {-2.0f, 1.0f, -4.0f}, {3.0f, -7.0f, -2.0f}});
        Matrix3f mRes = m1.trs();
        assertTrue(Utils.equals(mRes, new Matrix3f(new float[][] {{0.0f, -2.0f, 3.0f}, {-1.0f, 1.0f, -7.0f}, {1.0f, -4.0f, -2.0f}})));
    }

    @Test
    void testMulVector() {
        Matrix3f m1 = new Matrix3f(new float[][] {{3.0f, -2.0f, 5.0f}, {0.0f, -5.0f, 7.0f}, {-1.0f, -3.0f, 2.0f}});
        Vector3f v = m1.mulV(new Vector3f(3.0f, -2.0f, 1.0f));
        assertTrue(Utils.equals(v, new Vector3f(18, 17, 5)));
    }

    @Test
    void testDet() {
        Matrix3f m1 = new Matrix3f(new float[][] {{0.0f, -1.0f, 1.0f}, {-2.0f, 1.0f, -4.0f}, {3.0f, -7.0f, -2.0f}});
        assertTrue(Utils.epsEquals(27, m1.det()));
    }

    @Test
    void testInverseMatrix() {
        Matrix3f m1 = new Matrix3f(new float[][] {{2.0f, 0.0f, -1.0f}, {1.0f, 5.0f, -4.0f}, {-1.0f, 1.0f, 0.0f}});
        Matrix3f mRes = m1.inverseMatrix();
        assertTrue(Utils.equals(mRes, new Matrix3f(new float[][] {{2.0f, -0.5f, 2.5f}, {2.0f, -0.5f, 3.5f}, {3.0f, -1.0f, 5.0f}})));
    }
}
