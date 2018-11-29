//use to test encoders, etc.

package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/*
 * Created by chun on 8/8/18 for robotics boot camp 2018.
 */

/*
   Edited by jonathon for OpenCV Autonomous testing
 */

@Autonomous

public class AutoTesting extends Base {
    private int stage = 0;
    private GoldAlignDetector detector;

    @Override
    public void init() {
        super.init();

        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");

        //set_marker_servo(ConstantVariables.K_MARKER_SERVO_UP);

        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 50; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        servoTest.setPosition(up_position);

        detector.enable();
    }

    @Override
    public void start() {

        super.start();
        reset_drive_encoders();
        reset_climb_encoders();
    }

    @Override
    public void loop() {

        super.loop();
        telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral
        telemetry.addData("X Pos" , detector.getXPosition()); // Gold X pos.

        switch (stage) {
            case 0:
                if(Math.abs(get_climb_enc()) > 4000){
                    moveClimber(0);
                    reset_climb_encoders();
                    stage++;
                }
                else{
                    climber.setPower(1);
                }

                break;

            case 1:
                if(auto_drive(-0.8, 2)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 2:
                if(detector.getAligned()){
                    stage++;
                }else{
                    stage++;
                }

                break;

            case 3:
                if(detector.getXPosition() > 280){
                    if(auto_turn(0.4, 10)){
                        reset_drive_encoders();
                        stage+=2;
                    }
                }
                else{
                   stage++;
                }

                break;

            case 4:
                if(detector.getXPosition() < 280) {
                    if (auto_turn(-0.4, 10)) {
                        reset_drive_encoders();
                        stage++;
                    }
                }
                else{
                    stage++;
                }

                break;

            case 5:
                if(auto_drive(0.8, 5)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 6:
                if(auto_turn(-0.5, 5)){ //left
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 7:

                servoTest.setPosition(drop_position);
                stage++;
                break;

            default:

                break;
        }
    }

    @Override
    public void stop(){
        detector.disable();
    }

}

