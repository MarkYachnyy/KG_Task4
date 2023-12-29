package ru.vsu.cs.team4.task4.rasterization;

public class Rasterization {

    public static void fillTriangle(CustomPixelWriter pixelWriter, int x1, int y1, int x2, int y2, int x3, int y3, float tx1, float ty1, float tx2, float ty2, float tx3, float ty3, int[][] texture) {

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
        }

        int dxL, dyL, dxR, dyR, yL, yR, xL, xR;
        int dxL_sign, dxR_sign;
        boolean steepL, steepR;
        float txL, tyL, txR, tyR;
        int tWidth = texture.length;
        int tHeight = texture[0].length;

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
                pixelWriter.setRGB(xcL, y0, texture[(int) (tx * tWidth)][(int) (ty * tHeight)]);

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
                drawLineWithInterpolation(pixelWriter, x1, xL, tx1, ty1, txL, tyL, xcL - dxL_sign, y0, xToMove, -dxL_sign, texture);
                from = Math.max(xcL, xcL - xToMove * dxL_sign + 1);
            }

            if (steepR) {
                float tx = tx1 * (yR - y0) / dyR + txR * (y0 - y1) / dyR;
                float ty = ty1 * (yR - y0) / dyR + tyR * (y0 - y1) / dyR;
                pixelWriter.setRGB(xcR, y0, texture[(int) (tx * tWidth)][(int) (ty * tHeight)]);
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
                drawLineWithInterpolation(pixelWriter, x1, xR, tx1, ty1, txR, tyR, xcR - dxR_sign, y0, xToMove, -dxR_sign, texture);
                to = Math.min(xcR, xcR - xToMove * dxR_sign - 1);
            }

            drawLineWithInterpolation(pixelWriter, x1, x2, x3, y1, y2, y3, tx1, ty1, tx2, ty2, tx3, ty3, y0, from, to, texture);
        }

        //ОБРАБОТКА ПОСЛЕДНЕЙ ИТЕРАЦИИ ВЕРХНЕЙ ПОЛОВИНЫ

        if (longSideOnTheRight) {
            if (!steepL)
                drawLineWithInterpolation(pixelWriter, x1, xL, tx1, ty1, txL, tyL, x2, y2, Math.abs(xcL - x2) + 1, -dxL_sign, texture);
            xcL = x2;
        } else {
            if (!steepR)
                drawLineWithInterpolation(pixelWriter, x1, xR, tx1, ty1, txR, tyR, x2, y2, Math.abs(xcR - x2) + 1, -dxR_sign, texture);
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
        }

        //ОТРИСОВКА НИЖНЕЙ ПОЛОВИНЫ

        for (int y0 = y2; y0 < y3; y0++) {
            if (steepL) {
                float tx = tx3 * (y0 - yL) / dyL + txL * (y3 - y0) / dyL;
                float ty = ty3 * (y0 - yL) / dyL + tyL * (y3 - y0) / dyL;
                pixelWriter.setRGB(xcL, y0, texture[(int) (tx * tWidth)][(int) (ty * tHeight)]);

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
                drawLineWithInterpolation(pixelWriter, x3, xL, tx3, ty3, txL, tyL, xcL - dxL_sign, y0, xToMove, -dxL_sign, texture);
                from = Math.max(xcL, xcL - xToMove * dxL_sign + 1);
            }

            if (steepR) {

                float tx = tx3 * (y0 - yR) / dyR + txR * (y3 - y0) / dyR;
                float ty = ty3 * (y0 - yR) / dyR + tyR * (y3 - y0) / dyR;
                pixelWriter.setRGB(xcR, y0, texture[(int) (tx * tWidth)][(int) (ty * tHeight)]);

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
                drawLineWithInterpolation(pixelWriter, x3, xR, tx3, ty3, txR, tyR, xcR - dxR_sign, y0, xToMove, -dxR_sign, texture);
                to = Math.min(xcR, xcR - xToMove * dxR_sign - 1);
            }

            drawLineWithInterpolation(pixelWriter, x1, x2, x3, y1, y2, y3, tx1, ty1, tx2, ty2, tx3, ty3, y0, from, to, texture);
        }

        //ОБРАБОТКА ПОСЛЕДНЕЙ ИТЕРАЦИИ ПОСЛЕДНЕЙ ПОЛОВИНЫ

        if (!steepL)
            drawLineWithInterpolation(pixelWriter, x3, xL, tx3, ty3, txL, tyL, x3, y3, Math.abs(xcL - x3) + 1, -dxL_sign, texture);

        if (!steepR)
            drawLineWithInterpolation(pixelWriter, x3, xR, tx3, ty3, txR, tyR, x3, y3, Math.abs(xcR - x3) + 1, -dxR_sign, texture);

    }

    private static void drawLineWithInterpolation(CustomPixelWriter pixelWriter, int x1, int x2, float tx1, float ty1, float tx2, float ty2, int xc, int yc, int l, int sign, int[][] texture) {
        int x0 = xc;
        int x01 = x2 - xc;
        int x02 = xc - x1;
        int dx = x2 - x1;

        int height = texture.length;
        int width = texture[0].length;

        for (int i = 0; i < l; i++) {
            float tx = tx1 * x01 / dx + tx2 * x02 / dx;
            float ty = ty1 * x01 / dx + ty2 * x02 / dx;

            pixelWriter.setRGB(x0, yc, texture[(int) (tx * width)][(int) (ty * height)]);
            x0 += sign;
            x01 -= sign;
            x02 += sign;
        }
    }

    private static void drawLineWithInterpolation(CustomPixelWriter pixelWriter, int x1, int x2, int x3, int y1, int y2, int y3, float tx1, float ty1, float tx2, float ty2, float tx3, float ty3, int yc, int from, int to, int[][] texture) {

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
            float tx = tx1 * s1 / s + tx2 * s2 / s + tx3 * s3 / s;
            float ty = ty1 * s1 / s + ty2 * s2 / s + ty3 * s3 / s;


            pixelWriter.setRGB(x0, yc, texture[(int) (tx * width)][(int) (ty * height)]);

            s1 += ds1;
            s2 += ds2;
            s3 += ds3;
        }
    }
}
