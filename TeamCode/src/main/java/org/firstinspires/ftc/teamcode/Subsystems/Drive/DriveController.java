package org.firstinspires.ftc.teamcode.Subsystems.Drive;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.BotConfig;

public class DriveController {
    public Motorcode driveFromJoy(Gamepad pad, double headingRadians, double speed) {
        double dy = -pad.left_stick_y;
        double dx = pad.left_stick_x;
        double rx = -pad.right_stick_x;

        double x = dx;
        double y = dy;

        if (BotConfig.Drive.fieldCentricEnabled) {
            // process rotation for field-centric drive
            double cosHeading = Math.cos(headingRadians);
            double sinHeading = Math.sin(headingRadians);

            x = dx * cosHeading + dy * sinHeading;
            y = dy * cosHeading - dx * sinHeading;
        }

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double scaledSpeed = Math.max(0.0, Math.min(1.0, speed));

        double fl = (y + x + rx) / denominator;
        double fr = (y - x - rx) / denominator;
        double bl = (y - x + rx) / denominator;
        double br = (y + x - rx) / denominator;

        return new Motorcode(fl, fr, bl, br).scale(scaledSpeed);
    }
}