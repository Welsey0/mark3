package org.firstinspires.ftc.teamcode.hardware.sensors;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.framework.Hardware;
import org.firstinspires.ftc.teamcode.robot.Constants;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

public class IMUHardware implements Hardware {
    private IMU imu;

    @Override
    public void init(HardwareMap hardwareMap) {
        try {
            imu = hardwareMap.get(IMU.class, Constants.Sensors.IMU_NAME);
        } catch (RuntimeException ignored) {
            imu = null;
        }
    }
    /**
     * Return heading in radians if IMU is available; otherwise returns 0.
     */
    public double getHeading() {
        if (imu == null) return 0.0;
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }
}
