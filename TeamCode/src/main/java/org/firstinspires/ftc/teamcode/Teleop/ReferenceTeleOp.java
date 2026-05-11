package org.firstinspires.ftc.teamcode.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.System;

@TeleOp(name = "Mark 3 Reference TeleOp", group = "Mark 3")
public class ReferenceTeleOp extends OpMode {
    private System robot;

    @Override
    public void init() {
        robot = new System(
                hardwareMap,
                gamepad1,
                gamepad2,
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );

        robot.beginBoot();
        robot.initHardware();
    }

    @Override
    public void init_loop() {
        if (robot != null) {
            robot.bootLoop();
        }
    }

    @Override
    public void start() {
        if (robot != null) {
            robot.finishBoot();
        }
    }

    @Override
    public void loop() {
        if (robot != null) {
            robot.teleOpLoop();
        }
    }

    @Override
    public void stop() {
        if (robot != null) {
            robot.stopAll();
        }
    }
}

