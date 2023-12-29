package ru.vsu.cs.baklanova.Affine.AffineBuilder;

import ru.vsu.cs.baklanova.Affine.AffineExceptions;
import ru.vsu.cs.baklanova.Affine.AffineMatrix;
import ru.vsu.cs.baklanova.Affine.AxisEnum;
import ru.vsu.cs.baklanova.Math.vector.Vector3f;

public class Rotate {
    public enum RotateWayEnum{
        XYZ("XYZ"),
        XZY("XZY"),
        YXZ("YXZ"),
        YZX("YZX"),
        ZXY("ZXY"),
        ZYX("ZYX");

        String way;
        RotateWayEnum(String way) {
            this.way = way;
        }

        public String getWay() {
            return way;
        }
    }
    AffineBuilder builder;
    public Rotate(AffineBuilder builder) {
        this.builder = builder;
    }

    public Rotate byAxisInRadians(AxisEnum axisEnum, double angle) throws Exception {
        builder.addRotate(AffineMatrix.rotateMatrix(axisEnum, angle));

        return this;
    }

    public Rotate byAxisInDegrees(AxisEnum axisEnum, double angle) throws Exception {
        return byAxisInRadians(axisEnum, Math.toRadians(angle));
    }

    private void rotate(AxisEnum[] enums, Vector3f angles) throws Exception {
        if (angles == null) {
            throw new AffineExceptions("Angles vector is null");
        }
        for (AxisEnum e : enums) {
            switch (e) {
                case X -> byX(angles.getX());
                case Y -> byY(angles.getY());
                case Z -> byZ(angles.getZ());
            }
        }
    }

    public Rotate byX(double angle) throws Exception {
        byAxisInDegrees(AxisEnum.X, angle);
        return this;
    }

    public Rotate byY(double angle) throws Exception {
        byAxisInDegrees(AxisEnum.Y, angle);
        return this;
    }

    public Rotate byZ(double angle) throws Exception {
        byAxisInDegrees(AxisEnum.Z, angle);
        return this;
    }

    public Rotate inOrder(RotateWayEnum way, Vector3f angle) throws Exception {
        switch (way) {
            case XYZ -> {
                return XYZ(angle);
            }
            case XZY -> {
                return XZY(angle);
            }
            case YXZ -> {
                return YXZ(angle);
            }
            case YZX -> {
                return YZX(angle);
            }
            case ZXY -> {
                return ZXY(angle);
            }
            case ZYX -> {
                return ZYX(angle);
            }
            default -> throw new AffineExceptions("Wrong way to rotate");
        }
    }
    public Rotate XYZ(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.X, AxisEnum.Y, AxisEnum.Z};
        rotate(enums, angle);
        return this;
    }

    public Rotate XZY(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.X, AxisEnum.Z, AxisEnum.Y};
        rotate(enums, angle);
        return this;
    }

    public Rotate YXZ(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.Y, AxisEnum.X, AxisEnum.Z};
        rotate(enums, angle);
        return this;
    }
    public Rotate YZX(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.Y, AxisEnum.Z, AxisEnum.X};
        rotate(enums, angle);
        return this;
    }

    public Rotate ZXY(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.Z, AxisEnum.X, AxisEnum.Y};
        rotate(enums, angle);
        return this;
    }

    public Rotate ZYX(Vector3f angle) throws Exception {
        AxisEnum[] enums = new AxisEnum[] {AxisEnum.Z, AxisEnum.Y, AxisEnum.X};
        rotate(enums, angle);
        return this;
    }

    public AffineBuilder close() {
        return builder;
    }

}
