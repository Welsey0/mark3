package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;

// mega magic number repository
public class BotConfig {
    // ===== HARDWARE =====
    // no @Config because those wouldn't update if you changed them from dashboard anyway
    public static class Hardware {
        public static String[] driveMotorNames = {
                "frontLeft",
                "frontRight",
                "backLeft",
                "backRight"
        };

        public static DcMotor.Direction[] driveMotorDirections = {
                DcMotor.Direction.FORWARD,
                DcMotor.Direction.REVERSE,
                DcMotor.Direction.FORWARD,
                DcMotor.Direction.REVERSE
        };
    }
    // ===== DRIVE =====
    @Config
    public static class Drive {
        public static boolean fieldCentricEnabled = true;
        public static double defaultSpeed = 1.0;
        public static double slowSpeed = 0.4;
    }

    // ===== TELEMETRY =====
    @Config
    public static class Telemetry {
        public static boolean bootAnimationEnabled = true;
        public static boolean showHealthPage = true;
        public static boolean showDrivePage = true;
        public static boolean showBindingsPage = true;
        public static int defaultSummaryPage = 0;
    }
}