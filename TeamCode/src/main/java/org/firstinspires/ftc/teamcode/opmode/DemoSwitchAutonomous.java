package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.AutonomousSubsystem;
import org.firstinspires.ftc.teamcode.robot.RobotContext;
import org.firstinspires.ftc.teamcode.vision.VisionDetection;

@Autonomous(name = "Demo Switch Autonomous (Mark3)")
public final class DemoSwitchAutonomous extends OpMode {
    private enum AutoState {
        DRIVE_TO_CENTER,
        SEEK_BALL,
        SCORE,
        PARK,
        FINISHED
    }

    private static final double AUTO_SECONDS = 30.0;
    private static final double PARK_SECONDS_REMAINING = 7.0;

    private RobotContext robot;
    private AutoState state = AutoState.DRIVE_TO_CENTER;
    private final ElapsedTime runtime = new ElapsedTime();
    private boolean stateStarted;

    @Override
    public void init() {
        robot = RobotContext.builder(hardwareMap, telemetry)
                .withDrive()
                .withPosition(true, false)
                .withAutonomous()
                .build();
        robot.init();
    }

    @Override
    public void start() {
        runtime.reset();
        enterState(AutoState.DRIVE_TO_CENTER);
    }

    @Override
    public void loop() {
        if (robot == null || state == AutoState.FINISHED) {
            return;
        }

        double remaining = AUTO_SECONDS - runtime.seconds();
        if (remaining <= 0.0) {
            enterState(AutoState.FINISHED);
            return;
        }

        switch (state) {
            case DRIVE_TO_CENTER:
                if (stateStarted) {
                    stateStarted = false;
                    robot.autonomous().followPath(
                            new AutonomousSubsystem.Path(
                                    new AutonomousSubsystem.Point(0, 0),
                                    new AutonomousSubsystem.Point(200, 0),
                                    new AutonomousSubsystem.Point(400, 0),
                                    new AutonomousSubsystem.Point(600, 0)),
                            new AutonomousSubsystem.MotionProfile(300, 500),
                            0.0);
                }
                if (robot.autonomous().isFinished()) {
                    enterState(AutoState.SEEK_BALL);
                }
                break;

            case SEEK_BALL:
                if (remaining < PARK_SECONDS_REMAINING) {
                    enterState(AutoState.PARK);
                    break;
                }
                if (robot.camera() != null && !robot.camera().getBalls().isEmpty()) {
                    enterState(AutoState.SCORE);
                }
                break;

            case SCORE:
                // Replace this condition with the shooter/turret sequence for the robot.
                enterState(AutoState.SEEK_BALL);
                break;

            case PARK:
                if (stateStarted) {
                    stateStarted = false;
                    robot.autonomous().followPath(
                            new AutonomousSubsystem.Path(
                                    new AutonomousSubsystem.Point(600, 0),
                                    new AutonomousSubsystem.Point(500, -100),
                                    new AutonomousSubsystem.Point(300, -100),
                                    new AutonomousSubsystem.Point(0, -100)),
                            new AutonomousSubsystem.MotionProfile(250, 450),
                            0.0);
                }
                if (robot.autonomous().isFinished()) {
                    enterState(AutoState.FINISHED);
                }
                break;

            default:
                break;
        }

        robot.periodic();
        telemetry.addData("auto state", state);
        telemetry.addData("time remaining", "%.1f", remaining);
        if (robot.camera() != null) {
            VisionDetection goal = robot.camera().getGoals().isEmpty()
                    ? null : robot.camera().getGoals().get(0);
            telemetry.addData("goal", goal == null ? "none" : goal.label());
        }
        telemetry.update();
    }

    @Override
    public void stop() {
        if (robot != null) {
            robot.stop();
        }
    }

    private void enterState(AutoState next) {
        state = next;
        stateStarted = true;
    }
}
