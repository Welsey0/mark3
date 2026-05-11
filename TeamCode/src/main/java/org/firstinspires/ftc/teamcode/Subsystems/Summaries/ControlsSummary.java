package org.firstinspires.ftc.teamcode.Subsystems.Summaries;

public final class ControlsSummary {
    private ControlsSummary() {
    }

    public static SummaryPage build() {
        SummaryPage page = new SummaryPage("Bindings");
        page.addLine("Touchpad click: open/close menu");
        page.addLine("Touchpad right edge: OK / Select");
        page.addLine("Touchpad left edge: Back / Close / No");
        page.addLine("Touchpad top edge: Navigate Up");
        page.addLine("Touchpad bottom edge: Navigate Down");
        page.addLine("Left bumper: slow drive");
        page.addLine("Gamepad 1 sticks: drive");
        return page;
    }
}

