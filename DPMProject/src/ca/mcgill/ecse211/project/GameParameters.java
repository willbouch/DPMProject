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

  /**
   * @return the initial_x
   */
  public static int getInitial_x() {
    return Initial_x;
  }

  /**
   * @return the initial_y
   */
  public static int getInitial_y() {
    return Initial_y;
  }

  /**
   * @return the origin_LL_x
   */
  public static int getOrigin_LL_x() {
    return origin_LL_x;
  }

  /**
   * @param origin_LL_x the origin_LL_x to set
   */
  public static void setOrigin_LL_x(int origin_LL_x) {
    GameParameters.origin_LL_x = origin_LL_x;
  }

  /**
   * @return the origin_LL_y
   */
  public static int getOrigin_LL_y() {
    return origin_LL_y;
  }

  /**
   * @param origin_LL_y the origin_LL_y to set
   */
  public static void setOrigin_LL_y(int origin_LL_y) {
    GameParameters.origin_LL_y = origin_LL_y;
  }

  /**
   * @return the origin_UR_x
   */
  public static int getOrigin_UR_x() {
    return origin_UR_x;
  }

  /**
   * @param origin_UR_x the origin_UR_x to set
   */
  public static void setOrigin_UR_x(int origin_UR_x) {
    GameParameters.origin_UR_x = origin_UR_x;
  }

  /**
   * @return the origin_UR_y
   */
  public static int getOrigin_UR_y() {
    return origin_UR_y;
  }

  /**
   * @param origin_UR_y the origin_UR_y to set
   */
  public static void setOrigin_UR_y(int origin_UR_y) {
    GameParameters.origin_UR_y = origin_UR_y;
  }

  /**
   * @return the island_LL_x
   */
  public static int getIsland_LL_x() {
    return Island_LL_x;
  }

  /**
   * @param island_LL_x the island_LL_x to set
   */
  public static void setIsland_LL_x(int island_LL_x) {
    Island_LL_x = island_LL_x;
  }

  /**
   * @return the island_LL_y
   */
  public static int getIsland_LL_y() {
    return Island_LL_y;
  }

  /**
   * @param island_LL_y the island_LL_y to set
   */
  public static void setIsland_LL_y(int island_LL_y) {
    Island_LL_y = island_LL_y;
  }

  /**
   * @return the island_UR_x
   */
  public static int getIsland_UR_x() {
    return Island_UR_x;
  }

  /**
   * @param island_UR_x the island_UR_x to set
   */
  public static void setIsland_UR_x(int island_UR_x) {
    Island_UR_x = island_UR_x;
  }

  /**
   * @return the island_UR_y
   */
  public static int getIsland_UR_y() {
    return Island_UR_y;
  }

  /**
   * @param island_UR_y the island_UR_y to set
   */
  public static void setIsland_UR_y(int island_UR_y) {
    Island_UR_y = island_UR_y;
  }

  /**
   * @return the tN_LL_x
   */
  public static int getTN_LL_x() {
    return TN_LL_x;
  }

  /**
   * @param tN_LL_x the tN_LL_x to set
   */
  public static void setTN_LL_x(int tN_LL_x) {
    TN_LL_x = tN_LL_x;
  }

  /**
   * @return the tN_LL_y
   */
  public static int getTN_LL_y() {
    return TN_LL_y;
  }

  /**
   * @param tN_LL_y the tN_LL_y to set
   */
  public static void setTN_LL_y(int tN_LL_y) {
    TN_LL_y = tN_LL_y;
  }

  /**
   * @return the tN_UR_x
   */
  public static int getTN_UR_x() {
    return TN_UR_x;
  }

  /**
   * @param tN_UR_x the tN_UR_x to set
   */
  public static void setTN_UR_x(int tN_UR_x) {
    TN_UR_x = tN_UR_x;
  }

  /**
   * @return the tN_UR_y
   */
  public static int getTN_UR_y() {
    return TN_UR_y;
  }

  /**
   * @param tN_UR_y the tN_UR_y to set
   */
  public static void setTN_UR_y(int tN_UR_y) {
    TN_UR_y = tN_UR_y;
  }

  /**
   * @return the corner_x
   */
  public static int getCorner_x() {
    return Corner_x;
  }

  /**
   * @param corner_x the corner_x to set
   */
  public static void setCorner_x(int corner_x) {
    Corner_x = corner_x;
  }

  /**
   * @return the corner_y
   */
  public static int getCorner_y() {
    return Corner_y;
  }

  /**
   * @param corner_y the corner_y to set
   */
  public static void setCorner_y(int corner_y) {
    Corner_y = corner_y;
  }

  /**
   * @return the tN_ENTRY_x
   */
  public static double getTN_ENTRY_x() {
    return TN_ENTRY_x;
  }

  /**
   * @param tN_ENTRY_x the tN_ENTRY_x to set
   */
  public static void setTN_ENTRY_x(double tN_ENTRY_x) {
    TN_ENTRY_x = tN_ENTRY_x;
  }

  /**
   * @return the tN_ENTRY_y
   */
  public static double getTN_ENTRY_y() {
    return TN_ENTRY_y;
  }

  /**
   * @param tN_ENTRY_y the tN_ENTRY_y to set
   */
  public static void setTN_ENTRY_y(double tN_ENTRY_y) {
    TN_ENTRY_y = tN_ENTRY_y;
  }

  /**
   * @return the direction
   */
  public static String getDirection() {
    return direction;
  }

  /**
   * @param direction the direction to set
   */
  public static void setDirection(String direction) {
    GameParameters.direction = direction;
  }

  /**
   * @param initial_x the initial_x to set
   */
  public static void setInitial_x(int initial_x) {
    Initial_x = initial_x;
  }

  /**
   * @param initial_y the initial_y to set
   */
  public static void setInitial_y(int initial_y) {
    Initial_y = initial_y;
  }
  
  

}
