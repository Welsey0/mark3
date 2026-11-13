package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.hardware.sensors.IMUHardware;
import org.firstinspires.ftc.teamcode.hardware.sensors.TwoPodOdometryHardware;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;

public class PositionSubsystem implements Subsystem {
    private final IMUHardware imuHardware;
    private final TwoPodOdometryHardware odometryHardware;
    public PositionSubsystem(IMUHardware imuHardware, TwoPodOdometryHardware odometryHardware) {
        if (imuHardware == null && odometryHardware == null) {
            throw new RobotInitializationException(
                    "PositionSubsystem", "at least one position hardware wrapper is required");
        }
        this.imuHardware = imuHardware;
        this.odometryHardware = odometryHardware;
    }
    public double getHeading() {
        IMU imu = imuHardware == null ? null : imuHardware.imu;
        double iHeading = imuHardware == null ? 0.0 : imuHardware.getHeading();
        return imu != null ? iHeading : 0.0;
    }

    public double getX() {
        Pose2D pose = odometryHardware == null ? null : odometryHardware.getPosition(getHeading());
        return pose == null ? 0.0 : pose.getX(org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM);
    }

    public double getY() {
        Pose2D pose = odometryHardware == null ? null : odometryHardware.getPosition(getHeading());
        return pose == null ? 0.0 : pose.getY(org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit.MM);
    }

}