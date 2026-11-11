package org.firstinspires.ftc.teamcode.hardware.vision;

import android.util.Size;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import org.firstinspires.ftc.teamcode.framework.Hardware;
import org.firstinspires.ftc.teamcode.framework.RobotInitializationException;
import org.firstinspires.ftc.teamcode.vision.DetectionProvider;
import org.firstinspires.ftc.teamcode.vision.VisionDetection;
import org.firstinspires.ftc.teamcode.vision.hive.BallBlobPipeline;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CameraHardware implements Hardware {
    public static final class WebcamConfig {
        public final String name;
        public final BallBlobPipeline ballPipeline;
        public final VisionProcessor goalProcessor;
        public final DetectionProvider goalProvider;

        public WebcamConfig(
                String name,
                BallBlobPipeline ballPipeline,
                VisionProcessor goalProcessor,
                DetectionProvider goalProvider) {
            if (name == null || name.isEmpty() || ballPipeline == null) {
                throw new IllegalArgumentException(
                        "webcam name and ball pipeline are required");
            }
            if ((goalProcessor == null) != (goalProvider == null)) {
                throw new IllegalArgumentException(
                        "goal processor and goal provider must be supplied together");
            }
            this.name = name;
            this.ballPipeline = ballPipeline;
            this.goalProcessor = goalProcessor;
            this.goalProvider = goalProvider;
        }
    }

    public static final class LimelightConfig {
        public final String name;
        public final int ballPipeline;
        public final int goalPipeline;

        public LimelightConfig(String name, int ballPipeline, int goalPipeline) {
            if (name == null || name.isEmpty() || ballPipeline < 0 || goalPipeline < 0) {
                throw new IllegalArgumentException("limelight name and pipeline IDs are required");
            }
            this.name = name;
            this.ballPipeline = ballPipeline;
            this.goalPipeline = goalPipeline;
        }
    }

    private final List<WebcamConfig> webcams;
    private final List<LimelightConfig> limelightConfigs;
    private final List<VisionPortal> portals = new ArrayList<>();
    private final List<Limelight3A> limelights = new ArrayList<>();
    private final List<LimelightConfig> activeLimelightConfigs = new ArrayList<>();

    public CameraHardware(
            List<WebcamConfig> webcams,
            List<LimelightConfig> limelights) {
        this.webcams = new ArrayList<>(webcams);
        this.limelightConfigs = new ArrayList<>(limelights);
    }

    @Override
    public void init(HardwareMap hardwareMap) {
        try {
            for (WebcamConfig config : webcams) {
                VisionPortal.Builder builder = new VisionPortal.Builder()
                        .setCamera(hardwareMap.get(WebcamName.class, config.name))
                        .setCameraResolution(new Size(1280, 720))
                        .addProcessor(config.ballPipeline)
                        .enableLiveView(true);
                if (config.goalProcessor != null) {
                    builder.addProcessor(config.goalProcessor);
                }
                portals.add(builder.build());
            }

            for (LimelightConfig config : limelightConfigs) {
                Limelight3A limelight =
                        hardwareMap.get(Limelight3A.class, config.name);
                limelight.setPollRateHz(100);
                limelight.pipelineSwitch(config.ballPipeline);
                limelight.start();
                limelights.add(limelight);
                activeLimelightConfigs.add(config);
            }
        } catch (RuntimeException exception) {
            throw new RobotInitializationException(
                    "CameraHardware",
                    "check webcam/Limelight names and camera configuration",
                    exception);
        }
    }

    public List<VisionDetection> getBallDetections() {
        List<VisionDetection> result = new ArrayList<>();
        for (int i = 0; i < webcams.size(); i++) {
            for (VisionDetection detection : webcams.get(i).ballPipeline.getDetections()) {
                result.add(withSource(detection, webcams.get(i).name));
            }
        }
        for (int i = 0; i < limelights.size(); i++) {
            result.addAll(readLimelightDetections(
                    limelights.get(i), activeLimelightConfigs.get(i), true));
        }
        return result;
    }

    public List<VisionDetection> getGoalDetections() {
        List<VisionDetection> result = new ArrayList<>();
        for (WebcamConfig config : webcams) {
            if (config.goalProvider != null) {
                for (VisionDetection detection : config.goalProvider.getDetections()) {
                    result.add(withSource(detection, config.name));
                }
            }
        }
        for (int i = 0; i < limelights.size(); i++) {
            result.addAll(readLimelightDetections(
                    limelights.get(i), activeLimelightConfigs.get(i), false));
        }
        return result;
    }

    public void useGoalPipelines() {
        for (int i = 0; i < limelights.size(); i++) {
            limelights.get(i).pipelineSwitch(activeLimelightConfigs.get(i).goalPipeline);
        }
    }

    public void useBallPipelines() {
        for (int i = 0; i < limelights.size(); i++) {
            limelights.get(i).pipelineSwitch(activeLimelightConfigs.get(i).ballPipeline);
        }
    }

    private List<VisionDetection> readLimelightDetections(
            Limelight3A limelight,
            LimelightConfig config,
            boolean balls) {
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid() || result.getStaleness() > 250) {
            return Collections.emptyList();
        }
        if (balls) {
            List<VisionDetection> detections = new ArrayList<>();
            List<LLResultTypes.DetectorResult> detectorResults =
                    result.getDetectorResults();
            if (detectorResults != null) {
                for (LLResultTypes.DetectorResult detection : detectorResults) {
                    detections.add(new VisionDetection(
                            config.name,
                            VisionDetection.Kind.BALL,
                            detection.getClassName(),
                            detection.getConfidence(),
                            detection.getTargetXDegrees(),
                            detection.getTargetYDegrees(),
                            detection.getTargetArea(),
                            Range.clip(0.5 + detection.getTargetXDegrees() / 60.0, 0, 1),
                            Range.clip(0.5 - detection.getTargetYDegrees() / 45.0, 0, 1)));
                }
            }
            return detections;
        }
        return Collections.singletonList(new VisionDetection(
                config.name,
                VisionDetection.Kind.GOAL,
                "goal",
                1.0,
                result.getTx(),
                result.getTy(),
                result.getTa(),
                Range.clip(0.5 + result.getTx() / 60.0, 0, 1),
                Range.clip(0.5 - result.getTy() / 45.0, 0, 1)));
    }

    private VisionDetection withSource(VisionDetection detection, String source) {
        return new VisionDetection(
                source, detection.kind(), detection.label(), detection.confidence(),
                detection.tx(), detection.ty(), detection.area(),
                detection.cxNorm(), detection.cyNorm());
    }

    @Override
    public void stop() {
        for (Limelight3A limelight : limelights) {
            limelight.stop();
        }
        for (VisionPortal portal : portals) {
            portal.close();
        }
    }
}
