package org.firstinspires.ftc.teamcode.Subsystems.Summaries;

import org.firstinspires.ftc.teamcode.BotConfig;

public final class SystemSummary {
    private SystemSummary() {
    }

    public static RobotSummary build(org.firstinspires.ftc.teamcode.System robot) {
        RobotSummary summary = new RobotSummary();

        if (BotConfig.Telemetry.showHealthPage) {
            summary.addPage(HealthSummary.build(robot.hardware));
        }

        if (BotConfig.Telemetry.showDrivePage) {
            summary.addPage(DriveSummary.build(robot.getHeading(), robot.getLastDriveSpeed(), robot.getLastDriveCommand()));
        }

        if (BotConfig.Telemetry.showBindingsPage) {
            summary.addPage(ControlsSummary.build());
        }

        return summary;
    }
}

