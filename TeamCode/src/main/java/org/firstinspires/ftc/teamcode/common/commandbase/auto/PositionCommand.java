package org.firstinspires.ftc.teamcode.common.commandbase.auto;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.drive.drive.Drivetrain;
import org.firstinspires.ftc.teamcode.common.drive.geometry.Pose;
import org.firstinspires.ftc.teamcode.common.drive.localizer.Localizer;
import org.firstinspires.ftc.teamcode.common.hardware.Globals;

@Config
public class PositionCommand extends CommandBase {
    public static double ALLOWED_TRANSLATIONAL_ERROR = 0.25;
    public static double ALLOWED_HEADING_ERROR = Math.toRadians(1);

    public static double xP = 0.02;
    public static double xD = 0;
    public static double xF = 0;

    public static double yP = 0.02; // 0.02
    public static double yD = 0;
    public static double yF = 0;

    public static double hP = -0.025; // -0.25
    public static double hD = 0;
    public static double hF = 0;


    public static PIDFController xController = new PIDFController(xP, 0.0, xD, xF);
    public static PIDFController yController = new PIDFController(yP, 0.0, yD, yF);
    public static PIDFController hController = new PIDFController(hP, 0.0, hD, hF);
    public static double max_power = 1;

    Drivetrain drivetrain;
    Localizer localizer;
    Pose targetPose;
    ElapsedTime deadTimer;

    private final double ms;
    private final double delay;
    private ElapsedTime delayTimer;

    private final double v;

    public PositionCommand(Drivetrain drivetrain, Localizer localizer, Pose targetPose, double delay, double dead, double voltage) {
        this.drivetrain = drivetrain;
        this.localizer = localizer;
        this.targetPose = targetPose;
        this.ms = dead;
        this.delay = delay;
        this.v = voltage;
    }

    @Override
    public void execute() {
        if (deadTimer == null) {
            deadTimer = new ElapsedTime();
        }

        Pose yummypose = targetPose.subtract(localizer.getPos());
        System.out.println(yummypose);
        Pose powers = goToPosition(localizer.getPos(), targetPose);
        drivetrain.set(powers);
        Globals.yummypose = powers;
    }

    @Override
    public boolean isFinished() {
        Pose error = targetPose.subtract(localizer.getPos());
        Globals.error = error;
        Globals.targetPose = targetPose;
//        error.divide(new Pose(1, -1, 1));

        boolean reached = ((Math.hypot(error.x, error.y) < ALLOWED_TRANSLATIONAL_ERROR) && (Math.abs(error.heading) < ALLOWED_HEADING_ERROR));
        Globals.reached = reached;

        if (reached && delayTimer == null) {
            delayTimer = new ElapsedTime();
        }
        if (!reached && delayTimer != null) {
            delayTimer.reset();
        }

        boolean delayed = delayTimer != null && delayTimer.milliseconds() > delay;
        return (deadTimer.milliseconds() > ms) || delayed;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.set(new Pose());
    }

    private static Pose relDistanceToTarget(Pose robot, Pose target) {
        return target.subtract(robot);
    }

    public Pose goToPosition(Pose robotPose, Pose targetPose) {
        Pose deltaPose = relDistanceToTarget(robotPose, targetPose);
        Pose powers = new Pose(
                xController.calculate(0, deltaPose.x),
                yController.calculate(0, deltaPose.y),
                hController.calculate(0, deltaPose.heading)
        );
        double x_rotated = powers.x * Math.cos(robotPose.heading) - powers.y * Math.sin(robotPose.heading);
        double y_rotated = powers.x * Math.sin(robotPose.heading) + powers.y * Math.cos(robotPose.heading);
        double x_power = -x_rotated < -max_power ? -max_power :
                Math.min(-x_rotated, max_power);
        double y_power = -y_rotated < -max_power ? -max_power :
                Math.min(-y_rotated, max_power);
        double heading_power = powers.heading;

        heading_power = Math.max(Math.min(1, heading_power), -1);

        return new Pose(-y_power/v * 12.5, x_power/v * 12.5, heading_power/v * 12.5);
    }
}
