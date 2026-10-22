//package org.firstinspires.ftc.teamcode.commands.drive;
//
//import com.arcrobotics.ftclib.controller.PIDController;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.hardware.limelightvision.LLResultTypes.*;
//import com.qualcomm.robotcore.*;
//import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
//import org.firstinspires.ftc.teamcode.framework.Command;
//import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
//
//import com.qualcomm.hardware.limelightvision.LLResultTypes.*;
//import com.qualcomm.robotcore.*;
//
//// Based on TheGodObject by casperwz
//
//public class AutonomousDriveCommand implements Command {
//    double x = 0.0, y = 0.0, theta = 0.0;
//
//    static final double p = 0.022, i = 0.02, d = 0.001;
//    static final double ph = 0.02, ih = 0.02, dh = 0.001;
//
//    //--Drive Motors--
//
//
//    //--Pinpoint--
//    private GoBildaPinpointDriver pinpoint;
//
//    private PIDController xCon = new PIDController(p, i, d);
//    private PIDController yCon = new PIDController(p, i, d);
//    private PIDController thetaCon = new PIDController(ph, ih, dh);
//
//    private Curves path;
//    private MoveScum profile;
//    private ElapsedTime timer;
//
//    private DriveSubsystem drive;
//
//    // Constants for Feedforward
//    // If your max speed is 40 inches/sec, Kv is roughly 1/40 (0.025) to convert inches/sec to motor power.
//    private final double kV = 0.025;
//
//    // Place this method inside your TheGodObject class, below runOpMode()
//    public void followPath(Curves path, MoveScum profile, double targetheta) {
//        ElapsedTime timer = new ElapsedTime();
//        double totPathDist = path.arclength;
//
//        // We need to know when the profile is technically finished
//        double accelTime = profile.getMaxV() / profile.getMaxA(); // Note: You might need to add getters in MoveScum for this, or just rely on velocity == 0
//
//        while (opModeIsActive()) {
//            drive.updatePose();
//            // 2. ASK THE PACE CAR (Where should we be right now?)
//            double currentTime = timer.seconds();
//            MoveScum.Profile targetState = profile.calculate(totPathDist, currentTime);
//
//            Point endPoint = path.getPosT(1.0);
//            if (targetState.v == 0 && Math.abs(endPoint.x - drive.x) < 2.0 && Math.abs(endPoint.y - drive.y) < 2.0) {
//                break; // This exits the while loop and moves to the next instruction
//            }
//
//            // 3. ASK THE MAP (Translate that distance into X, Y, and Velocity)
//            double t = path.getT(targetState.pos);
//            Point targetCoord = path.getPosT(t);
//            Point rawDerivative = path.getDerivT(t);
//
//            // Calculate the angle of our Feedforward velocity vector
//            double pathAngle = Math.atan2(rawDerivative.y, rawDerivative.x);
//
//            // Calculate pure Feedforward Power (Target Velocity * kV)
//            double ffPowerX = Math.cos(pathAngle) * targetState.v * kV;
//            double ffPowerY = Math.sin(pathAngle) * targetState.v * kV;
//
//            // 4. ASK THE CORRECTORS (PID)
//            double pidPowerX = xCon.calculate(drive.x, targetCoord.x);
//            double pidPowerY = yCon.calculate(drive.y, targetCoord.y);
//
//            // ANGLE WRAPPING FOR THE HEADING!
//            double headingError = targetheta - drive.theta;
//            while (headingError > 180) {
//                headingError -= 360;
//            }
//            while (headingError <= -180){
//                headingError += 360;
//            }
//            double pidPowerHeading = thetaCon.calculate(drive.theta, headingError);
//
//            // 5. COMBINE AND DRIVE
//            // Total Power = Proactive Push (FF) + Reactive Fix (PID)
//            double finalDriveX = -(ffPowerX + pidPowerX);
//            double finalDriveY = -(ffPowerY + pidPowerY);
//            double finalTurn = -pidPowerHeading;
//
//            // 6. drive
//            drive.driveWithHeading(finalDriveX, finalDriveY, finalTurn, drive.theta);
//        }
//        drive.driveWithHeading(0, 0, 0, drive.theta);
//    }
//}
//
//
//class Curves {
//    private Point start, p1, p2, end;
//
//    private double[] arctable;
//    double arclength;
//
//    public Curves(Point start, Point p1, Point p2, Point end){
//        this.start = start;
//        this.p1 = p1;
//        this.p2 = p2;
//        this.end = end;
//        lookup();
//    }
//
//    //--Math--
//    public Point getPosT(double t){
//        double u = 1 - t;
//
//        double x = (u*u*u * this.start.x) +
//                (3 * u*u * t * this.p1.x) +
//                (3 * u * t*t * this.p2.x) +
//                (t*t*t * this.end.x);
//
//        double y = (u*u*u * this.start.y) +
//                (3 * u*u * t * this.p1.y) +
//                (3 * u * t*t * this.p2.y) +
//                (t*t*t * this.end.y);
//
//        return new Point(x, y);
//    }
//    public Point getDerivT(double t){
//        double u = 1 - t;
//
//        double dx = (3 * u*u * (this.p1.x - this.start.x)) +
//                (6 * u * t * (this.p2.x - this.p1.x)) +
//                (3 * t*t * (this.end.x - this.p2.x));
//
//        double dy = (3 * u*u * (this.p1.y - this.start.y)) +
//                (6 * u * t * (this.p2.y - this.p1.y)) +
//                (3 * t*t * (this.end.y - this.p2.y));
//
//        return new Point(dx, dy);
//    }
//
//    //--arrays and stuff--
//
//    private void lookup(){
//        arctable = new double[101];
//        arctable[0] = 0;
//
//        double currTotDist = 0;
//        Point prevPoint = getPosT(0);
//
//        for(int i= 1; i <=100; i++){
//            double t = (double) i /100;
//            Point currPoint = getPosT(t);
//            double dx = currPoint.x-prevPoint.x;
//            double dy = currPoint.y - prevPoint.y;
//            double segDist = Math.sqrt((dx*dx)+(dy*dy));
//            currTotDist += segDist;
//            arctable[i] = currTotDist;
//            prevPoint = currPoint;
//        }
//        arclength = currTotDist;
//    }
//    public double getT(double target){
//        if(target <= 0) return 0;
//        if (target >= arclength) return 1;
//
//        int low = 0;
//        int high = 100;
//
//        while (low<high){
//            int mid = (low+high)/2;
//            if(arctable[mid] == target){
//                return (double) mid /100;
//            }
//
//            if(arctable[mid]<target){
//                low = mid+1;
//            } else {
//                high = mid;
//            }
//
//        }
//
//        int after = low;
//        int before = low-1;
//        double distafter = arctable[after];
//        double distbefore = arctable[before];
//
//        double segfrac = (target - distbefore)/(distafter-distbefore);
//
//        double tb = (double) before /100;
//        double ta = (double) after /100;
//
//        return tb + (segfrac*(ta-tb));
//    }
//
//
//}
//
//class Point {
//    public double x;
//    public double y;
//
//    public Point(double x, double y) {
//        this.x = x;
//        this.y = y;
//    }
//}
//
//class MoveScum {
//    private double maxV;
//    private double maxA;
//
//    public MoveScum(double maxV, double maxA){
//        this.maxV = maxV;
//        this.maxA = maxA;
//
//    }
//
//    public static class Profile{
//        public double pos;
//        public double v;
//    }
//
//
//    public Profile calculate(double totDist, double currTime){
//        Profile state = new Profile();
//
//        double at = maxV/maxA;
//        double ad = 0.5*maxA*at*at;
//        double maxVactual = maxV;
//
//        if (ad*2 > totDist){
//            ad = totDist/2.0;
//            at = Math.sqrt((2*ad)/maxA);
//            maxVactual = maxA*at;
//        }
//        double cruised = totDist- (2*ad);
//        double cruiset = (maxVactual == 0) ? 0 : (cruised / maxVactual);
//
//        double totime = (2 * at) + cruiset;
//
//        if(currTime >= totime){
//            state.pos = totDist;
//            state.v = 0;
//            return state;
//        }
//        if(currTime<at){
//            state.pos = 0.5*maxA*(currTime*currTime);
//            state.v = maxA * currTime;
//        }
//        else if (currTime < at + cruiset) {
//            double cruiseCurrt = currTime - at;
//
//            state.pos = ad + (maxVactual * cruiseCurrt);
//            state.v = maxVactual;
//
//        } else {
//            double timeLeft = totime - currTime;
//            double distanceLeftToBrake = 0.5 * maxA * (timeLeft * timeLeft);
//
//            state.pos = totDist - distanceLeftToBrake;
//            state.v = maxA * timeLeft;
//        }
//        if (maxA <= 0) throw new IllegalArgumentException("maxA must be positive");
//
//        return state;
//    }
//
//    public double getMaxA() {
//        return maxA;
//    }
//
//    public double getMaxV() {
//        return maxV;
//    }
//}