package org.firstinspires.ftc.teamcode.Subsystems.Summaries;

public class NumericMetric {
    private final String label;
    private final double value;

    public NumericMetric(String label, double value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public double getValue() {
        return value;
    }
}

