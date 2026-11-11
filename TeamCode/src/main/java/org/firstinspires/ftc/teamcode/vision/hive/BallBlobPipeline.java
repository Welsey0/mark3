package org.firstinspires.ftc.teamcode.vision.hive;

import java.util.ArrayList;
import java.util.List;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.teamcode.vision.DetectionProvider;
import org.firstinspires.ftc.teamcode.vision.VisionDetection;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public abstract class BallBlobPipeline implements VisionProcessor, DetectionProvider {
    public enum BallColor { YELLOW, RED, BLUE }

    public static final class BallBlob {
        public final BallColor color;
        public final Rect rect;
        public final double area;
        public final double cxNorm;
        public final double cyNorm;

        private BallBlob(BallColor color, Rect rect, double area, int width, int height) {
            this.color = color;
            this.rect = rect;
            this.area = area;
            this.cxNorm = (rect.x + rect.width / 2.0) / width;
            this.cyNorm = (rect.y + rect.height / 2.0) / height;
        }
    }

    private final Object lock = new Object();
    private List<BallBlob> detections = new ArrayList<>();
    private volatile boolean enabled = true;

    protected abstract double expectedArea(BallColor color);
    protected abstract void findBlobs(Mat input, List<BallBlob> output);

    public BallBlob bestOf(BallColor color) {
        BallBlob best = null;
        double bestCost = Double.MAX_VALUE;
        synchronized (lock) {
            for (BallBlob blob : detections) {
                if (blob.color != color) continue;
                double fill = blob.area / (blob.rect.width * (double) blob.rect.height);
                double cost = Math.abs(blob.area / expectedArea(color) - 1.0)
                        + 4.0 * (1.0 - fill);
                if (cost < bestCost) {
                    best = blob;
                    bestCost = cost;
                }
            }
        }
        return best;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public List<VisionDetection> getDetections() {
        List<VisionDetection> result = new ArrayList<>();
        synchronized (lock) {
            for (BallBlob blob : detections) {
                result.add(new VisionDetection(
                        "", VisionDetection.Kind.BALL, blob.color.name(),
                        1.0, 0.0, 0.0, blob.area,
                        blob.cxNorm, blob.cyNorm));
            }
        }
        return result;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
    }

    @Override
    public Mat processFrame(Mat input, long captureTimeNanos) {
        List<BallBlob> found = new ArrayList<>();
        if (enabled) {
            findBlobs(input, found);
        }
        synchronized (lock) {
            detections = found;
        }
        return input;
    }

    @Override
    public void onDrawFrame(
            android.graphics.Canvas canvas,
            int onscreenWidth,
            int onscreenHeight,
            float scaleBmpPxToCanvasPx,
            float scaleCanvasDensity,
            Object userContext) {
    }

    protected void blobsFromMask(
            Mat mask,
            BallColor color,
            int frameWidth,
            int frameHeight,
            double minArea,
            double maxArea,
            List<BallBlob> output) {
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(
                mask, contours, hierarchy,
                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        hierarchy.release();
        mask.release();
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            Rect rect = Imgproc.boundingRect(contour);
            contour.release();
            if (area < minArea || area > maxArea || rect.width <= 0 || rect.height <= 0) {
                continue;
            }
            double aspect = rect.width / (double) rect.height;
            double fill = area / (rect.width * (double) rect.height);
            if (aspect < 0.71 || aspect > 1.4 || fill < 0.5 || fill > 1.3) {
                continue;
            }
            output.add(new BallBlob(color, rect, area, frameWidth, frameHeight));
        }
    }
}
