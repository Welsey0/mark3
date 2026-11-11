# Vision configuration

Panels is included through `com.bylazar:fullpanels:1.0.20`. The project is
already on FTC SDK 12.0.0. After Gradle sync, install Panels on the Robot
Controller and use its telemetry, camera stream, Limelight, and field tools.

Hive Vision's webcam detector is included as `HiveBallDetectorPipeline`. A
webcam configuration needs a configured `WebcamName` and that processor:

```java
HiveBallDetectorPipeline balls = new HiveBallDetectorPipeline();
CameraHardware cameras = new CameraHardware(
        Arrays.asList(new CameraHardware.WebcamConfig(
                "intakeWebcam", balls, null, null)),
        Arrays.asList(new CameraHardware.LimelightConfig(
                "limelight", 0, 1)));

robot = RobotContext.builder(hardwareMap, telemetry)
        .withCameras(cameras)
        .build();
```

Limelight pipeline `0` above is the ball detector and pipeline `1` is the
goal pipeline. Configure those pipelines on each Limelight before deploying.
`robot.camera().getBalls()` returns normalized ball detections from all
configured cameras. `robot.camera().getGoals()` returns Limelight `tx`, `ty`,
and `ta` or detections from a webcam processor supplied as both a
`VisionProcessor` and `DetectionProvider`.

Call `robot.camera().useGoalPipelines()` before reading Limelight goals and
`robot.camera().useBallPipelines()` before reading Limelight balls. Webcam
ball processors continue running in either mode.
