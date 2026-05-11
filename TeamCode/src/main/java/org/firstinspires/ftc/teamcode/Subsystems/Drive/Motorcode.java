package org.firstinspires.ftc.teamcode.Subsystems.Drive;

public class Motorcode {
    public double frontLeft;
    public double frontRight;
    public double backLeft;
    public double backRight;

    public Motorcode() {
        this(0, 0, 0, 0);
    }

    public Motorcode(double fl, double fr, double bl, double br) {
        frontLeft = fl;
        frontRight = fr;
        backLeft = bl;
        backRight = br;
    }

    public static Motorcode zero() {
        return new Motorcode(0, 0, 0, 0);
    }

    public Motorcode scale(double s) {
        return new Motorcode(
                frontLeft * s,
                frontRight * s,
                backLeft * s,
                backRight * s
        );
    }

    public Motorcode add(Motorcode other) {
        return new Motorcode(
                frontLeft + other.frontLeft,
                frontRight + other.frontRight,
                backLeft + other.backLeft,
                backRight + other.backRight
        );
    }

    public Motorcode clamp(double min, double max) {
        return new Motorcode(
                clamp(frontLeft, min, max),
                clamp(frontRight, min, max),
                clamp(backLeft, min, max),
                clamp(backRight, min, max)
        );
    }

    public String toCodeString() {
        return String.format(java.util.Locale.US, "%.2f,%.2f,%.2f,%.2f",
                frontLeft, frontRight, backLeft, backRight);
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}