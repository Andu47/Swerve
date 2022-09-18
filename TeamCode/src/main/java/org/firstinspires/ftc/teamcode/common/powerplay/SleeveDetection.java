package org.firstinspires.ftc.teamcode.common.powerplay;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class SleeveDetection extends OpenCvPipeline {
    /*
    YELLOW  = Parking Left
    CYAN    = Parking Middle
    MAGENTA = Parking Right
     */

    public enum SleeveRotation {
        YELLOW,
        CYAN,
        MAGENTA
    }

    public static Point SLEEVE_TOPLEFT_ANCHOR_POINT = new Point(145, 168);

    public static int REGION_WIDTH = 30;
    public static int REGION_HEIGHT = 50;

    public static final int COLOR_MAX = 49;
    public static final int COLOR_MIN = 205;

    private double yelPercent, cyaPercent, magPercent;
    
    private static final Scalar
            lower_yellow_bounds  = new Scalar(COLOR_MIN, COLOR_MIN, 0, 255),
            upper_yellow_bounds  = new Scalar(255, 255, COLOR_MAX, 255),
            lower_cyan_bounds    = new Scalar(0, COLOR_MIN, COLOR_MIN, 255),
            upper_cyan_bounds    = new Scalar(COLOR_MAX, 255, 255, 255),
            lower_magenta_bounds = new Scalar(COLOR_MIN, 0, COLOR_MIN, 255),
            upper_magenta_bounds = new Scalar(255, COLOR_MAX, 255, 255);

    private final Scalar
            RED   = new Scalar(255, 0, 0),
            GREEN = new Scalar(0, 255, 0),
            BLUE  = new Scalar(0, 0, 255),
            WHITE = new Scalar(255, 255, 255);

    private Mat yelMat = new Mat(), cyaMat = new Mat(), magMat = new Mat();

    Point sleeve_pointA = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y);

    Point sleeve_pointB = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    private volatile SleeveRotation rotation = SleeveRotation.YELLOW;

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.blur(input, input, new Size(5, 5));

        Core.inRange(input, lower_yellow_bounds, upper_yellow_bounds, yelMat);
        Core.inRange(input, lower_cyan_bounds, upper_cyan_bounds, cyaMat);
        Core.inRange(input, lower_magenta_bounds, upper_magenta_bounds, magMat);

        yelPercent = Core.countNonZero(yelMat);
        cyaPercent = Core.countNonZero(cyaMat);
        magPercent = Core.countNonZero(magMat);

        Imgproc.rectangle(
                yelMat,
                sleeve_pointA,
                sleeve_pointB,
                GREEN,
                2
        );
        return yelMat;
    }
}
