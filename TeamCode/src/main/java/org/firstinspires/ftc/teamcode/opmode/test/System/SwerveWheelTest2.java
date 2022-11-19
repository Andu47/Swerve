package org.firstinspires.ftc.teamcode.opmode.test.System;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.teamcode.opmode.teleop.OpMode;

@TeleOp(name = "SwerveWheelTest2")
public class SwerveWheelTest2 extends OpMode {
    CRServo a, b, c, d;
    DcMotorEx mA, mB, mC, mD;

    @Override
    public void initialize() {
        a = hardwareMap.get(CRServo.class, "rightFrontServo");
        b = hardwareMap.get(CRServo.class, "rightRearServo");
        c = hardwareMap.get(CRServo.class, "leftFrontServo");
        d = hardwareMap.get(CRServo.class, "leftRearServo");
        ((CRServoImplEx) a).setPwmRange(new PwmControl.PwmRange(510, 2490, 5000));
        ((CRServoImplEx) b).setPwmRange(new PwmControl.PwmRange(510, 2490, 5000));
        ((CRServoImplEx) c).setPwmRange(new PwmControl.PwmRange(510, 2490, 5000));
        ((CRServoImplEx) d).setPwmRange(new PwmControl.PwmRange(510, 2490, 5000));


        mA = hardwareMap.get(DcMotorEx.class, "rightFrontMotor");
        mB = hardwareMap.get(DcMotorEx.class, "leftFrontMotor");
        mC = hardwareMap.get(DcMotorEx.class, "leftRearMotor");
        mD = hardwareMap.get(DcMotorEx.class, "rightRearMotor");
    }

    @Override
    public void run() {
        if (gamepad1.a) {
            a.setPower(1);
        } else {
            a.setPower(-1);
        }

        if (gamepad1.b) {
            b.setPower(1);
        } else {
            b.setPower(-1);
        }

        if (gamepad1.x) {
            c.setPower(1);
        } else {
            c.setPower(-1);
        }

        if (gamepad1.y) {
            d.setPower(1);
        } else {
            d.setPower(-1);
        }

        if (gamepad1.right_bumper) {
            mA.setPower(1.0);
            mB.setPower(1.0);
            mC.setPower(1.0);
            mD.setPower(1.0);
        }

        if (gamepad1.left_bumper) {
            mA.setPower(0);
            mB.setPower(0);
            mC.setPower(0);
            mD.setPower(0);
        }

        if (gamepad1.dpad_down) {
            mA.setPower(1.0);
        } else {
            mA.setPower(0);
        }

        if (gamepad1.dpad_left) {
            mB.setPower(1.0);
        } else {
            mB.setPower(0);
        }

        if (gamepad1.dpad_up) {
            mC.setPower(1.0);
        } else {
            mC.setPower(0);
        }

        if (gamepad1.dpad_right) {
            mD.setPower(1.0);
        } else {
            mD.setPower(0);
        }
    }

}
