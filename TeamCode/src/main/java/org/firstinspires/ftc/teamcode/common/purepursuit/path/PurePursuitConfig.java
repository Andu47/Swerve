package org.firstinspires.ftc.teamcode.common.purepursuit.path;

import com.acmerobotics.dashboard.config.Config;

@Config
public class PurePursuitConfig {
    public static double ALLOWED_TRANSLATIONAL_ERROR = 4;
    public static double ALLOWED_HEADING_ERROR = Math.toRadians(5);

    // 24, 26 for mecanum, -Math.PI / 2.5
    public static int pCoefficientX = 12;
    public static int pCoefficientY = 12;
    public static double pCoefficientH = -Math.PI / 1.2;
}
