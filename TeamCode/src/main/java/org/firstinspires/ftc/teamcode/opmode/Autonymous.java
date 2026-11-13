package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.framework.input.GamepadInput;
import org.firstinspires.ftc.teamcode.robot.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotContext;
import org.firstinspires.ftc.teamcode.subsystems.AutoDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.AutoDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PositionSubsystem;
import org.firstinspires.ftc.teamcode.supersystems.AutonomousSupersystem;

/**
 * Demonstration OpMode implementing basic mecanum drive.
 */

@TeleOp(name = "laith boutta lock in")
public class Autonymous extends OpMode {
    private RobotContext robot;
    public enum AutoState {
        gotopollinone,
        collectpollinone,
        gotonearsideshoot,
        shootnearside,
        gotopollintwo,
        collectpollintwo,
        gotofarsideshoot,
        shootfarside
        //yes i know this is cooked and it is temporary
    }

    private AutoState State = AutoState.gotopollinone;

    @Override
    public void init() {
        robot = RobotContext.builder(hardwareMap, telemetry)
                .withDrive()
                .withPosition(true, false)
                .withShooter()
                .build();
        robot.init();
    }

    @Override
    public void loop() {

        if (robot == null) {return;}

        switch (State) {
            case gotopollinone:
                robot.autonomous().followPath(new AutoDriveSubsystem.Path(new AutoDriveSubsystem.Point(0,0), new AutoDriveSubsystem.Point(0,0), new AutoDriveSubsystem.Point(0,0), new AutoDriveSubsystem.Point(0,1000)), new AutoDriveSubsystem.MotionProfile(100,100), 4.71238898038);
                break;
        }
        robot.periodic();
    }

    @Override
    public void stop() {
        if (robot != null) {
            robot.stop();
        }
    }

    private static double applyDeadband(double value) {
        double deadband = Constants.Drive.JOYSTICK_DEADZONE;
        if (Math.abs(value) <= deadband) return 0.0;
        return value > 0
                ? (value - deadband) / (1.0 - deadband)
                : (value + deadband) / (1.0 - deadband);
    }
}