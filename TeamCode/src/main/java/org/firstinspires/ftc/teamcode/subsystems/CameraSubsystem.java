package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.hardware.vision.CameraHardware;
import org.firstinspires.ftc.teamcode.vision.VisionDetection;

import java.util.List;

public final class CameraSubsystem implements Subsystem {
    private final CameraHardware hardware;

    public CameraSubsystem(CameraHardware hardware) {
        if (hardware == null) {
            throw new RobotInitializationException("CameraSubsystem", "CameraHardware is null");
        }
        this.hardware = hardware;
    }

    public List<VisionDetection> getBalls() {
        return hardware.getBallDetections();
    }

    public List<VisionDetection> getGoals() {
        return hardware.getGoalDetections();
    }

    public void useGoalPipelines() {
        hardware.useGoalPipelines();
    }

    public void useBallPipelines() {
        hardware.useBallPipelines();
    }

    @Override
    public void stop() {
        hardware.stop();
    }
}
