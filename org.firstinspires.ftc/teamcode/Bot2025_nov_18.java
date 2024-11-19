package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "Starterbot2025MecanumNov18")
public class Bot2025_nov_18 extends LinearOpMode {

  private DcMotor frontLeft;
  private DcMotor backRight;
  private DcMotor frontRight;
  private DcMotor backLeft;
  private DcMotor arm;
  private DcMotor arm2;
  //private DcMotor wrist;
  //private DcMotor wrist2;
  private Servo claw;
  private CRServo intake;

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
  //int targetWrist;
  String WALL_GRAB;
  String WALL_UNHOOK;
  String HOVER_HIGH;
  String CLIP_HIGH;

  /**
   * This function is executed when this Op Mode is selected.
   */
  @Override
  public void runOpMode() {
    frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
    backRight = hardwareMap.get(DcMotor.class, "backRight");
    frontRight = hardwareMap.get(DcMotor.class, "frontRight");
    backLeft = hardwareMap.get(DcMotor.class, "backLeft");
    arm = hardwareMap.get(DcMotor.class, "arm");
    arm2 = hardwareMap.get(DcMotor.class, "arm2");
    //wrist = hardwareMap.get(DcMotor.class, "wrist");
    //wrist2 = hardwareMap.get(DcMotor.class, "wrist2");
    claw = hardwareMap.get(Servo.class, "claw");
    intake = hardwareMap.get(CRServo.class, "intake");

    // Put initialization blocks here.
    frontLeft.setDirection(DcMotor.Direction.REVERSE);
    backRight.setDirection(DcMotor.Direction.REVERSE);
    //wrist2.setDirection(DcMotor.Direction.REVERSE);
    frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    MANUAL = "MANUAL";
    INTAKE = "INTAKE";
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
        GAMEPAD_INTAKE();
        STATE_MACHINE();
        MECANUM_DRIVE();
        TELEMETRY();
        arm.setTargetPosition(targetArm);
        arm2.setTargetPosition(targetArm);
        //wrist.setTargetPosition(targetWrist);
        //wrist2.setTargetPosition(targetWrist);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(1);
        arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm2.setPower(1);
        /*wrist.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wrist2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        wrist.setPower(1);
        wrist2.setPower(1);*/
      }
    }
  }

  /**
   * Describe this function...
   */
  private void GAMEPAD_INPUT_STATE() {
    if (gamepad1.a) {
      currentState = INTAKE;
    } 
    else if (gamepad1.b && !lastGrab) {
      if (currentState.equals(WALL_GRAB)) {
        currentState = WALL_UNHOOK;
      } 
      else {
        currentState = WALL_GRAB;
      }
    } 
    else if (gamepad1.y && !lastHook) {
      if (currentState.equals(HOVER_HIGH)) {
        currentState = CLIP_HIGH;
      } 
      else {
        currentState = HOVER_HIGH;
      }
    } 
    else if (gamepad1.x) {
      currentState = LOW_BASKET;
    } 
    else if (gamepad1.left_bumper) {
      currentState = INIT;
    }
    lastGrab = gamepad1.b;
    lastHook = gamepad1.y;
  }

  /**
   * Describe this function...
   */
  private void GAMEPAD_INPUT_TOGGLE() {
    if (gamepad1.right_bumper && !lastBump) {
      clawOpen = !clawOpen;
      if (clawOpen) {
        claw.setPosition(0.7);
      } else {
        claw.setPosition(0.9);
      }
    }
    lastBump = gamepad1.right_bumper;
  }

  /**
   * Describe this function...
   */
  private void GAMEPAD_INTAKE() {
    if (gamepad1.right_trigger > 0.1) {
      intake.setPower(1);
    } else if (gamepad1.left_trigger > 0.1) {
      intake.setPower(-1);
    } else {
      intake.setPower(0);
    }
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
      leftFrontPower = leftFrontPower / max;
      rightFrontPower = rightFrontPower / max;
      leftBackPower = (float) (leftBackPower / max);
      rightBackPower = (float) (rightBackPower / max);
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
      targetArm += 50;
    } else if (gamepad1.dpad_down) {
      currentState = MANUAL;
      targetArm += -50;
    } else if (gamepad1.dpad_left) {
      currentState = MANUAL;
      //targetWrist += 20;
    } else if (gamepad1.dpad_right) {
      currentState = MANUAL;
      //targetWrist += -20;
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
    //telemetry.addData("Wrist Position", wrist.getCurrentPosition());
    //telemetry.addData("Wrist Power", wrist.getPower());
    //telemetry.addData("Wrist2 Position", wrist2.getCurrentPosition());
    //telemetry.addData("Wrist2 Power", wrist2.getPower());
    telemetry.update();
  }

  /**
   * Describe this function...
   */
  private void STATE_MACHINE() {
    if (currentState.equals(INIT)) {
      targetArm = 300;
      //targetWrist = 0;
    } else if (currentState.equals(INTAKE)) {
      targetArm = 450;
      //targetWrist = 270;
    } else if (currentState.equals(WALL_GRAB)) {
      targetArm = 1100;
      //targetWrist = 10;
    } else if (currentState.equals(WALL_UNHOOK)) {
      targetArm = 1700;
      //targetWrist = 10;
    } else if (currentState.equals(HOVER_HIGH)) {
      targetArm = 2600;
      //targetWrist = 10;
    } else if (currentState.equals(CLIP_HIGH)) {
      targetArm = 2100;
      //targetWrist = 10;
    } else if (currentState.equals(LOW_BASKET)) {
      targetArm = 2500;
      //targetWrist = 270;
    } else {
      currentState = MANUAL;
    }
  }
}
