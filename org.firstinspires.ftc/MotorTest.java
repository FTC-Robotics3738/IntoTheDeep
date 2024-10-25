package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "test")

public class MotorTest extends LinearOpMode {
    private DcMotor motor;
    
    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "motor");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        boolean motorXPositionTarget;
        int i = 0;
        int y = 0;
        waitForStart();
        telemetry.addData("Current motor speed", motor.getPower());
        telemetry.addData("Motor position target", motor.getCurrentPosition());
        telemetry.addData("iter", (int)y);
        if (opModeIsActive()) {
            y++;
            motorRun(.3, 500);
            motorXPositionTarget = gamepad1.a;
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            telemetry.addData("Button status", gamepad1.a);
            telemetry.update();
            
            while (opModeIsActive()) {
                telemetry.update();
                y++;
                //motor.setPower(gamepad1.left_stick_x*.7);
                if (gamepad1.a == true) {
                    motor.setPower(.4);
                } else {
                    motor.setPower(0);
                }
                
                if (motorXPositionTarget == true) {
                    motor.setTargetPosition(i+=700);
                    y++;
                }
                else {
                    y--;
                }
            }
        }
    }
    
    public void motorRun(double motorSpeed, int timeToRun) {
        motor.setPower(motorSpeed);
        sleep(timeToRun);
        motor.setPower(0);
    }
    
}
