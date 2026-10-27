package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.teamcode.framework.Subsystem;
import org.firstinspires.ftc.teamcode.hardware.sensors.IMUHardware;
import org.firstinspires.ftc.teamcode.hardware.sensors.PinpointHardware;

public class PositionSubsystem implements Subsystem {
    private final IMUHardware imuHardware;
    private final PinpointHardware pinpointHardware;
    public PositionSubsystem(IMUHardware imuHardware, PinpointHardware pinpointHardware) {
        this.imuHardware = imuHardware;
        this.pinpointHardware = pinpointHardware;
    }
    public double getHeading() {
        // grab heading from either pinpoint, imu, or both depending on availability
        IMU imu  = imuHardware.imu;
        double iHeading = imuHardware.getHeading();
        if (imu != null) {boolean iHeadingAvailable = true;} else {boolean iHeadingAvailable = false;}
        GoBildaPinpointDriver pinpoint = pinpointHardware.pinpoint;
        double pHeading = pinpointHardware.getHeading();
        if (pinpoint != null) {boolean pHeadingAvailable = true;} else {boolean pHeadingAvailable = false;}
        // big if
        double heading = 0.0;
        if (imu != null && pinpoint != null) {
            //both available; average
            heading = iHeading + pHeading / 2;
        } else if (imu != null) {
            heading = iHeading;
        } else if (pinpoint != null) {
            heading = pHeading;
        }
        return heading;
    }
}