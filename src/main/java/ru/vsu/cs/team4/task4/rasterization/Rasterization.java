package ru.vsu.cs.team4.task4.rasterization;

import ru.vsu.cs.team4.task4.math.vector.Vector;
import ru.vsu.cs.team4.task4.math.vector.Vector3f;

import java.util.function.Function;

public class Rasterization {

    public static void fillPolygon(ZBufferPixelWriter pixelWriter, PolygonVertex v1, PolygonVertex v2, PolygonVertex v3, Vector3f normalToPolygon, ColorIntARGB[][] textureARGB, Vector3f light,
                                   float ambient, Function<Integer, Integer> borderColorTransfiguration, boolean disableSmoothing) {

        if (v1.getY() > v2.getY()) {
            PolygonVertex temp = v1;
            v1 = v2;
            v2 = temp;
        }

        if (v1.getY() > v3.getY()) {
            PolygonVertex temp = v1;
            v1 = v3;
            v3 = temp;
        }

        if (v2.getY() > v3.getY()) {
            PolygonVertex temp = v2;
            v2 = v3;
            v3 = temp;
        }

        int x1 = v1.getX(), y1 = v1.getY(), x2 = v2.getX(), y2 = v2.getY(), x3 = v3.getX(), y3 = v3.getY();
        float z1 = v1.getZ(), z2 = v2.getZ(), z3 = v3.getZ();
        float tx1 = v1.getTx(), ty1 = v1.getTy(), tx2 = v2.getTx(), ty2 = v2.getTy(), tx3 = v3.getTx(), ty3 = v3.getTy();
        Vector3f n1 = v1.getNormal(), n2 = v2.getNormal(), n3 = v3.getNormal();

        int S = x2 * y3 + x3 * y1 + x1 * y2 - y1 * x2 - y2 * x3 - x1 * y3;

        float A = (y2 - y1) * (z3 - z1) - (y3 - y1) * (z2 - z1);
        float B = (z2 - z1) * (x3 - x1) - (x2 - x1) * (z3 - z1);
        float C = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        float D = -A * x1 - B * y1 - C * z1;

        float kNotSmoothed = 0;
        if (disableSmoothing) {
            float dp0 = -normalToPolygon.dotProduct(light);
            if (dp0 < 0) dp0 = 0;
            kNotSmoothed = ambient + (1 - ambient) * dp0;
        }

        int dxL, dyL, dxR, dyR, yL, yR, xL, xR;
        int dxL_sign, dxR_sign;
        boolean steepL, steepR;
        float txL, tyL, txR, tyR, zL, zR;
        Vector3f nL, nR;
        int tWidth = textureARGB.length;
        int tHeight = textureARGB[0].length;

        boolean longSideOnTheRight = (y3 - y1) * (x2 - x1) - (y2 - y1) * (x3 - x1) < 0;

        if (longSideOnTheRight) {
            dxL = x2 - x1;
            dxR = x3 - x1;

            xL = x2;
            yL = y2;

            xR = x3;
            yR = y3;

            txL = tx2;
            tyL = ty2;

            txR = tx3;
            tyR = ty3;

            zR = z3;
            zL = z2;

            nL = n2;
            nR = n3;
        } else {
            dxL = x3 - x1;
            dxR = x2 - x1;

            xL = x3;
            yL = y3;

            xR = x2;
            yR = y2;

            txL = tx3;
            tyL = ty3;

            txR = tx2;
            tyR = ty2;

            zL = z3;
            zR = z2;

            nL = n3;
            nR = n2;
        }


        dyL = yL - y1;
        dyR = yR - y1;
        dxL_sign = dxL == 0 ? 1 : dxL / Math.abs(dxL);
        dxR_sign = dxR == 0 ? 1 : dxR / Math.abs(dxR);

        dxL = Math.abs(dxL);
        dxR = Math.abs(dxR);


        steepL = dyL >= dxL;
        steepR = dyR >= dxR;

        int faultL_num = steepL ? dyL : dxL;
        int faultL_denom = faultL_num * 2;
        int faultR_num = steepR ? dyR : dxR;
        int faultR_denom = faultR_num * 2;

        int xcL = x1;
        int xcR = x1;

        int from, to;

        //ОТРИСОВКА ВЕРХНЕЙ ПОЛОВИНЫ
        for (int y0 = y1; y0 < y2; y0++) {

            if (steepL) {
                float k1 = 1f * (yL - y0) / dyL;
                float kL = 1f * (y0 - y1) / dyL;
                float tx = tx1 * k1 + txL * kL;
                float ty = ty1 * k1 + tyL * kL;
                float z = z1 * k1 + zL * kL;
                ColorIntARGB color = textureARGB[(int) (tx * tWidth)][(int) (ty * tHeight)];

                if (disableSmoothing) {
                    pixelWriter.setRGB(xcL, y0, z, borderColorTransfiguration.apply(fadeColorARGB(color, kNotSmoothed)));
                } else {
                    Vector3f normal = Vector3f.sum(Vector3f.mul(n1, -k1), Vector3f.mul(nL, -kL)).normalized();
                    float dp = normal.dotProduct(light);
                    if (dp < 0) dp = 0;
                    float k = ambient + dp * (1 - ambient);

                    pixelWriter.setRGB(xcL, y0, z, borderColorTransfiguration.apply(fadeColorARGB(color, k)));
                }

                from = xcL + 1;
                faultL_num += 2 * dxL;
                if (faultL_num >= faultL_denom) {
                    xcL += dxL_sign;
                    faultL_num -= faultL_denom;
                }
            } else {
                int xToMove = 1 + (faultL_denom - faultL_num) / (2 * dyL);
                xcL += xToMove * dxL_sign;
                faultL_num += 2 * dyL * xToMove - faultL_denom;
                drawLineWithInterpolation(pixelWriter, x1, z1, xL, zL, tx1, ty1, txL, tyL, xcL - dxL_sign, y0, xToMove, -dxL_sign, textureARGB, n1, nL,
                        light, ambient, borderColorTransfiguration, disableSmoothing, kNotSmoothed);
                from = Math.max(xcL, xcL - xToMove * dxL_sign + 1);
            }

            if (steepR) {
                float k1 = 1f * (yR - y0) / dyR;
                float kR = 1f * (y0 - y1) / dyR;

                float tx = tx1 * k1 + txR * kR;
                float ty = ty1 * k1 + tyR * kR;
                float z = z1 * k1 + zR * kR;

                ColorIntARGB color = textureARGB[(int) (tx * tWidth)][(int) (ty * tHeight)];

                if (disableSmoothing) {
                    pixelWriter.setRGB(xcR, y0, z, borderColorTransfiguration.apply(fadeColorARGB(color, kNotSmoothed)));
                } else {
                    Vector3f normal = Vector3f.sum(Vector3f.mul(n1, -k1), Vector3f.mul(nR, -kR)).normalized();
                    float dp = normal.dotProduct(light);
                    if (dp < 0) dp = 0;
                    float k = ambient + dp * (1 - ambient);

                    pixelWriter.setRGB(xcR, y0, z, borderColorTransfiguration.apply(fadeColorARGB(color, k)));
                }

                to = xcR - 1;
                faultR_num += 2 * dxR;
                if (faultR_num >= faultR_denom) {
                    xcR += dxR_sign;
                    faultR_num -= faultR_denom;
                }
            } else {
                int xToMove = 1 + (faultR_denom - faultR_num) / (2 * dyR);
                xcR += xToMove * dxR_sign;
                faultR_num += 2 * dyR * xToMove - faultR_denom;
                drawLineWithInterpolation(pixelWriter, x1, z1, xR, zR, tx1, ty1, txR, tyR, xcR - dxR_sign, y0, xToMove, -dxR_sign, textureARGB, n1, nR,
                        light, ambient, borderColorTransfiguration, disableSmoothing, kNotSmoothed);
                to = Math.min(xcR, xcR - xToMove * dxR_sign - 1);
            }

            drawLineWithInterpolation(A, B, C, D, S, pixelWriter, x1, x2, x3, y1, y2, y3, tx1, ty1, tx2, ty2, tx3, ty3, y0, from, to, textureARGB, n1, n2, n3, light, ambient, disableSmoothing, kNotSmoothed);
        }

        //ОБРАБОТКА ПОСЛЕДНЕЙ ИТЕРАЦИИ ВЕРХНЕЙ ПОЛОВИНЫ

        if (longSideOnTheRight) {
            if (!steepL)
                drawLineWithInterpolation(pixelWriter, x1, z1, xL, zL, tx1, ty1, txL, tyL, x2, y2, Math.abs(xcL - x2) + 1, -dxL_sign, textureARGB, n1, nL,
                        light, ambient, borderColorTransfiguration, disableSmoothing, kNotSmoothed);
            xcL = x2;
        } else {
            if (!steepR)
                drawLineWithInterpolation(pixelWriter, x1, z1, xR, zR, tx1, ty1, txR, tyR, x2, y2, Math.abs(xcR - x2) + 1, -dxR_sign, textureARGB, n1, nR,
                        light, ambient, borderColorTransfiguration, disableSmoothing, kNotSmoothed);
            xcR = x2;
        }

        //ПЕРЕНАСТРОЙКА ПАРАМЕТРОВ ДЛЯ ОТРИСОВКИ НИЖНЕЙ ПОЛОВИНЫ
        if (longSideOnTheRight) {
            dxL = x3 - xL;
            dxL_sign = dxL == 0 ? 1 : Math.abs(dxL) / dxL;
            dxL = Math.abs(dxL);
            dyL = y3 - yL;
            steepL = dyL >= dxL;
            faultL_num = steepL ? dyL : dxL;
            faultL_denom = faultL_num * 2;
            xR = x1;
            xL = x2;
            yR = y1;
            yL = y2;

            txR = tx1;
            tyR = ty1;
            txL = tx2;
            tyL = ty2;

            zL = z2;
            zR = z1;

            nL = n2;
            nR = n1;

        } else {
            dxR = x3 - xR;
            dxR_sign = dxR == 0 ? 1 : Math.abs(dxR) / dxR;
            dxR = Math.abs(dxR);
            dyR = y3 - yR;
            steepR = dyR >= dxR;
            faultR_num = steepR ? dyR : dxR;
            faultR_denom = faultR_num * 2;
            xR = x2;
            xL = x1;
            yR = y2;
            yL = y1;

            txR = tx2;
            tyR = ty2;
            txL = tx1;
            tyL = ty1;

            zL = z1;
            zR = z2;

            nL = n1;
            nR = n2;
        }

        //ОТРИСОВКА НИЖНЕЙ ПОЛОВИНЫ

        for (int y0 = y2; y0 < y3; y0++) {
            if (steepL) {
                float k3 = 1f * (y0 - yL) / dyL;
                float kL = 1f * (y3 - y0) / dyL;

                float tx = tx3 * k3 + txL * kL;
                float ty = ty3 * k3 + tyL * kL;
                float z = z3 * k3 + zL * kL;

                ColorIntARGB color = textureARGB[(int) (tx * tWidth)][(int) (ty * tHeight)];

                if (disableSmoothing) {
                    pixelWriter.setRGB(xcL, y0, z, borderColorTransfiguration.apply(fadeColorARGB(color, kNotSmoothed)));
                } else {
                    Vector3f normal = Vector3f.sum(Vector3f.mul(n3, -k3), Vector3f.mul(nL, -kL)).normalized();
                    float dp = normal.dotProduct(light);
                    if (dp < 0) dp = 0;
                    float k = ambient + dp * (1 - ambient);

                    pixelWriter.setRGB(xcL, y0, z, borderColorTransfiguration.apply(fadeColorARGB(color, k)));
                }

                from = xcL + 1;
                faultL_num += 2 * dxL;
                if (faultL_num >= faultL_denom) {
                    xcL += dxL_sign;
                    faultL_num -= faultL_denom;
                }

            } else {
                int xToMove = 1 + (faultL_denom - faultL_num) / (2 * dyL);
                xcL += xToMove * dxL_sign;
                faultL_num += 2 * dyL * xToMove - faultL_denom;
                drawLineWithInterpolation(pixelWriter, x3, z3, xL, zL, tx3, ty3, txL, tyL, xcL - dxL_sign, y0, xToMove, -dxL_sign, textureARGB, n3, nL,
                        light, ambient, borderColorTransfiguration, disableSmoothing, kNotSmoothed);
                from = Math.max(xcL, xcL - xToMove * dxL_sign + 1);
            }

            if (steepR) {
                float k3 = 1f * (y0 - yR) / dyR;
                float kR = 1f * (y3 - y0) / dyR;

                float tx = tx3 * k3 + txR * kR;
                float ty = ty3 * k3 + tyR * kR;
                float z = z3 * k3 + zR * kR;

                ColorIntARGB color = textureARGB[(int) (tx * tWidth)][(int) (ty * tHeight)];

                if (disableSmoothing) {
                    pixelWriter.setRGB(xcR, y0, z, borderColorTransfiguration.apply(fadeColorARGB(color, kNotSmoothed)));
                } else {
                    Vector3f normal = Vector3f.sum(Vector3f.mul(n3, -k3), Vector3f.mul(nR, -kR)).normalized();
                    float dp = normal.dotProduct(light);
                    if (dp < 0) dp = 0;
                    float k = ambient + dp * (1 - ambient);

                    pixelWriter.setRGB(xcR, y0, z, borderColorTransfiguration.apply(fadeColorARGB(color, k)));
                }

                to = xcR - 1;
                faultR_num += 2 * dxR;
                if (faultR_num >= faultR_denom) {
                    xcR += dxR_sign;
                    faultR_num -= faultR_denom;
                }
            } else {
                int xToMove = 1 + (faultR_denom - faultR_num) / (2 * dyR);
                xcR += xToMove * dxR_sign;
                faultR_num += 2 * dyR * xToMove - faultR_denom;
                drawLineWithInterpolation(pixelWriter, x3, z3, xR, zR, tx3, ty3, txR, tyR, xcR - dxR_sign, y0, xToMove, -dxR_sign, textureARGB, n3, nR,
                        light, ambient, borderColorTransfiguration, disableSmoothing, kNotSmoothed);
                to = Math.min(xcR, xcR - xToMove * dxR_sign - 1);
            }

            drawLineWithInterpolation(A, B, C, D, S, pixelWriter, x1, x2, x3, y1, y2, y3, tx1, ty1, tx2, ty2, tx3, ty3, y0, from, to, textureARGB, n1, n2, n3, light, ambient, disableSmoothing, kNotSmoothed);
        }

        //ОБРАБОТКА ПОСЛЕДНЕЙ ИТЕРАЦИИ ПОСЛЕДНЕЙ ПОЛОВИНЫ

        if (!steepL)
            drawLineWithInterpolation(pixelWriter, x3, z3, xL, zL, tx3, ty3, txL, tyL, x3, y3, Math.abs(xcL - x3) + 1, -dxL_sign, textureARGB, n3, nL,
                    light, ambient, borderColorTransfiguration, disableSmoothing, kNotSmoothed);

        if (!steepR)
            drawLineWithInterpolation(pixelWriter, x3, z3, xR, zR, tx3, ty3, txR, tyR, x3, y3, Math.abs(xcR - x3) + 1, -dxR_sign, textureARGB, n3, nR,
                    light, ambient, borderColorTransfiguration, disableSmoothing, kNotSmoothed);

    }

    private static void drawLineWithInterpolation(ZBufferPixelWriter pixelWriter, int x1, float z1, int x2, float z2, float tx1,
                                                  float ty1, float tx2, float ty2, int xc, int yc, int l, int sign,
                                                  ColorIntARGB[][] texture, Vector3f n1, Vector3f n2, Vector3f light,
                                                  float ambient, Function<Integer, Integer> borderColorTransfiguration, boolean disableSmoothing, float kNotSmoothed) {
        int x0 = xc;
        int x01 = x2 - xc;
        int x02 = xc - x1;
        int dx = x2 - x1;

        int height = texture.length;
        int width = texture[0].length;

        for (int i = 0; i < l; i++) {
            float k1 = 1f * x01 / dx;
            float k2 = 1f * x02 / dx;
            float tx = tx1 * k1 + tx2 * k2;
            float ty = ty1 * x01 / dx + ty2 * x02 / dx;
            float z = z1 * x01 / dx + z2 * x02 / dx;
            ColorIntARGB color = texture[(int) (tx * width)][(int) (ty * height)];

            if (disableSmoothing) {
                pixelWriter.setRGB(x0, yc, z, borderColorTransfiguration.apply(fadeColorARGB(color, kNotSmoothed)));
            } else {
                Vector3f normal = Vector3f.sum(Vector3f.mul(n1, -k1), Vector3f.mul(n2, -k2)).normalized();
                float dp = normal.dotProduct(light);
                if (dp < 0) dp = 0;
                float k = ambient + dp * (1 - ambient);

                pixelWriter.setRGB(x0, yc, z, borderColorTransfiguration.apply(fadeColorARGB(color, k)));
            }

            x0 += sign;
            x01 -= sign;
            x02 += sign;
        }
    }

    private static void drawLineWithInterpolation(float A, float B, float C, float D, int s, ZBufferPixelWriter pixelWriter, int x1, int x2, int x3, int y1, int y2, int y3,
                                                  float tx1, float ty1, float tx2, float ty2, float tx3, float ty3,
                                                  int yc, int from, int to, ColorIntARGB[][] texture, Vector3f n1, Vector3f n2, Vector3f n3,
                                                  Vector3f light, float ambient, boolean disableSmoothing, float kNotSmoothed) {

        int s1 = x2 * y3 + from * y2 + yc * x3 - yc * x2 - y2 * x3 - from * y3;
        int s2 = from * y3 + x1 * yc + y1 * x3 - from * y1 - yc * x3 - x1 * y3;
        int s3 = x2 * yc + x1 * y2 + from * y1 - x2 * y1 - from * y2 - x1 * yc;

        int ds1 = y2 - y3;
        int ds2 = y3 - y1;
        int ds3 = y1 - y2;

        int height = texture.length;
        int width = texture[0].length;

        for (int x0 = from; x0 <= to; x0++) {
            float k1 = 1f * s1 / s;
            float k2 = 1f * s2 / s;
            float k3 = 1f * s3 / s;

            float tx = tx1 * k1 + tx2 * k2 + tx3 * k3;
            float ty = ty1 * k1 + ty2 * k2 + ty3 * k3;
            float z = -(A * x0 + B * yc + D) / C;

            ColorIntARGB color = texture[(int) (tx * width)][(int) (ty * height)];

            if (disableSmoothing) {
                pixelWriter.setRGB(x0, yc, z, fadeColorARGB(color, kNotSmoothed));
            } else {
                Vector3f normal = Vector3f.sum(Vector3f.mul(n1, -k1), Vector3f.mul(n2, -k2)).add(Vector3f.mul(n3, -k3)).normalized();
                float dp = normal.dotProduct(light);
                if (dp < 0) dp = 0;
                float k = ambient + dp * (1 - ambient);

                pixelWriter.setRGB(x0, yc, z, fadeColorARGB(color, k));
            }

            s1 += ds1;
            s2 += ds2;
            s3 += ds3;
        }
    }

    private static int fadeColorARGB(ColorIntARGB color, float k) {

        int blue = (int) (color.getBlue() * k);
        int green = (int) (color.getGreen() * k);
        int red = (int) (color.getRed() * k);
        return 255 << 24 | red << 16 | green << 8 | blue;
    }

    public static void fillPolygon(ZBufferPixelWriter pixelWriter, PolygonVertex v1, PolygonVertex v2, PolygonVertex v3, Vector3f normalToPolygon, Vector3f light,
                                   float ambient, ColorIntARGB[][] textureARGB, int meshColor,
                                   boolean disableSmoothing) {
        fillPolygon(pixelWriter, v1, v2, v3, normalToPolygon, textureARGB, light, ambient, c -> meshColor, disableSmoothing);
    }
}
