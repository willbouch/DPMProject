package ca.mcgill.ecse211.project;


import static ca.mcgill.ecse211.project.Resources.*;

/**
 * A class that handles the navigation done throughout the competition. It takes care
 * of the travelling to different waypoints, adjusting the flow when the navigation crosses
 * a tunnel or when the robot is searching the island (using the 2 boolean attributes).
 */
public class Navigation implements Runnable {

  /**
   * The array of way points to travel to.
   */
  private double[][] map;
  
  /**
   * The boolean responsible to differentiate if the navigation is one that cross the tunnel.
   * This is to adjust the position before entering.
   */
  private boolean isTunnelNav;
  
  /**
   * The boolean responsible to differentiate if the navigation is on the island for searching
   */
  private boolean isIslandSearch;
  
  /**
   * The previous X position on the map (last waypoint visited)
   */
  private static double prevX = GameParameters.getInitial_x();
  
  /**
   * The previous Y position on the map (last waypoint visited)
   */
  private static double prevY = GameParameters.getInitial_y();

  /**
   * Constructor for navigation
   * 
   * @param map The way points to visit
   * @param isTunnelNav true if the Navigation is one that crosses a tunnel
   * @param isTunnelNav true if the Navigation is one for searching on the island
   */
  public Navigation(double[][] map, boolean isTunnelNav, boolean isIslandSearch) {
    this.map = map;
    this.isTunnelNav = isTunnelNav;
    this.isIslandSearch = isIslandSearch;
  }

  /**
   * Main navigation method - goes through and travels to each waypoint.
   */
  public void run() {	  
    for (int i = 0; i < map.length; i++) {
      travelTo(map[i][0]*TILE_SIZE, map[i][1]*TILE_SIZE);
      if(isIslandSearch) {
        LightLocalization.adjustPositionForSearching();
        boolean isCar = lookForCar();
        if(isCar) {
          //TODO
          //Make the robot go forward to get to the car
          Hook.findPin();
          Hook.deployHook();
          break;
        }
      }
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
    
    //Correction before entering the tunnel
    if(isTunnelNav) {
      LightLocalization.adjustPositionForTunnel();
      odometer.setX(prevX);
      odometer.setY(prevY);
    }
    
    //Avoid slips of wheels between rotation and going forward
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
      if(isIslandSearch) {
        //TODO
        //Poll the US sensor to check that the robot is not facing towards an obstacle
        //if it is, avoid the obstacle
        //otherwise, do nothing
      }
      
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
  private void turnTo(double angle) {
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
  
  
  
  /**
   * Completes a 360 rotation and samples the US sensor to see if anything is detected nearby
   * 
   * @return true if a car was detected, false otherwise
   */
  private boolean lookForCar() {
    //TODO
    //Turn 20 degrees, sample the sensor and check if the distance is under a certain threshold
    //like 50 cm.
    //If it is, using the x, y and theta of the odometer, determine if it is a tunnel or a wall
    //If it is, ignore and keep going
    //Otherwise, go and sample the colour sensor to see if the object is white.
    boolean isCar = false;
    
    return isCar;
  }
}