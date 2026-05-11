package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.BotConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hardware {
    public HardwareMap hardwareMap;
    private final List<String> bindingProblems = new ArrayList<>();

    // Drive motors
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public boolean driveReady;

    public Hardware(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public void init() {
        bindingProblems.clear();

        // Bind drive motors from config
        frontLeft = bindMotor("frontLeft", BotConfig.Hardware.driveMotorNames[0]);
        frontRight = bindMotor("frontRight", BotConfig.Hardware.driveMotorNames[1]);
        backLeft = bindMotor("backLeft", BotConfig.Hardware.driveMotorNames[2]);
        backRight = bindMotor("backRight", BotConfig.Hardware.driveMotorNames[3]);

        // Apply directions
        configureDriveMotor(frontLeft, BotConfig.Hardware.driveMotorDirections[0]);
        configureDriveMotor(frontRight, BotConfig.Hardware.driveMotorDirections[1]);
        configureDriveMotor(backLeft, BotConfig.Hardware.driveMotorDirections[2]);
        configureDriveMotor(backRight, BotConfig.Hardware.driveMotorDirections[3]);

        // Optional: set zero power behavior
        driveReady = frontLeft != null && frontRight != null && backLeft != null && backRight != null;
    }

    private DcMotor bindMotor(String role, String name) {
        try {
            return hardwareMap.dcMotor.get(name);
        } catch (RuntimeException ex) {
            bindingProblems.add(role + " missing: " + name + " (" + ex.getMessage() + ")");
            return null;
        }
    }

    private void configureDriveMotor(DcMotor motor, DcMotor.Direction direction) {
        if (motor == null) {
            return;
        }

        motor.setDirection(direction);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public boolean isDriveReady() {
        return driveReady;
    }

    public List<String> getBindingProblems() {
        return Collections.unmodifiableList(bindingProblems);
    }

    public String getDriveHealthSummary() {
        if (bindingProblems.isEmpty()) {
            return "Drive hardware ready";
        }

        return String.join(" | ", bindingProblems);
    }
}