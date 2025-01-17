/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="My Second TeleOp", group="Iterative OpMode")

public class MySecondTeleOp extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;
    private DcMotor backLeft = null;
    private DcMotor linAc = null;
    private DcMotor linAcRotation = null;
   // private Servo gate = null;

    private Servo wrist = null;

    // Setup a variable for each drive wheel to save power level for telemetry
    double leftPower = 0;
    double rightPower = 0;

    int linAcRotationTarget = 0;
    boolean buttonCheck = false;
    boolean open = false;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeft = hardwareMap.get(DcMotor.class, "front left");
        frontRight = hardwareMap.get(DcMotor.class, "front right");
        backRight = hardwareMap.get(DcMotor.class, "back right");
        backLeft = hardwareMap.get(DcMotor.class, "back left");
        linAc = hardwareMap.get(DcMotor.class, "linear actuator");
        linAcRotation = hardwareMap.get(DcMotor.class, "linear actuator rotation");
       // gate = hardwareMap.get(Servo.class, "gate servo");
        wrist = hardwareMap.get(Servo.class, "wrist servo");


        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        linAcRotation.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        linAcRotation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linAcRotation.setPower(0.5);

        //Defining variable stuff below
       // moveGate(open);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {


        // Choose to drive using either Tank Mode, or POV Mode
        // Comment out the method that's not used.  The default below is POV.

        // POV Mode uses left stick to go forward, and right stick to turn.
        // - This uses basic math to combine motions and is easier to drive straight.
//        double drive = -gamepad1.left_stick_y;
//        double turn  =  gamepad1.right_stick_x;
//        leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
//        rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

        // Tank Mode uses one stick to control each wheel.
        // - This requires no math, but it is hard to drive forward slowly and keep straight.
         leftPower  = -gamepad1.left_stick_y ;
         rightPower = -gamepad1.right_stick_y ;

        // Send calculated power to wheels
        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);

        linAcRotation.setTargetPosition(linAcRotationTarget);

        //This controls the linAcRotation's target
        if(gamepad1.right_trigger >= 0.1){
            linAcRotationTarget = linAcRotationTarget + 2;
            linAcRotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        if(gamepad1.right_bumper){
            linAcRotationTarget = linAcRotationTarget - 2;
            linAcRotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
//LinAcRotation board angle is 465 from starting position

        if(gamepad1.a && !buttonCheck){
            buttonCheck = true;
        }

        if(!gamepad1.a && buttonCheck){
            buttonCheck = false;
            open = !open;
        }

        if(gamepad1.left_bumper){
            wrist.setPosition(1);
        }
        if(gamepad1.left_trigger >= 0.1) {
            wrist.setPosition(0.5);
        }

/*
        if(gamepad1.y){
            linAcRotationTarget = 465;
            linAcRotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            wrist.setPosition(1);
        }
        else{
            linAcRotationTarget = 0;
            linAcRotation.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            wrist.setPosition(0.25);
        }
*/
       // moveGate(open);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
        telemetry.addData("Current LinAc Rotation", linAcRotation.getCurrentPosition());
        telemetry.addData("Wrist Position", wrist.getPosition());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
    //public void moveGate(boolean isGateOpen){
    //    if(isGateOpen){
    //        gate.setPosition(1);
    //    }
    //    else{
    //        gate.setPosition(0.75);
    //    }

    }
/*
    public void moveWrist(boolean isWristDown){
        if(isWristDown){
            wrist.setPosition(0);
        }
        else{
            wrist.setPosition(0.15);
        }

    }

*/

//}
