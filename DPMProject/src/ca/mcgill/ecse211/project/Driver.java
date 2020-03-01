package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.ACCELERATION;
import static ca.mcgill.ecse211.project.Resources.BASE_WIDTH;
import static ca.mcgill.ecse211.project.Resources.DIST_TO_DEG;
import static ca.mcgill.ecse211.project.Resources.TILE_SIZE;
import static ca.mcgill.ecse211.project.Resources.WHEEL_RAD;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.rightMotor;

/**
 * 
 * Driver has a few methods that assist in controlling the motors
 */
public class Driver {
  /**
   * Moves the robot straight for the given distance.
   * 
   * @param distance in feet (tile sizes), may be negative
   */
  public static void moveStraightFor(double distance) {
    leftMotor.rotate(convertDistance(distance * TILE_SIZE), true);
    rightMotor.rotate(convertDistance(distance * TILE_SIZE), false);
  }

  /**
   * Turns the robot by a specified angle. Note that this method is different from
   * {@code Navigation.turnTo()}. For example, if the robot is facing 90 degrees, calling
   * {@code turnBy(90)} will make the robot turn to 180 degrees, but calling
   * {@code Navigation.turnTo(90)} should do nothing (since the robot is already at 90 degrees).
   * 
   * @param angle the angle by which to turn, in degrees
   */
  public static void turnBy(double angle) {
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-convertAngle(angle), false);
  }
  
  /**
   * Turns the robot by a specified angle. Note that this method is different from
   * {@code Navigation.turnTo()}. For example, if the robot is facing 90 degrees, calling
   * {@code turnBy(90)} will make the robot turn to 180 degrees, but calling
   * {@code Navigation.turnTo(90)} should do nothing (since the robot is already at 90 degrees).
   * 
   * @param angle the angle by which to turn, in degrees
   * @param returnImmediately whether the method should run in parallel
   */
  public static void turnBy(double angle, boolean returnImmediately) {
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-convertAngle(angle), returnImmediately);
  }

  /**
   * Converts input distance to the total rotation of each wheel needed to cover that distance.
   * 
   * @param distance the input distance
   * @return the wheel rotations necessary to cover the distance
   */
  public static int convertDistance(double distance) {
    return (int) ((180.0 * distance) / (Math.PI * WHEEL_RAD));
  }
  /**
   * This method takes in the total distance needed to travel and transforms it
   * into the number of wheel rotations needed
   * 
   * @param distance
   * @return distance in wheel rotations.
   * 
   */
  public static int convertDistance(double radius, double distance) {
    return (int) ((180.0 * distance) / (Math.PI * radius));
  }
  /**
   * converts radians into degrees.
   * 
   * @param angle
   * @return wheel rotations needed
   * 
   */
  @SuppressWarnings("unused")
  public static int convertAngle(double radius, double width, double angle) {
    return convertDistance(radius, Math.PI * width * angle / 360.0);
  }
  /**
   * Converts input angle to the total rotation of each wheel needed to rotate the robot by that
   * angle.
   * 
   * @param angle the input angle
   * @return the wheel rotations necessary to rotate the robot by the angle
   */
  public static int convertAngle(double angle) {
    return convertDistance(Math.PI * BASE_WIDTH * angle / 360.0);
  }

  /**
   * Stops both motors.
   */
  public static void stopMotors() {
    leftMotor.stop();
    rightMotor.stop();
  }

  /**
   * Sets the speed of both motors to the same values.
   * 
   * @param speed the speed in degrees per second
   */
  public static void setSpeed(int speed) {
    setSpeeds(speed, speed);
  }

  /**
   * Rotates forward in a straight line for specified distance.
   * 
   * @param distance the distance
   * @param speed the speed in deg/s
   */
  public static void moveDistFwd(int distance, int speed) {
    // Motor commands block by default (i.e. they return only when motion is complete).
    // To get both motors synchronized, use the non-blocking method for leftMotor
    // so that it returns immediately. The blocking form is used for rightMotor so
    // that this method returns when motion is complete.

    int rotationAngle = distance * DIST_TO_DEG / 100; // Convert linear distance to turns
    leftMotor.setSpeed(speed); // Roll both motors forward
    rightMotor.setSpeed(speed);
    leftMotor.setAcceleration(ACCELERATION);
    rightMotor.setAcceleration(ACCELERATION);
    leftMotor.rotate(rotationAngle, true); // Rotate left motor - DO NOT BLOCK
    rightMotor.rotate(rotationAngle); // Rotate right motor
  }
  
  
  /**
   * Rotates forward in a straight line for specified distance.
   * 
   * @param distance the distance
   * @param returnImmediately whether the method should run in parallel
   */
  public static void moveDistFwd(double distance, boolean returnImmediately) {
    // Motor commands block by default (i.e. they return only when motion is complete).
    // To get both motors synchronized, use the non-blocking method for leftMotor
    // so that it returns immediately. The blocking form is used for rightMotor so
    // that this method returns when motion is complete.

    int rotationAngle = (int) (distance * DIST_TO_DEG / 100); // Convert linear distance to turns
    leftMotor.rotate(rotationAngle, true); // Rotate left motor - DO NOT BLOCK
    rightMotor.rotate(rotationAngle, returnImmediately); // Rotate right motor
  }
  
  /**
   * Sets the speed of both motors to different values.
   * 
   * @param leftSpeed the speed of the left motor in degrees per second
   * @param rightSpeed the speed of the right motor in degrees per second
   */
  public static void setSpeeds(int leftSpeed, int rightSpeed) {
    leftMotor.setSpeed(leftSpeed);
    rightMotor.setSpeed(rightSpeed);
  }

  /**
   * Sets the acceleration of both motors.
   * 
   * @param acceleration the acceleration in degrees per second squared
   */
  public static void setAcceleration(int acceleration) {
    leftMotor.setAcceleration(acceleration);
    rightMotor.setAcceleration(acceleration);
  }

}