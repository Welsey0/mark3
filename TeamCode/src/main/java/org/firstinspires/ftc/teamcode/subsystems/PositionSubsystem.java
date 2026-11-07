package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.hardware.sensors.IMUHardware;
import org.firstinspires.ftc.teamcode.hardware.sensors.PinpointHardware;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;

public class PositionSubsystem implements Subsystem {
    private final IMUHardware imuHardware;
    private final PinpointHardware pinpointHardware;
    public PositionSubsystem(IMUHardware imuHardware, PinpointHardware pinpointHardware) {
        if (imuHardware == null && pinpointHardware == null) {
            throw new RobotInitializationException(
                    "PositionSubsystem", "at least one position hardware wrapper is required");
        }
        this.imuHardware = imuHardware;
        this.pinpointHardware = pinpointHardware;
    }
    public double getHeading() {
        IMU imu = imuHardware == null ? null : imuHardware.imu;
        double iHeading = imuHardware == null ? 0.0 : imuHardware.getHeading();
        GoBildaPinpointDriver pinpoint =
                pinpointHardware == null ? null : pinpointHardware.pinpoint;
        double pHeading = pinpointHardware == null ? 0.0 : pinpointHardware.getHeading();
        if (imu != null && pinpoint != null) {
            return iHeading;
        }
        return imu != null ? iHeading : pHeading;
    }

    public double getX() {
        Pose2D pose = pinpointHardware == null ? null : pinpointHardware.getPosition();
        return pose == null ? 0.0 : pose.getX(org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM);
    }

    public double getY() {
        Pose2D pose = pinpointHardware == null ? null : pinpointHardware.getPosition();
        return pose == null ? 0.0 : pose.getY(org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM);
    }
}