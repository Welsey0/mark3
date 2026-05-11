package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.framework.opmode.BaseOpMode;
import org.firstinspires.ftc.teamcode.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

/**
 * Simple TeleOp demonstrating the WPILib-like framework and basic mecanum drive.
 *
 * - Initializes DriveHardware using motor names compatible with Mark 2
 * - Creates DriveSubsystem and a TeleopDriveCommand bound to gamepad1
 * - Registers the TeleopDriveCommand as the default command for the drivetrain
 * - Runs the CommandScheduler in loop()
 *
 * This OpMode is an example: keep it small and easy to read so you can adapt it.
 *
 * EDIT HERE: this file should stay small. Use it to compose the robot and telemetry.
 */
@TeleOp(name = "Mecanum TeleOp (Mark3)")
public class MecanumTeleOp extends BaseOpMode {

    private RobotHardware robotHardware;
    private RobotContainer robotContainer;

    @Override
    protected void onInit() {
        telemetry.addData("Status", "Initializing drive hardware...");
        telemetry.update();

        robotHardware = new RobotHardware();
        robotHardware.init(hardwareMap);

        robotContainer = new RobotContainer(robotHardware);
        robotContainer.configureTeleop(gamepad1);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    protected void onLoopMode() {
        telemetry.addData("Drive Mode", org.firstinspires.ftc.teamcode.robot.Constants.Drive.FIELD_CENTRIC_ENABLED ? "Field-centric" : "Robot-centric");
        telemetry.addData("Use IMU Heading", org.firstinspires.ftc.teamcode.robot.Constants.Drive.USE_IMU_FOR_HEADING);
        if (org.firstinspires.ftc.teamcode.robot.Constants.Drive.USE_IMU_FOR_HEADING && robotHardware != null) {
            telemetry.addData("Heading(rad)", robotHardware.drive.getHeadingRadians());
        }

        telemetry.addData("Status", "Running");
        telemetry.update();
    }
}

