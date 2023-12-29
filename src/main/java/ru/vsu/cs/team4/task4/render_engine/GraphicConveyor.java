package ru.vsu.cs.team4.task4.render_engine;


import ru.vsu.cs.team4.task4.Affine.AffineBuilder.AffineBuilder;
import ru.vsu.cs.team4.task4.math.Point2f;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.math.vector.Vector4f;

public class GraphicConveyor {

    public static Matrix4f rotateScaleTranslate(Vector3f scaleV, Vector3f rotateV, Vector3f translateV) throws Exception {
        AffineBuilder affineBuilder = new AffineBuilder().scale().byVector(scaleV).close();
        affineBuilder.rotate().XYZ(rotateV).close();
        affineBuilder.translate().byVector(translateV);
        return affineBuilder.returnFinalMatrix();
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {

        Vector3f resultZ = Vector3f.sub(target, eye);
        Vector3f resultX = Vector3f.vectorMul(up, resultZ);
        Vector3f resultY = Vector3f.vectorMul(resultZ, resultX);

        resultX.normalize();
        resultY.normalize();
        resultZ.normalize();

        float[][] matrix = new float[][] {
                {resultX.getX(), resultY.getX(), resultZ.getX(),  -resultX.scalarMul(eye)},
                {resultX.getY(), resultY.getY(), resultZ.getY(), -resultY.scalarMul(eye)},
                {resultX.getZ(), resultY.getZ(), resultZ.getZ(), -resultZ.scalarMul(eye)},
                {0, 0, 0, 1}};
        return new Matrix4f(matrix);
    }

    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        return new Matrix4f(new float[][] {
                {tangentMinusOnDegree / aspectRatio, 0, 0, 0},
                {0, tangentMinusOnDegree, 0, 0},
                {0, 0, (farPlane + nearPlane) / (farPlane - nearPlane), 2 * farPlane * nearPlane / (nearPlane - farPlane)},
                {0, 0, 1, 0}});
    }

    /*public static Vector3f multiplyMatrix4ByVector3(final Matrix4f matrix, final Vector3f vertex) {
        final float x = (vertex.x * matrix.m00) + (vertex.y * matrix.m10) + (vertex.z * matrix.m20) + matrix.m30;
        final float y = (vertex.x * matrix.m01) + (vertex.y * matrix.m11) + (vertex.z * matrix.m21) + matrix.m31;
        final float z = (vertex.x * matrix.m02) + (vertex.y * matrix.m12) + (vertex.z * matrix.m22) + matrix.m32;
        final float w = (vertex.x * matrix.m03) + (vertex.y * matrix.m13) + (vertex.z * matrix.m23) + matrix.m33;
        return new Vector3f(x / w, y / w, z / w);
    }*/

    public static Vector3f multiplierMatrixToVector(Matrix4f matrix, Vector4f vector) {
        final Vector4f multipliedV = matrix.mulV(vector);
        return new Vector3f(
                multipliedV.getX() / multipliedV.getW(),
                multipliedV.getY() / multipliedV.getW(),
                multipliedV.getZ() / multipliedV.getW());
    }

    public static Point2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
        return new Point2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
    }
}
