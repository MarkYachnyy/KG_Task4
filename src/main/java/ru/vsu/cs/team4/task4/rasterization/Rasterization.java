package ru.vsu.cs.team4.task4.rasterization;

import ru.vsu.cs.team4.task4.math.vector.Vector3f;

public class Rasterization {

    public static void fillPolygon(ZBufferPixelWriter pixelWriter, int x1, int y1, float z1, int x2, int y2, float z2, int x3, int y3, float z3, float tx1, float ty1, float tx2, float ty2, float tx3, float ty3,
                                   Vector3f n1, Vector3f n2, Vector3f n3, Vector3f light, float ambient, int[][] textureARGB) {

        if (y1 > y2) {
            int t = y1;
            y1 = y2;
            y2 = t;

            t = x1;
            x1 = x2;
            x2 = t;

            float tf = tx1;
            ty1 = ty2;
            ty2 = tf;

            tf = tx1;
            tx1 = tx2;
            tx2 = tf;

            float tz = z1;
            z1 = z2;
            z2 = tz;

            Vector3f tv = n1;
            n1 = n2;
            n2 = tv;
        }

        if (y1 > y3) {
            int t = y1;
            y1 = y3;
            y3 = t;

            t = x1;
            x1 = x3;
            x3 = t;

            float tf = ty1;
            ty1 = ty3;
            ty3 = tf;

            tf = tx1;
            tx1 = tx3;
            tx3 = tf;

            float tz = z1;
            z1 = z3;
            z3 = tz;

            Vector3f tv = n1;
            n1 = n3;
            n3 = tv;
        }

        if (y2 > y3) {
            int t = y2;
            y2 = y3;
            y3 = t;

            t = x2;
            x2 = x3;
            x3 = t;

            float tf = ty2;
            ty2 = ty3;
            ty3 = tf;

            tf = tx2;
            tx2 = tx3;
            tx3 = tf;

            float tz = z2;
            z2 = z3;
            z3 = tz;

            Vector3f tv = n2;
            n2 = n3;
            n3 = tv;
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
                float tx = tx1 * (yL - y0) / dyL + txL * (y0 - y1) / dyL;
                float ty = ty1 * (yL - y0) / dyL + tyL * (y0 - y1) / dyL;
                float z = z1 * (yL - y0) / dyL + zL * (y0 - y1) / dyL;
                pixelWriter.setRGB(xcL, y0, z, textureARGB[(int) (tx * tWidth)][(int) (ty * tHeight)]);

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
                drawLineWithInterpolation(pixelWriter, x1, z1, xL, zL, tx1, ty1, txL, tyL, xcL - dxL_sign, y0, xToMove, -dxL_sign, textureARGB, n1, nL, light, ambient);
                from = Math.max(xcL, xcL - xToMove * dxL_sign + 1);
            }

            if (steepR) {
                float tx = tx1 * (yR - y0) / dyR + txR * (y0 - y1) / dyR;
                float ty = ty1 * (yR - y0) / dyR + tyR * (y0 - y1) / dyR;
                float z = z1 * (yR - y0) / dyR + zR * (y0 - y1) / dyR;
                pixelWriter.setRGB(xcR, y0, z, textureARGB[(int) (tx * tWidth)][(int) (ty * tHeight)]);
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
                drawLineWithInterpolation(pixelWriter, x1, z1, xR, zR, tx1, ty1, txR, tyR, xcR - dxR_sign, y0, xToMove, -dxR_sign, textureARGB, n1, nR, light, ambient);
                to = Math.min(xcR, xcR - xToMove * dxR_sign - 1);
            }

            drawLineWithInterpolation(pixelWriter, x1, x2, x3, y1, y2, y3, z1, z2, z3, tx1, ty1, tx2, ty2, tx3, ty3, y0, from, to, textureARGB, n1, n2, n3, light, ambient);
        }

        //ОБРАБОТКА ПОСЛЕДНЕЙ ИТЕРАЦИИ ВЕРХНЕЙ ПОЛОВИНЫ

        if (longSideOnTheRight) {
            if (!steepL)
                drawLineWithInterpolation(pixelWriter, x1, z1, xL, zL, tx1, ty1, txL, tyL, x2, y2, Math.abs(xcL - x2) + 1, -dxL_sign, textureARGB, n1, nL, light, ambient);
            xcL = x2;
        } else {
            if (!steepR)
                drawLineWithInterpolation(pixelWriter, x1, z1, xR, zR, tx1, ty1, txR, tyR, x2, y2, Math.abs(xcR - x2) + 1, -dxR_sign, textureARGB, n1, nR, light, ambient);
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
                float tx = tx3 * (y0 - yL) / dyL + txL * (y3 - y0) / dyL;
                float ty = ty3 * (y0 - yL) / dyL + tyL * (y3 - y0) / dyL;
                float z = z3 * (y0 - yL) / dyL + zL * (y3 - y0) / dyL;
                pixelWriter.setRGB(xcL, y0, z, textureARGB[(int) (tx * tWidth)][(int) (ty * tHeight)]);

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
                drawLineWithInterpolation(pixelWriter, x3, z3, xL, zL, tx3, ty3, txL, tyL, xcL - dxL_sign, y0, xToMove, -dxL_sign, textureARGB, n3, nL, light, ambient);
                from = Math.max(xcL, xcL - xToMove * dxL_sign + 1);
            }

            if (steepR) {

                float tx = tx3 * (y0 - yR) / dyR + txR * (y3 - y0) / dyR;
                float ty = ty3 * (y0 - yR) / dyR + tyR * (y3 - y0) / dyR;
                float z = z3 * (y0 - yL) / dyL + zR * (y3 - y0) / dyL;
                pixelWriter.setRGB(xcR, y0, z, textureARGB[(int) (tx * tWidth)][(int) (ty * tHeight)]);

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
                drawLineWithInterpolation(pixelWriter, x3, z3, xR, zR, tx3, ty3, txR, tyR, xcR - dxR_sign, y0, xToMove, -dxR_sign, textureARGB, n3, nR, light, ambient);
                to = Math.min(xcR, xcR - xToMove * dxR_sign - 1);
            }

            drawLineWithInterpolation(pixelWriter, x1, x2, x3, y1, y2, y3, z1, z2, z3, tx1, ty1, tx2, ty2, tx3, ty3, y0, from, to, textureARGB, n1, n2, n3, light, ambient);
        }

        //ОБРАБОТКА ПОСЛЕДНЕЙ ИТЕРАЦИИ ПОСЛЕДНЕЙ ПОЛОВИНЫ

        if (!steepL)
            drawLineWithInterpolation(pixelWriter, x3, z3, xL, zL, tx3, ty3, txL, tyL, x3, y3, Math.abs(xcL - x3) + 1, -dxL_sign, textureARGB, n3, nL, light, ambient);

        if (!steepR)
            drawLineWithInterpolation(pixelWriter, x3, z3, xR, zR, tx3, ty3, txR, tyR, x3, y3, Math.abs(xcR - x3) + 1, -dxR_sign, textureARGB, n3, nR, light, ambient);

    }

    private static void drawLineWithInterpolation(ZBufferPixelWriter pixelWriter, int x1, float z1, int x2, float z2, float tx1, float ty1, float tx2, float ty2, int xc, int yc, int l, int sign, int[][] texture, Vector3f n1, Vector3f n2, Vector3f light, float ambient) {
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

            Vector3f normal = Vector3f.sum(Vector3f.mul(n1, -k1), Vector3f.mul(n2, -k2)).normalized();
            float dp = normal.dotProduct(light);
            if(dp < 0) dp = 0;
            int color = texture[(int) (tx * width)][(int) (ty * height)];
            float k = ambient + dp * (1 - ambient);

            pixelWriter.setRGB(x0, yc, z, fadeColorARGB(color, k));
            x0 += sign;
            x01 -= sign;
            x02 += sign;
        }
    }

    private static void drawLineWithInterpolation(ZBufferPixelWriter pixelWriter, int x1, int x2, int x3, int y1, int y2, int y3, float z1, float z2, float z3,
                                                  float tx1, float ty1, float tx2, float ty2, float tx3, float ty3,
                                                  int yc, int from, int to, int[][] texture, Vector3f n1, Vector3f n2, Vector3f n3, Vector3f light, float ambient) {

        int s = x2 * y3 + x3 * y1 + x1 * y2 - y1 * x2 - y2 * x3 - x1 * y3;
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
            float z = z1 * k1 + z2 * k2 + z3 * k3;

            Vector3f normal = Vector3f.sum(Vector3f.mul(n1, -k1), Vector3f.mul(n2, -k2)).add(Vector3f.mul(n3, -k3)).normalized();
            float dp = normal.dotProduct(light);
            if(dp < 0) dp = 0;
            int color = texture[(int) (tx * width)][(int) (ty * height)];
            float k = ambient + dp * (1 - ambient);

            pixelWriter.setRGB(x0, yc, z, fadeColorARGB(color, k));

            s1 += ds1;
            s2 += ds2;
            s3 += ds3;
        }
    }


    private static int fadeColorARGB(int color, float k) {
        var blue = (color) & 255;
        var green = (color >> 8) & 255;
        var red = (color >> 16) & 255;
        blue = (int) (blue * k);
        green = (int) (green * k);
        red = (int) (red * k);
        return 255 << 24 | red << 16 | green << 8 | blue;
    }
}
