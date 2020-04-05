package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

/**
 * A class to localize the robot to be on tile position (1,1) relative to corner being (0,0)
 * with an orientation of 0 degrees using the ultrasonic sensor.
 */
public class USLocalization implements Runnable {


  /**
   * The buffer in which we put the sensor readings.
   */
  private float[] usData = new float[usSensor.sampleSize()];

  /**
   * A private buffer to hold data received from the left light sensor.
   */
  private static float[] leftBuffer = new float[leftColorSample.sampleSize()];

  /**
   * A private buffer to hold data received from the right light sensor.
   */
  private static float[] rightBuffer = new float[rightColorSample.sampleSize()];

  /**
   * A private buffer to store the relevant left light intensity levels over time.
   */
  private static float[] leftColorArray = new float[LS_SAMPLE_SIZE];

  /**
   * A private buffer to store the relevant right light intensity levels over time.
   */
  private static float[] rightColorArray = new float[LS_SAMPLE_SIZE];

  /**
   * The number of invalid samples seen by {@code filter()} so far.
   */
  private static int invalidSampleCount;

  /**
   * The distance remembered by the {@code filter()} method.
   */
  private static double prevDistance;

  /**
   * A moving average of the left light intensity level over time.
   */
  private static float leftMovingAverage = 1;

  /**
   * A moving average of the right light intensity level over time.
   */
  private static float rightMovingAverage = 1;

  /**
   * The distance in cm to consider infinity (for filtering)
   */
  private static final int infinity = 50;

  /**
   * The main method of the Ultrasonic localizer class, localizes the robot to tile position 1,1 
   * with a bearing of 0 degrees.
   */
  @Override
  public void run() {

    //Set the rotation speed of the motors
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);

    while(true) {
      double dist = readUsDistance();
      leftMotor.forward();
      rightMotor.backward();
      if(dist < infinity) {
        //It is not seeing infinity so rotate 45 degrees and redo
        odometer.setTheta(0);
        Driver.turnBy(45);
      } else {
        //We are oriented towards infinity
        break;
      }
    }

    //30 degrees until we find the wall
    while(true) {
      double dist = 0;
      Driver.turnBy(30);
      for(int i = 0; i < 10; i++) {
        dist += readUsDistance();
      }
      double avg = dist / 10;
      if(avg < infinity) {
        break;
      }
    }

    //5 degrees until we are perpendicular
    double prevDist = 255;
    while(true) {
      double dist = 0;
      Driver.turnBy(5);
      for(int i = 0; i < 10; i++) {
        double d = readUsDistance();
        if(d > infinity) {
          i--;
        }
        else{
          dist += d;
        }     
      }
      double avg = dist / 10;
      if(avg > prevDist) {
        //Go back to adjust with 1 degree rotations
        Driver.turnBy(-20);
        break;
      }
      prevDist = avg;
    }

    //1 degree until we are perpendicular
    prevDist = 255;
    while(true) {
      double dist = 0;
      Driver.turnBy(1);
      for(int i = 0; i < 10; i++) {
        double d = readUsDistance();
        if(d > infinity) {
          i--;
        }
        else{
          dist += d;
        }   
      }
      double avg = dist / 10;
      if(avg > prevDist) {
        Driver.turnBy(-1);
        break;
      }
      prevDist = avg;
    }


    //Then we want to make the robot go backward until both sensors detect a line
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    leftMotor.backward();
    rightMotor.backward();
    waitLineDetection();
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    Driver.moveStraightFor(SENSOR_TO_CENTER);

    Main.sleepFor(3000);

    //Turn 90 degrees and check if we face the wall or infinity
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    Driver.turnBy(-90);

    //Then we want to make the robot go forward until both sensors detect a line
    leftMotor.forward();
    rightMotor.forward();
    waitLineDetection();
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    Driver.moveStraightFor(SENSOR_TO_CENTER);

    //Then we orient towards relative 0 degree and update the odometer values depending on initial location
    Driver.turnBy(-90);    

    //Then we adjust the odometer values
    if(GameParameters.getCorner_x() == MAX_X && GameParameters.getCorner_y() == MAX_Y) {
      odometer.setXyt((MAX_X - 1) * TILE_SIZE, (MAX_Y - 1) * TILE_SIZE, 180);
    }
    else if(GameParameters.getCorner_x() == MAX_X && GameParameters.getCorner_y() == 0) {
      odometer.setXyt((MAX_X - 1) * TILE_SIZE, TILE_SIZE, 270);
    }
    else if(GameParameters.getCorner_x() == 0 && GameParameters.getCorner_y() == 0) {
      odometer.setXyt(TILE_SIZE, TILE_SIZE, 0);
    }
    else {
      odometer.setXyt(TILE_SIZE, (MAX_Y - 1) * TILE_SIZE, 90);
    }
  }

  /**
   * This method samples the light sensors and terminates only when both have detected a line.
   */
  public static void waitLineDetection() {
    boolean left = true;
    boolean right = true;

    //Reset light levels in buffer - otherwise it takes a bit to update the buffer.
    for (int i = 0; i < LS_SAMPLE_SIZE; i++) {
      leftColorArray[i] = 1000;
      rightColorArray[i] = 1000;
    }

    while(left || right) {
      //We fetch the right light sensor and determine if line detected
      if(right) {
        rightColorSample.fetchSample(rightBuffer, 0);
        float lastMovingAverage = rightMovingAverage;
        rightMovingAverage +=  ((float)1 / LS_SAMPLE_SIZE) * (rightBuffer[0] * 1000 - rightColorArray[0]);
        float deriv = (rightMovingAverage - lastMovingAverage) / LS_SLEEP;
        for (int i = 0; i < LS_SAMPLE_SIZE - 1; i++) {
          rightColorArray[i] = rightColorArray[i + 1];
        }
        rightColorArray[LS_SAMPLE_SIZE - 1] = rightBuffer[0] * 1000;

        if(deriv > LIGHT_CHANGE_THRESHOLD) {
          right = false;
          rightMotor.setSpeed(0);
        }
      }

      //We fetch the left light sensor and determine if line detected
      if(left) {
        leftColorSample.fetchSample(leftBuffer, 0);
        float lastMovingAverage = leftMovingAverage;
        leftMovingAverage +=  ((float)1 / LS_SAMPLE_SIZE) * (leftBuffer[0] * 1000 - leftColorArray[0]);
        float deriv = (leftMovingAverage - lastMovingAverage) / LS_SLEEP;
        for (int i = 0; i < LS_SAMPLE_SIZE - 1; i++) {
          leftColorArray[i] = leftColorArray[i + 1];
        }
        leftColorArray[LS_SAMPLE_SIZE - 1] = leftBuffer[0] * 1000;

        if(deriv > LIGHT_CHANGE_THRESHOLD) {
          left = false;
          leftMotor.setSpeed(0);
        }
      }

      //Slow down the fetching
      Main.sleepFor(LS_SLEEP);
    }
  }

  /**
   * Returns the filtered distance between the US sensor and an obstacle in cm.
   * 
   * @return the filtered distance between the US sensor and an obstacle in cm
   */
  public double readUsDistance() {
    usSensor.fetchSample(usData, 0);
    // extract from buffer, convert to cm, cast to int, and filter
    return filter((usData[0] * 100.0));
  }

  /**
   * Rudimentary filter - toss out invalid samples corresponding to null signal.
   * 
   * @param distance raw distance measured by the sensor in cm
   * @return the filtered distance in cm
   */
  public static double filter(double distance) {
    if (distance >= 255 && invalidSampleCount < INVALID_SAMPLE_LIMIT) {
      // bad value, increment the filter value and return the distance remembered from before
      invalidSampleCount++;
      return prevDistance;
    } 
    //attempt to smooth out the reading by 1 tick,ex.d=12,13,15,255
    //will ignore 255 the first time
    if (distance >= 255){
      return 255; //max out the ultrasonic sensor at 255
    }
    else {
      if (distance < 255) {
        // distance went below 255: reset filter and remember the input distance.
        invalidSampleCount = 0;
      }
      prevDistance = distance;
      return distance;
    }
  }
}