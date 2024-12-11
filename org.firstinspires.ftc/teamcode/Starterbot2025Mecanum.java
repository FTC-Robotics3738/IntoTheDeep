package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import java.lang.Math.*;

@TeleOp(name = "SecondaryTeleOp")
public class Starterbot2025Mecanum extends LinearOpMode {

  private DcMotor frontLeft;
  private DcMotor backRight;
  private DcMotor frontRight;
  private DcMotor backLeft;
  private DcMotor arm;
  private DcMotor arm2;
  private DcMotor wrist;
  private Servo claw;

  String currentState;
  boolean lastGrab;
  boolean clawOpen;
  boolean lastBump;
  int targetArm;
  String INTAKE;
  String LOW_BASKET;
  String INIT;
  boolean lastHook;
  String MANUAL;
  int targetWrist;
  String WALL_GRAB;
  String WALL_UNHOOK;
  String HOVER_HIGH;
  String CLIP_HIGH;
  boolean goingUp;

  /**
   * This function is executed when this Op Mode is selected.
   */
  @Override
  public void runOpMode() {
    frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
    backRight = hardwareMap.get(DcMotor.class, "backRight");
    frontRight = hardwareMap.get(DcMotor.class, "frontRight");
    backLeft = hardwareMap.get(DcMotor.class, "backLeft");
    arm = hardwareMap.get(DcMotor.class, "armLeft");
    arm2 = hardwareMap.get(DcMotor.class, "armRight");
    // wrist = hardwareMap.get(DcMotor.class, "wrist");
    claw = hardwareMap.get(Servo.class, "claw");

    // Put initialization blocks here.
    frontLeft.setDirection(DcMotor.Direction.REVERSE);
    backRight.setDirection(DcMotor.Direction.REVERSE);
    arm2.setDirection(DcMotor.Direction.REVERSE);
    frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    MANUAL = "MANUAL";
    WALL_GRAB = "WALL_GRAB";
    WALL_UNHOOK = "WALL_UNHOOK";
    HOVER_HIGH = "HOVER_HIGH";
    CLIP_HIGH = "CLIP_HIGH";
    LOW_BASKET = "LOW_BASKET";
    INIT = "INIT";
    currentState = INIT;
    clawOpen = false;
    lastBump = false;
    lastHook = false;
    lastGrab = false;
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        GAMEPAD_INPUT_STATE();
        GAMEPAD_INPUT_TOGGLE();
        GAMEPAD_INPUT_MANUAL();
        STATE_MACHINE();
        MECANUM_DRIVE();
        TELEMETRY();
        arm.setTargetPosition(targetArm);
        arm2.setTargetPosition(targetArm);
        // wrist.setTargetPosition(targetWrist);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // arm.setPower(1);
        arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(targetArm < 0)
        {
          targetArm = 0;
        }
      }
    }
  }

  /**
   * Describe this function...
   */
  private void GAMEPAD_INPUT_STATE() {
    if (gamepad1.a) {
      //claw close;
      claw.setPosition(0.1);
    } 
    else if (gamepad1.b) {
      //claw open; 
      claw.setPosition(0.9);
    } 
    else if (gamepad1.y) {
      // raise arm
      // arm.setTargetPosition(100);
      goingUp = true;
      Math.min(targetArm+=10, 1400);
      arm.setPower(.5);
      arm2.setPower(.5);
    } 
    else if (gamepad1.x) {
      //drop arm;
      goingUp = false;
      // Math.max(targetArm-=100, 0);
      targetArm = 0;
      // targetArm-=100;
      // arm.setTargetPosition(1000);
      arm.setPower(-0.5);
      arm2.setPower(-0.5);
    } 
    else if (gamepad1.left_bumper) {
      currentState = INIT;
    }
  }

  /**
   * Describe this function...
   */
  private void GAMEPAD_INPUT_TOGGLE() {
    if (gamepad1.right_bumper && !lastBump) {
      clawOpen = !clawOpen;
      if (clawOpen) {
        claw.setPosition(0.0);
      } else {
        claw.setPosition(0.9);
      }
    }
    lastBump = gamepad1.right_bumper;
  }

  /**
   * Describe this function...
   */
  private void MECANUM_DRIVE() {
    float Strafe;
    float FB;
    float Turn;
    double leftFrontPower;
    double rightFrontPower;
    float leftBackPower;
    float rightBackPower;
    double max;

    // Determining movement based on gamepad inputs
    FB = -gamepad1.left_stick_y;
    Strafe = gamepad1.left_stick_x;
    Turn = gamepad1.right_stick_x;
    leftFrontPower = (FB - Strafe * 0.7) - Turn;
    rightFrontPower = (FB + Strafe * 0.7) + Turn;
    leftBackPower = (FB + Strafe * 1) - Turn;
    rightBackPower = (FB - Strafe * 1) + Turn;
    // The below section "clips" the values to remain within the expected range
    max = JavaUtil.maxOfList(JavaUtil.createListWith(Math.abs(leftFrontPower), Math.abs(rightFrontPower), Math.abs(leftBackPower), Math.abs(rightBackPower)));
    if (max > 1) {
      leftFrontPower = leftFrontPower / max * FB;
      rightFrontPower = rightFrontPower / max * FB;
      leftBackPower = (float) (leftBackPower / max) * FB;
      rightBackPower = (float) (rightBackPower / max) * FB;
    }
    // Setting Motor Power
    frontLeft.setPower(leftFrontPower);
    frontRight.setPower(rightFrontPower);
    backLeft.setPower(leftBackPower);
    backRight.setPower(rightBackPower);
  }

  /**
   * Describe this function...
   */
  private void GAMEPAD_INPUT_MANUAL() {
    if (gamepad1.dpad_up) {
      currentState = MANUAL;
      Math.min(targetArm+=10, 1400);
    } else if (gamepad1.dpad_down) {
      currentState = MANUAL;
      Math.max(targetArm-=10, 0);
    // } else if (gamepad1.dpad_left) {
    //   currentState = MANUAL;
    //   targetWrist += 20;
    // } else if (gamepad1.dpad_right) {
    //   currentState = MANUAL;
    //   targetWrist += -20;
    }
  }

  /**
   * Describe this function...
   */
  private void TELEMETRY() {
    telemetry.addData("STATE:", currentState);
    telemetry.addData("Claw Position", clawOpen ? "Open" : "Closed");
    telemetry.addData("Arm Position", arm.getCurrentPosition());
    telemetry.addData("Arm Power", arm.getPower());
    telemetry.addData("Arm2 Position", arm2.getCurrentPosition());
    telemetry.addData("Arm2 Power", arm2.getPower());
    telemetry.addData("TargetArm destination", targetArm);
    // telemetry.addData("Wrist Position", wrist.getCurrentPosition());
    // telemetry.addData("Wrist Power", wrist.getPower());
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  private void STATE_MACHINE() {
    if (currentState.equals(INIT)) {
      targetArm = 0;
      targetWrist = 0;
      claw.setPosition(0.0);
    }
    // } else if (currentState.equals(WALL_GRAB)) {
    //   targetArm = 1100;
    //   targetWrist = 10;
    // } else if (currentState.equals(WALL_UNHOOK)) {
    //   targetArm = 1700;
    //   targetWrist = 10;
    // } else if (currentState.equals(HOVER_HIGH)) {
    //   targetArm = 2600;
    //   targetWrist = 10;
    // } else if (currentState.equals(CLIP_HIGH)) {
    //   targetArm = 2100;
    //   targetWrist = 10;
    // } else if (currentState.equals(LOW_BASKET)) {
    //   targetArm = 2500;
    //   targetWrist = 270;
    // } else {
    //   currentState = MANUAL;
    // }
  }
}

