package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

/**
 * The class to access static game parameters.
 */
public class GameParameters {
  /**
   * Origin lower left x position
   */
  private static int origin_LL_x;

  /**
   * Origin lower left y position
   */
  private static int origin_LL_y;

  /**
   * Origin upper right x position
   */
  private static int origin_UR_x;

  /**
   * Origin upper right y position
   */
  private static int origin_UR_y;


  /**
   * Island lower left x position
   */
  private static int Island_LL_x;

  /**
   * Island lower left y position
   */
  private static int Island_LL_y;

  /**
   * Island upper right x position
   */
  private static int Island_UR_x;

  /**
   * Island upper right y position
   */
  private static int Island_UR_y;


  /**
   * Tunnel lower left x position
   */
  private static int TN_LL_x;

  /**
   * Tunnel lower left y position
   */
  private static int TN_LL_y;

  /**
   * Tunnel upper right x position
   */
  private static int TN_UR_x;

  /**
   * Tunnel upper right y position
   */
  private static int TN_UR_y;


  /**
   * Initial corner x position
   */
  private static int Corner_x;

  /**
   * Initial corner y position
   */
  private static int Corner_y;
  
  
  /**
   * Initial x position
   */
  private static int Initial_x;

  /**
   * Initial y position
   */
  private static int Initial_y;
  
  
  /**
   * Tunnel entry x position
   */
  private static double TN_ENTRY_x;

  /**
   * Tunnel entry y position
   */
  private static double TN_ENTRY_y;
  
  /**
   * Direction to go to get to island
   */
  private static String direction;
  

  /**
   * This method set up the important parameters
   */
  public static void setUp() {
    //We first check if we are going left, right, bottom or up to reach island
    //Set up the corner
    if(origin_UR_x == MAX_X && origin_UR_y == MAX_Y) {
      Corner_x = MAX_X;
      Corner_y = MAX_Y;
      Initial_x = Corner_x - 1;
      Initial_y = Corner_y - 1;
    }
    else if(origin_LL_x == 0 && origin_LL_y == 0) {
      Corner_x = 0;
      Corner_y = 0;
      Initial_x = Corner_x + 1;
      Initial_y = Corner_y + 1;
    }
    else if(origin_UR_x == MAX_X && origin_LL_y == 0) {
      Corner_x = MAX_X;
      Corner_y = 0;
      Initial_x = Corner_x - 1;
      Initial_y = Corner_y + 1;
    }
    else if(origin_LL_x == 0 && origin_UR_y == MAX_Y) {
      Corner_x = 0;
      Corner_y = MAX_Y;
      Initial_x = Corner_x + 1;
      Initial_y = Corner_y - 1;
    }

    //We then select the direction we are going
    if(Island_LL_y > Corner_y) {
      direction = "UP";
    }
    else if(Corner_y > Island_UR_y) {
      direction = "DOWN";
    }
    else if(Island_LL_x > Corner_x) {
      direction = "RIGHT";
    }
    else if(Corner_x > Island_UR_x) {
      direction = "LEFT";
    }
    
    //We then setup the entry and exit position of the tunnel
    if(direction.equals("UP")) {
      TN_ENTRY_x = TN_LL_x + 0.5;
      TN_ENTRY_y = TN_LL_y;
    }
    else if(direction.equals("DOWN")) {
      TN_ENTRY_x = TN_LL_x + 0.5;
      TN_ENTRY_y = TN_UR_y;
    }
    else if(direction.equals("LEFT")) {
      TN_ENTRY_x = TN_UR_x;
      TN_ENTRY_y = TN_UR_y - 0.5;
    }
    else if(direction.equals("RIGHT")) {
      TN_ENTRY_x = TN_LL_x;
      TN_ENTRY_y = TN_UR_y - 0.5;
    }
  }

  /**
   * This method returns the position the robot should face to enter the tunnel
   * @return position Position of the tunnel entrance
   */
  public static double[] getTunnelEntranceAndExitPosition() {
    double[] position = new double[4];

    if(direction.equals("UP")) {
      position[0] = TN_ENTRY_x;
      position[1] = TN_ENTRY_y - 1;
      position[2] = position[0];
      position[3] = position[1] + 4;
    }
    else if(direction.equals("DOWN")) {
      position[0] = TN_ENTRY_x;
      position[1] = TN_ENTRY_y + 1;
      position[2] = position[0];
      position[3] = position[1] - 4;
    }
    else if(direction.equals("LEFT")) {
      position[0] = TN_ENTRY_x + 1;
      position[1] = TN_ENTRY_y;
      position[2] = position[0] - 4;
      position[3] = position[1];
    }
    else if(direction.equals("RIGHT")) {
      position[0] = TN_ENTRY_x - 1;
      position[1] = TN_ENTRY_y;
      position[2] = position[0] + 4;
      position[3] = position[1];
    }

    return position;
  }
  
  /**
   * This method returns the position to which the robot should end its course
   * @return position Position where to end the course
   */
  public static int[] getReturnPosition() {
    int[] position = new int[2];
    position[0] = Initial_x;
    position[1] = Initial_y;
    return position;
  }

}
