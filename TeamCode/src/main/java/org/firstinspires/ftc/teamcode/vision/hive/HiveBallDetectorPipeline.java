package org.firstinspires.ftc.teamcode.vision.hive;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public final class HiveBallDetectorPipeline extends BallBlobPipeline {
    private static final Scalar YELLOW_LO = new Scalar(9, 90, 45);
    private static final Scalar YELLOW_HI = new Scalar(33, 255, 238);
    private static final Scalar RED_LO_A = new Scalar(0, 81, 45);
    private static final Scalar RED_HI_A = new Scalar(15, 255, 232);
    private static final Scalar RED_LO_B = new Scalar(170, 81, 45);
    private static final Scalar RED_HI_B = new Scalar(180, 255, 232);
    private static final Scalar BLUE_LO = new Scalar(102, 80, 45);
    private static final Scalar BLUE_HI = new Scalar(128, 255, 247);

    @Override
    protected double expectedArea(BallColor color) {
        return color == BallColor.YELLOW ? 3500.0 : 4200.0;
    }

    @Override
    protected void findBlobs(Mat input, List<BallBlob> output) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);
        threshold(hsv, BallColor.YELLOW, YELLOW_LO, YELLOW_HI, null, null, output);
        threshold(hsv, BallColor.RED, RED_LO_A, RED_HI_A, RED_LO_B, RED_HI_B, output);
        threshold(hsv, BallColor.BLUE, BLUE_LO, BLUE_HI, null, null, output);
        hsv.release();
    }

    private void threshold(
            Mat hsv,
            BallColor color,
            Scalar lowA,
            Scalar highA,
            Scalar lowB,
            Scalar highB,
            List<BallBlob> output) {
        Mat mask = new Mat();
        Core.inRange(hsv, lowA, highA, mask);
        if (lowB != null) {
            Mat second = new Mat();
            Core.inRange(hsv, lowB, highB, second);
            Core.bitwise_or(mask, second, mask);
            second.release();
        }
        blobsFromMask(mask, color, hsv.cols(), hsv.rows(), 900.0, 12000.0, output);
    }
}
