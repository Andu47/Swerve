package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.common.commandbase.command.subsystemcommands.IntakeExtendCommand;
import org.firstinspires.ftc.teamcode.common.hardware.Robot;

@Config
@TeleOp(name = "OpModeTest")
public class OpMode extends CommandOpMode {
    private Robot robot;

    private ElapsedTime timer;
    private double loopTime = 0;
    private boolean fA = false;
    private boolean fB = false;
    private boolean fX = false;
    private boolean fY = false;

    @Override
    public void initialize() {
        robot = new Robot(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        PhotonCore.CONTROL_HUB.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        PhotonCore.enable();


    }

    @Override
    public void run() {
        if (timer == null) {
            timer = new ElapsedTime();
        }

        // use fallimg edge dedteier
        boolean a = gamepad1.a;
        if (a && !fA) {
            schedule(new InstantCommand(() -> robot.lift.resetTimer())
            .alongWith(new InstantCommand(() -> robot.lift.setDVA(500, 1500, 7500))));
        }
        boolean fA = a;

        boolean b = gamepad1.b;
        if (b && !fB) {
            schedule(new InstantCommand(() -> robot.lift.setDVA(-500, -1500, -7500))
            .alongWith(new InstantCommand(() -> robot.lift.resetTimer())));
        }
        fB = b;

        // 600 1500 7500
        boolean x = gamepad1.x;
        if (x && !fX) {
            schedule(new InstantCommand(() -> robot.intake.resetTimer())
            .alongWith(new InstantCommand(() -> robot.intake.setDVA(200, 15, 75))));
        }
        boolean fX = x;

        boolean y = gamepad1.y;
        if (y && !fY) {
            schedule(new InstantCommand(() -> robot.intake.setDVA(-200, -15, -75))
            .alongWith(new InstantCommand(() -> robot.intake.resetTimer())));
        }
        fY = y;

        robot.intake.loop();
        robot.lift.loop();
        CommandScheduler.getInstance().run();

        telemetry.addData("curPos:", robot.lift.getPos());
        telemetry.addData("curPow:", robot.lift.power);
        telemetry.addData("curPos:", robot.intake.getPos());
        telemetry.addData("curPow:", robot.intake.power);

        double loop = System.currentTimeMillis();
        telemetry.addData("hz ", 1000 / (loop - loopTime));

        telemetry.update();

        loopTime = loop;
        PhotonCore.CONTROL_HUB.clearBulkCache();
        PhotonCore.EXPANSION_HUB.clearBulkCache();
    }
}
