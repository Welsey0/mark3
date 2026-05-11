package org.firstinspires.ftc.teamcode.Subsystems.Summaries;

import org.firstinspires.ftc.teamcode.Subsystems.Hardware;

public final class HealthSummary {
    private HealthSummary() {
    }

    public static SummaryPage build(Hardware hardware) {
        SummaryPage page = new SummaryPage("Health");
        page.addLine(hardware.isDriveReady() ? "Drive hardware ready." : "Drive hardware has problems.");
        page.addLine(hardware.getDriveHealthSummary());

        page.addMetric("DriveReady", hardware.isDriveReady() ? 1 : 0);
        page.addMetric("BindingProblems", hardware.getBindingProblems().size());
        return page;
    }
}

