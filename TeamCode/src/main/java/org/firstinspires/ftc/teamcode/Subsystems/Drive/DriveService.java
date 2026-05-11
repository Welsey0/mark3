package org.firstinspires.ftc.teamcode.Subsystems.Drive;
import org.firstinspires.ftc.teamcode.Subsystems.Hardware;

public class DriveService {
    private final Hardware hardware;
    private String lastCode = "";

    public DriveService(Hardware hardware) {
        this.hardware = hardware;
    }

    public void apply(Motorcode code) {
        if (code == null || hardware == null || !hardware.isDriveReady()) {
            return;
        }

        String encoded = code.toCodeString();
        if (encoded.equals(lastCode)) return;

        hardware.frontLeft.setPower(code.frontLeft);
        hardware.frontRight.setPower(code.frontRight);
        hardware.backLeft.setPower(code.backLeft);
        hardware.backRight.setPower(code.backRight);

        lastCode = encoded;
    }
}
