package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.ROTATE_SPEED;
import static ca.mcgill.ecse211.project.Resources.SENSOR_TO_CENTER;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.odometer;
import static ca.mcgill.ecse211.project.Resources.rightMotor;

/**
 * A class that implements the adjustements in position using the two light sensors on the side.
 * The adjustements take no argument and always return void since the methods take care
 * of moving the robot and correcting the odometer's values.
 */
public class LightLocalization {

  /**
   * Adjusts the position before entering the tunnel. There is no need to pass
   * parameters since this method moves the robot and adjusts the robot's position
   * using the closest horizontal and vertical line to correct the position on both
   * axis. If the car is towed to the back of the robot, then the robot releases it
   * and then performs the localization. It deploys the hook again afterwards.
   */
  public static void adjustPositionForTunnel() {
    //If the car is towed, we release before localization
    boolean needToRecuperateCar = false;
    if(Hook.isCarTowed()) {
      Hook.retractHook();
      needToRecuperateCar = true;
    }
    
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    Driver.turnBy(90);
    leftMotor.forward();
    rightMotor.forward();
    USLocalization.waitLineDetection();
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    Driver.moveStraightFor(SENSOR_TO_CENTER);

    //We go back half a tile to position in the middle
    Driver.moveStraightFor(-1.0/2);

    Driver.turnBy(-90);

    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    Driver.moveStraightFor(-1.0/3); //To adjust position horizontally also using the line we are on
    leftMotor.forward();
    rightMotor.forward();
    USLocalization.waitLineDetection();
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    Driver.moveStraightFor(SENSOR_TO_CENTER);

    //We adjust the angle of the odometer since we are facing right in front
    double t = odometer.getXyt()[2];
    double theta = 0;
    if(t > 46 && t < 134) {
      theta = 90;
    }
    else if(t > 136 && t < 224) {
      theta = 180;
    }
    else if(t > 226 && t < 314) {
      theta = 270;
    }
    else {
      theta = 0;
    }
    odometer.setTheta(theta);
    
    //If the car was released, we need to hook it back.
    if(needToRecuperateCar) {
      Hook.deployHook();
    }
  }

  /**
   * Adjusts the position at each waypoint when searching on the island. There is no need to pass
   * parameters since this method moves the robot and adjusts the robot's position
   * using the closest point on the grid.
   */
  public static void adjustPositionForSearching() {
    //TODO
    //Idea developed in the software documentation
  }
}
