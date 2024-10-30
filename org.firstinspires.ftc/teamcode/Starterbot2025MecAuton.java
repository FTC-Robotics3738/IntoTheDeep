package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Starterbot2025MecAuton")
public class Starterbot2025MecAuton extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor arm;
    private DcMotor wrist;
    private Servo claw;
    private CRServo intake;

    @Override
    public void runOpMode() {
        // Hardware maps
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        arm = hardwareMap.get(DcMotor.class, "arm");
        wrist = hardwareMap.get(DcMotor.class, "wrist");
        claw = hardwareMap.get(Servo.class, "claw");
        intake = hardwareMap.get(CRServo.class, "intake");

        // Put initialization code here...
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        if (opModeIsActive) {
            // Put code here that runs once after start
            turnLeft(90, 200);
            moveStraight(.7, 2000, 200);
            
            while (opModeIsActive) {
                // Put loop code here!

            }
        }
    }
    // Put methods below, they allow for easier code changes (simple timers especially)

    public void moveStraight(double speed, int driveMs, int pauseMs) {
        backLeft.setPower(speed);
        backRight.setPower(speed);
        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        sleep(driveMs);
        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        sleep(pauseMs);
    }

    public void turnLeft(double degreesTurned, int pauseMs) {
        backLeft.setPower(-.6);
        backRight.setPower(.6);
        frontLeft.setPower(-.6);
        frontRight.setPower(.6);
        sleep((2220/90)*degreesTurned);
        backLeft.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        sleep(pauseMs);
    }
}