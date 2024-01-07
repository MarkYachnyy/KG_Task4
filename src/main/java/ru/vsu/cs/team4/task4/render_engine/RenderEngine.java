package ru.vsu.cs.team4.task4.render_engine;

import ru.vsu.cs.team4.task4.affine.AffineBuilder;
import ru.vsu.cs.team4.task4.affine.affineComposite.Affine;
import ru.vsu.cs.team4.task4.affine.affineComposite.Scale;
import ru.vsu.cs.team4.task4.rasterization.ColorIntARGB;
import ru.vsu.cs.team4.task4.rasterization.PolygonVertex;
import ru.vsu.cs.team4.task4.scene.LoadedModel;
import ru.vsu.cs.team4.task4.scene.Scene;
import ru.vsu.cs.team4.task4.math.Point2f;
import ru.vsu.cs.team4.task4.math.matrix.Matrix4f;
import ru.vsu.cs.team4.task4.math.vector.Vector2f;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;
import ru.vsu.cs.team4.task4.model.ModelTriangulated;
import ru.vsu.cs.team4.task4.model.Polygon;
import ru.vsu.cs.team4.task4.rasterization.ZBufferPixelWriter;
import ru.vsu.cs.team4.task4.rasterization.Rasterization;

import java.util.Arrays;
import java.util.Locale;

public class RenderEngine {
    private static void renderModel(final int[] buffer, int width, int height,
                                    final Matrix4f modelViewProjectionMatrix,
                                    final ModelTriangulated mesh, ColorIntARGB[][] textureARGB, float[][] Z,
                                    Vector3f light, boolean disableMesh, boolean disableSmoothing) {

        for (Polygon polygon : mesh.getPolygons()) {
            Vector3f v1NonProjected = mesh.getVertices().get(polygon.getVertexIndices().get(0));
            Vector3f v2NonProjected = mesh.getVertices().get(polygon.getVertexIndices().get(1));
            Vector3f v3NonProjected = mesh.getVertices().get(polygon.getVertexIndices().get(2));

            Vector3f v1 = GraphicConveyor.multiplyMVPMatrixByVertex(modelViewProjectionMatrix, v1NonProjected);
            Vector3f v2 = GraphicConveyor.multiplyMVPMatrixByVertex(modelViewProjectionMatrix, v2NonProjected);
            Vector3f v3 = GraphicConveyor.multiplyMVPMatrixByVertex(modelViewProjectionMatrix, v3NonProjected);


            if(!(v1.getX() > -1 && v1.getX() < 1 && v1.getY() > -1 && v1.getY() < 1 && v1.getZ() > -1 && v1.getZ() < 1 ||
                    v2.getX() > -1 && v2.getX() < 1 && v2.getY() > -1 && v2.getY() < 1 && v2.getZ() > -1 && v2.getZ() < 1 ||
                    v3.getX() > -1 && v3.getX() < 1 && v3.getY() > -1 && v3.getY() < 1 && v3.getZ() > -1 && v3.getZ() < 1)){
                return;
            }

            Point2f p1 = GraphicConveyor.vertexToPoint(v1, width, height);
            Point2f p2 = GraphicConveyor.vertexToPoint(v2, width, height);
            Point2f p3 = GraphicConveyor.vertexToPoint(v3, width, height);

            Vector2f vt1 = mesh.getTextureVertices().get(polygon.getTextureVertexIndices().get(0));
            Vector2f vt2 = mesh.getTextureVertices().get(polygon.getTextureVertexIndices().get(1));
            Vector2f vt3 = mesh.getTextureVertices().get(polygon.getTextureVertexIndices().get(2));

            Vector3f n1 = mesh.getNormals().get(polygon.getNormalIndices().get(0));
            Vector3f n2 = mesh.getNormals().get(polygon.getNormalIndices().get(1));
            Vector3f n3 = mesh.getNormals().get(polygon.getNormalIndices().get(2));

            PolygonVertex pv1 = new PolygonVertex(p1.getX(), p1.getY(), v1.getZ(), vt1.getX(), vt1.getY(), n1);
            PolygonVertex pv2 = new PolygonVertex(p2.getX(), p2.getY(), v2.getZ(), vt2.getX(), vt2.getY(), n2);
            PolygonVertex pv3 = new PolygonVertex(p3.getX(), p3.getY(), v3.getZ(), vt3.getX(), vt3.getY(), n3);

            ZBufferPixelWriter pixelWriter = (x, y, z, color) -> {
                if (x < width && y < height && x > 0 && y > 0) {
                    if (z < Z[x][y]) {
                        Z[x][y] = z;
                        buffer[x + width * y] = color;
                    }
                }
            };

            if (disableMesh) {
                Rasterization.fillPolygon(pixelWriter, pv1, pv2, pv3, light, 0.5f, textureARGB, c -> c,
                        disableSmoothing);
            } else {
                Rasterization.fillPolygon(pixelWriter, pv1, pv2, pv3, light, 0.5f, textureARGB, new ColorIntARGB(255,
                                0, 0, 0).toInt(),
                        disableSmoothing);
            }


        }
    }


    public static void renderScene(final int[] buffer, int width, int height,
                                   final Scene scene) throws Exception {

        Camera activeCamera = scene.getActiveCamera();

        float[][] Z = new float[width][height];
        for (float[] row : Z) {
            Arrays.fill(row, Float.POSITIVE_INFINITY);
        }

        Matrix4f viewMatrix = GraphicConveyor.lookAt(activeCamera.getPosition(), activeCamera.getTarget());
        Matrix4f projectionMatrix = GraphicConveyor.perspective(activeCamera.getFov(), activeCamera.getAspectRatio(), activeCamera.getNearPlane(), activeCamera.getFarPlane());

        for (LoadedModel loadedModel : scene.getModels()) {
            if (scene.containsActive(loadedModel.getId())) {
                Matrix4f modelMatrix = GraphicConveyor.rotateScaleTranslate((loadedModel.getScaleV()), loadedModel.getRotateV(),
                        loadedModel.getTranslateV());

                Matrix4f modelViewProjectionMatrix = new Matrix4f(projectionMatrix.getValues());
                modelViewProjectionMatrix.mulMut(viewMatrix);
                modelViewProjectionMatrix.mulMut(modelMatrix);
                renderModel(buffer, width, height, modelViewProjectionMatrix, loadedModel.getModel(),
                        loadedModel.getTextureARGB(), Z, scene.getLight(), loadedModel.getDisableMesh(), loadedModel.isDisableSmoothing());

            }
        }

        for(Camera camera: scene.getCameras()){
            if(camera != activeCamera){
                Vector3f position = camera.getPosition();
                double horizontalLen = Math.sqrt(position.getX() * position.getX() + position.getZ() * position.getZ());
                double thetaCustom = Math.acos(-horizontalLen / position.len());
                if(position.getY() > 0) thetaCustom *= -1;
                double thetaY = Math.acos(position.getZ() / horizontalLen);
                if(position.getX() < 0) thetaY *= -1;
                Affine affine = new AffineBuilder().
                        rotateY(thetaY).
                        rotateCustom(new Vector3f(-camera.getPosition().getZ(), 0, camera.getPosition().getX()), thetaCustom).
                        move(position.getX(), position.getY(), position.getZ()).
                        build();

                Matrix4f modelViewProjectionMatrix = new Matrix4f(projectionMatrix.getValues());
                modelViewProjectionMatrix.mulMut(viewMatrix);
                modelViewProjectionMatrix.mulMut(affine.getMatrix());

                LoadedModel model = new LoadedModel(PreloadedModels.sceneCamera(), "");

                renderModel(buffer, width, height, modelViewProjectionMatrix, model.getModel(), new ColorIntARGB[][]{{new ColorIntARGB(255, 255,255,255)}}, Z, scene.getLight());
            }
        }
    }

}
