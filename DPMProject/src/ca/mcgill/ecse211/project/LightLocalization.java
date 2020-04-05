package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.ROTATE_SPEED;
import static ca.mcgill.ecse211.project.Resources.SENSOR_TO_CENTER;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.odometer;
import static ca.mcgill.ecse211.project.Resources.rightMotor;

/**
 * A class that implements the adjustements in position using the two light sensors on the side.
 */
public class LightLocalization {

  /**
   * Adjusts the position before entering the tunnel
   */
  public static void adjustPositionForTunnel() {
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    Driver.turnBy(90);
    leftMotor.forward();
    rightMotor.forward();
    USLocalization.waitLineDetection();
    Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    Driver.moveStraightFor(SENSOR_TO_CENTER);

    //We go back half a tile to positiuon in the middle
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
    double t = odometer.getTheta();
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
  }

  /**
   * Adjusts the position at each waypoint when searching on the island
   */
  public static void adjustPositionForSearching() {
    //TODO
  }
}
