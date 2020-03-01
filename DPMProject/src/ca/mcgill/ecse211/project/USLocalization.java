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
   * The number of invalid samples seen by {@code filter()} so far.
   */
  private static int invalidSampleCount;

  /**
   * The distance remembered by the {@code filter()} method.
   */
  private static int prevDistance;

  /**
   * Wall distance
   */
  private int d = 30;

  /**
   * gap distance
   */
  private int k = 5;

  private double theta;


  /**
   * The main method of the Ultrasonic localizer class, localizes the robot to tile position 1,1 
   * with a bearing of 0 degrees.
   */
  @Override
  public void run() {
    double alpha;  //Angle to the back
    double beta;  //Angle to the left 
    double turnangle;  //Angle to add to orient the robot 
    int turnOffset = 24;
    int distFromUSToWheels = 0;

    //Set the rotation speed of the motors
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);

    // Rotate until we don't see a wall
    leftMotor.backward();
    rightMotor.forward();
    while(readUsDistance() < d + k);

    // Keep Rotating until we see a wall
    while(readUsDistance() > d);    

    // Record angle alpha for the first wall
    alpha = odometer.getXyt()[2];    

    //switch direction and wait until we don't see a wall
    leftMotor.forward();
    rightMotor.backward();
    while(readUsDistance() < d + k);

    // Keep Rotating until we see a wall
    while(readUsDistance() > d);

    //Record angle beta for the first wall
    beta = odometer.getXyt()[2];

    //Calculate angle we would need to rotate 
    turnangle = getRotAngle(alpha, beta);

    leftMotor.rotate(-((Driver.convertAngle(turnangle+turnOffset))), true);
    rightMotor.rotate(Driver.convertAngle(turnangle+turnOffset), false);

    double distA =readUsDistance();//Read distance from the back wall

    leftMotor.rotate(-Driver.convertAngle(90), true);
    rightMotor.rotate(Driver.convertAngle(90), false);

    double distB =readUsDistance(); //Read distance from the left wall

    leftMotor.rotate(-Driver.convertAngle(90), true);
    rightMotor.rotate(Driver.convertAngle(90), false);
    
    //Move x distance
    Driver.moveDistFwd((int) (100*(TILE_SIZE+distFromUSToWheels-distA)), FORWARD_SPEED); // Draw a SIDE

    leftMotor.rotate(-Driver.convertAngle(90), true);
    rightMotor.rotate(Driver.convertAngle(90), false);
    
    //Move y distance
    Driver.moveDistFwd((int) (100*(TILE_SIZE+distFromUSToWheels-distB)), FORWARD_SPEED); // Draw a SIDE

    //Robot is now on (1,1) facing 0 relative to corner being (0,0)
    //We stop the motors so that a new thread can start from scratch
    Driver.setSpeeds(0, 0);
  }

  /**
   * Returns the filtered distance between the US sensor and an obstacle in cm.
   * 
   * @return the filtered distance between the US sensor and an obstacle in cm
   */
  public int readUsDistance() {
    usSensor.fetchSample(usData, 0);
    // extract from buffer, convert to cm, cast to int, and filter
    return filter((int) (usData[0] * 100.0));
  }

  /**
   * Rudimentary filter - toss out invalid samples corresponding to null signal.
   * 
   * @param distance raw distance measured by the sensor in cm
   * @return the filtered distance in cm
   */
  public static int filter(int distance) {
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

  /**
   * Calculates the theta the robot is at based on the points at which it found a specific distance
   * @param alpha - The first angle it found the distance
   * @param beta - The second angle it found the distance
   * @return the theta to turn (in degrees) to fix the odometer error
   */
  public double getTheta(double alpha , double beta) {
    if(alpha < beta)
    {
      theta = 45 - (alpha +beta)/2 + 180;
    }

    else if(beta < alpha)
    {
      theta = 225 - (alpha +beta)/2 + 180;
    }

    return theta;
  }

  /** 
   * Gives the angle required to fix the robots bearing based on the points at which it found a specific distance
   * @param alpha - The first angle it found the distance
   * @param beta - The second angle it found the distance
   * @return the theta to turn (in degrees) to have correct bearing
   */
  public double getRotAngle(double alpha,double beta) {
    return getTheta(alpha, beta) + odometer.getXyt()[2];
  }
}