package org.firstinspires.ftc.teamcode.Subsystems.Telemetry;

public class BootCheck {
    private final String label;
    private final boolean ok;
    private final String detail;

    public BootCheck(String label, boolean ok, String detail) {
        this.label = label;
        this.ok = ok;
        this.detail = detail;
    }

    public String getLabel() {
        return label;
    }

    public boolean isOk() {
        return ok;
    }

    public String getDetail() {
        return detail;
    }
}

