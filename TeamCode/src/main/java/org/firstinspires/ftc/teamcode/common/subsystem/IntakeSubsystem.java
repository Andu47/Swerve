package org.firstinspires.ftc.teamcode.common.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.outoftheboxrobotics.photoncore.PhotonLynxModule;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.purepursuit.geometry.profiling.MotionProfile;
import org.firstinspires.ftc.teamcode.common.purepursuit.geometry.profiling.TrapezoidalMotionProfile;

import javax.xml.transform.TransformerException;

@Config
public class IntakeSubsystem extends SubsystemBase {
    private final MotorEx extension;
    private final Servo barLeft, barRight;
    private final Servo claw, turret;

    private MotionProfile profile;
    private final ElapsedTime timer;
    private final ElapsedTime voltageTimer;
    private final PIDController controller;
    private final VoltageSensor voltageSensor;

    private double voltage;

    private double P = 0.01;
    private double I = 0.0;
    private double D = 0.0;

    private double distance = 0.0;
    private double maxV = 16;
    private double maxA = 8;

    public static int intake_out_pos = 100;
    public static int intake_in_pos = 0;

    public static double claw_open_pos = 10;
    public static double claw_closed_pos = 5;

    public static double fourbar_extended = 0.4;
    public static double fourbar_retracted = 0.1;

    public static final double FOURBAR_LENGTH = 9.842;

    public double power = 0.0;

    // thanks aabhas <3
    public IntakeSubsystem(HardwareMap hardwareMap) {
        this.extension = new MotorEx(hardwareMap, "extension");
        extension.resetEncoder();
        this.barLeft = hardwareMap.get(Servo.class, "fourbarLeft");
        this.barRight = hardwareMap.get(Servo.class, "fourbarRight");
        this.claw = hardwareMap.get(Servo.class, "claw");
        this.turret = hardwareMap.get(Servo.class, "turret");

        this.profile = new TrapezoidalMotionProfile(maxV, maxA, distance);
        this.timer = new ElapsedTime();
        timer.reset();
        this.voltageTimer = new ElapsedTime();
        timer.reset();
        this.controller = new PIDController(P, I, D);
        this.voltageSensor = hardwareMap.voltageSensor.iterator().next();
        this.voltage = voltageSensor.getVoltage();
    }

    public void loop() {
        if (voltageTimer.seconds() > 5) {
            voltage = voltageSensor.getVoltage();
            voltageTimer.reset();
        }

        profile = new TrapezoidalMotionProfile(maxV, maxA, distance);
        controller.setPID(P, I, D);
        double target = profile.update(timer.time())[0];
        power = controller.calculate(extension.encoder.getPosition(), target) / voltage * 12;
        extension.set(power);

        //AnalogInput sensor = new AnalogInput()
                //
                // AnalogInput claw = hardwareMap.get(AnalogInput.class, "clawName");
                //
                // mult by 360/33.33

    }

    public void setMotionProfile(MotionProfile profile) {
        this.profile = profile;
    }

    public void setPos(int pos) {
        this.distance = pos;
        resetTimer();
    }

    public void setFourbar(double pos) {
        barLeft.setPosition(pos);
        barRight.setPosition(1 - pos);
    }

    public void setClaw(double position) {
        claw.setPosition(position);
    }

    public int getPos() {
        return extension.encoder.getPosition();
    }

    public void extensionOut() {
        extension.setTargetPosition(intake_out_pos);
    }

    public void extensionIn() {
        extension.setTargetPosition(intake_in_pos);
    }

    public void closeClaw() {
        claw.setPosition(claw_closed_pos);
    }

    public void openClaw() {
        claw.setPosition(claw_open_pos);
    }

    public void extendForebar() {
        barLeft.setPosition(fourbar_extended);
        barRight.setPosition(fourbar_extended);
    }

    public void closeForebar() {
        barLeft.setPosition(fourbar_retracted);
        barRight.setPosition(fourbar_retracted);
    }

    public void resetTimer() {
        timer.reset();
    }

    public void setPID(double P, double I, double D) {
        controller.setPID(P, I, D);
    }

    public void setDVA(double d, double v, double a) {
        this.distance = d;
        this.maxV = v;
        this.maxA = a;
    }
}
