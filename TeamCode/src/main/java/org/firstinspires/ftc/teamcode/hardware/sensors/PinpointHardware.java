package org.firstinspires.ftc.teamcode.hardware.sensors;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.framework.Hardware;
import org.firstinspires.ftc.teamcode.robot.Constants;

public class PinpointHardware implements Hardware {
    public GoBildaPinpointDriver pinpoint;

    public void init(HardwareMap hardwareMap) {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, Constants.Sensors.PINPOINT_NAME);
        pinpoint.setOffsets(Constants.Sensors.PINPOINT_X_OFFSET, Constants.Sensors.PINPOINT_Y_OFFSET, DistanceUnit.MM);
        pinpoint.setEncoderResolution(Constants.Sensors.PINPOINT_ENCODER_RESOLUTION);
        pinpoint.setEncoderDirections(Constants.Sensors.PINPOINT_X_ENCODER_DIRECTION, Constants.Sensors.PINPOINT_Y_ENCODER_DIRECTION);
    }

    public Pose2D getPosition() {
        return pinpoint.getPosition();
    }

    public double getHeading() {
        double heading;
        Pose2D pos = getPosition();
        if (pos != null) {
            pos = getPosition();
            heading = pos.getHeading(AngleUnit.RADIANS);
            return heading;
        }
        return 0;
    }
}
