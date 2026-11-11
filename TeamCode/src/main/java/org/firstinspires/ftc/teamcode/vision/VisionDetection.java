package org.firstinspires.ftc.teamcode.vision;

public final class VisionDetection {
    public enum Kind { BALL, GOAL }

    private final String source;
    private final Kind kind;
    private final String label;
    private final double confidence;
    private final double tx;
    private final double ty;
    private final double area;
    private final double cxNorm;
    private final double cyNorm;

    public VisionDetection(
            String source,
            Kind kind,
            String label,
            double confidence,
            double tx,
            double ty,
            double area,
            double cxNorm,
            double cyNorm) {
        this.source = source;
        this.kind = kind;
        this.label = label;
        this.confidence = confidence;
        this.tx = tx;
        this.ty = ty;
        this.area = area;
        this.cxNorm = cxNorm;
        this.cyNorm = cyNorm;
    }

    public String source() { return source; }
    public Kind kind() { return kind; }
    public String label() { return label; }
    public double confidence() { return confidence; }
    public double tx() { return tx; }
    public double ty() { return ty; }
    public double area() { return area; }
    public double cxNorm() { return cxNorm; }
    public double cyNorm() { return cyNorm; }
}
