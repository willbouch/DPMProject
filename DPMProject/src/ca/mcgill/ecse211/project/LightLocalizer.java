package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

import lejos.hardware.Sound;

/**
 * A class that deals with all of the light sensor functions, focused on localization.
 *
 */
public class LightLocalizer {
  private static final double convertRadDeg = 180 / Math.PI;
  /**
   * A private buffer to hold data received from the light sensor.
   */
  private static float[] sampleBuffer = new float[colorSample.sampleSize()];
  /**
   * A private buffer to store the relevant light intensity levels over time.
   */
  private static float[] colorArray = new float[LS_SAMPLE_SIZE];

  /**
   * A moving average of the light intensity level over time.
   */
  private static float movingAverage = 1;

  /**
   * This method uses a mean filter to determine whether the robot has crossed a line.
   * @return whether the robot crossed a line;
   */
  public static boolean isOnLine() {
    //Fetch intensity of red light, from 0-1
    colorSample.fetchSample(sampleBuffer, 0);


    //Store the average
    float lastMovingAverage = movingAverage;

    //Moving average technique from lecture slides. 
    //*1000 is scaling for clarity - range is now 0 - 1000
    movingAverage +=  ((float)1 / LS_SAMPLE_SIZE) * (sampleBuffer[0] * 1000 - colorArray[0]);

    //Calculate change in light levels (derivative)
    float deriv = (movingAverage - lastMovingAverage) / LS_SLEEP;

    //Update the stored colorValues with the new value before returning
    for (int i = 0; i < LS_SAMPLE_SIZE - 1; i++) {
      colorArray[i] = colorArray[i + 1];
    }
    colorArray[LS_SAMPLE_SIZE - 1] = sampleBuffer[0] * 1000;

    //Decide if line
    return deriv > LIGHT_CHANGE_THRESHOLD;
  }


  //turned to make sure when light localizer misses the first line, it only does it once
  static boolean turned = false;

  /**
   * A method to be called when the robot is near a corner.
   * This will position the robot directly on the nearest tile corner, 
   * and orient it towards 0 degrees (positive y).
   */
  public static void moveToCorner() {

    for (int i = 0; i < LS_SAMPLE_SIZE; i++) {
      colorArray[i] = 1000;
    }

    //Initialize vars. 
    //Angles will store angles it crosses each line during rotation
    int crossedLines = 0;
    double[] angles = {0,0,0,0};
    Driver.setSpeed(FORWARD_SPEED / 2);

    //Begin rotation to find line angles
    Driver.turnBy(-360, true);

    while (crossedLines != 4) {
      //If a line is detected
      if (isOnLine()) {

        //Signify a line is crossed and update vars accordingly
        Sound.beep();
        angles[crossedLines] = odometer.getXyt()[2]*convertRadDeg;
        if(angles[crossedLines] < 0 ) angles[crossedLines]+= 360;
        crossedLines++;

        //Reset if it was on the other side
        if (crossedLines == 1 && angles[0] < 315 && angles[0] > 225 && turned == false) {
          turned=true;
          leftMotor.stop();
          rightMotor.stop();
          Driver.turnBy(25+90, false);
          odometer.setTheta(0);
          moveToCorner();
          return;
        }

        //Reset light levels in buffer - otherwise it takes a bit to update the buffer.
        for (int i = 0; i < LS_SAMPLE_SIZE; i++) {
          colorArray[i] = 1000;
        }

      }

      //Sleep
      try {
        Thread.sleep(LS_SLEEP);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }



    //These are the arc angles formed by the horizontal(X) and vertical(Y) lines
    double thetaX;
    double thetaY;

    thetaY = angles[2] - angles[0] ;
    thetaX = angles[3] - angles[1] ;

    //These now represent the distance from their first angle to the X/Y axis
    thetaX /= 2.0;
    thetaY /= 2.0;

    //For each angle, calculate the rotation needed to fix the bearing, then average the two.

    double avgTheta = ((thetaY + angles[0] - 270) % 360) 
        + ((thetaX + angles[1] - 180) % 360);
    avgTheta /= 2.0;


    //Finish rotation
    Driver.turnBy(-angles[3], false);

    //Signify angle is fixing and fix
    Driver.turnBy(avgTheta, false);

    //Sound.beep();
    odometer.setTheta(0);
  }
}
