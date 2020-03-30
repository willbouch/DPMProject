package ca.mcgill.ecse211.project;


import static ca.mcgill.ecse211.project.Resources.*;

/**
 * A class that handles the navigation done throughout the lab, including traveling
 */
public class Navigation implements Runnable {

  private double[][] map;
  private boolean isTunnelNav;
  private static double prevX = GameParameters.getInitial_x();
  private static double prevY = GameParameters.getInitial_y();

  public Navigation(double[][] map, boolean isTunnelNav) {
    this.map = map;
    this.isTunnelNav = isTunnelNav;
  }

  /**
   * Main navigation method - goes through and travels to each waypoint.
   */
  public void run() {	  
    for (int i = 0; i < map.length; i++) {
      travelTo(map[i][0]*TILE_SIZE, map[i][1]*TILE_SIZE);
    }
  }

  /**
   * Travel to coordinates.
   * @param x x position in cm
   * @param y y position in cm
   */	
  public void travelTo(double x, double y) {
    //We first compute the angle of the next waypoint relative to 0 degree up front
    double[] startLoc = odometer.getXyt();
    double angle = (180 / Math.PI) * Math.atan((x-startLoc[0])/(y-startLoc[1])); //Constant is deg/rad
    
    //Some adjustements to the angle
    if(y-startLoc[1] < 0){
      angle += 180;
    }
    
    if(angle < 0) {
      angle += 360;
    }
    //Once we have the angle, the robot turns its orientation towards the angle
    turnTo(angle);
    
    Main.sleepFor(500);
    
    if(isTunnelNav) {
      Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
      Driver.turnBy(90);
      leftMotor.forward();
      rightMotor.forward();
      USLocalization.waitLineDetection();
      Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
      Driver.moveStraightFor(.095);
            
      Driver.moveStraightFor(-1.0/2);
      
      Driver.turnBy(-94);
      
      Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
      Driver.moveStraightFor(-1.0/3);
      leftMotor.forward();
      rightMotor.forward();
      USLocalization.waitLineDetection();
      Driver.setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
      Driver.moveStraightFor(.095);
      
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

     odometer.setXyt(prevX, prevY, theta);
    }
    
    Main.sleepFor(500);
    
    //Once it is oriented towards the point, the robot navigates until it is there
    leftMotor.setSpeed(FORWARD_SPEED);
    rightMotor.setSpeed(FORWARD_SPEED);

    leftMotor.forward();
    rightMotor.forward();
    
    //The robot always computes its current distance from the waypoint and compares it to the lastdistance
    //at the previous iteration. When the current distance gets higher than the last, then we know the robot 
    //arrived at the way point.
    double lastDistance = Math.pow(x - odometer.getXyt()[0], 2) + Math.pow(y - odometer.getXyt()[1], 2);
    while(true) {
      double currentDistance = Math.pow(x - odometer.getXyt()[0], 2) + Math.pow(y - odometer.getXyt()[1], 2);
      if(currentDistance > lastDistance) {
        break;
      }   
      
      //Just a wait time to slow down the computations
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      lastDistance = currentDistance;
    }
    
    leftMotor.setSpeed(0);
    rightMotor.setSpeed(0);
    prevX = x;
    prevY = y;
  }

  /**
   * Turns the robot to a specified angle.
   */
  private static void turnTo(double angle) {
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
   
    //These conditions are all the cases that make the robot either turn right or left
    //Basically, they are meant to  make the robot turn with a minimal angle
    if(((angle - odometer.getTheta()) < 180 && (angle - odometer.getTheta()) > 0)
        || ((angle - odometer.getTheta() + 360) < 180 && (angle - odometer.getTheta() + 360) > 0)) {
      //We turn right
      leftMotor.forward();
      rightMotor.backward();
      while(odometer.getTheta() < angle || odometer.getTheta() > angle + 180);
    }
    else {
      //We turn left
      leftMotor.backward();
      rightMotor.forward();
      while(odometer.getTheta() > angle || odometer.getTheta() < angle - 180); 
    }
    leftMotor.setSpeed(0);
    rightMotor.setSpeed(0);
  }
}