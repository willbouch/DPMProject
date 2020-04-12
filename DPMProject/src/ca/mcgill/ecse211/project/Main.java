package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

import lejos.hardware.Button;
import lejos.hardware.Sound;

/**
 * The main driver class for the project. Takes care of every step of the software flow.
 */
public class Main {
  /**
   * The main entry point.
   * The main manages the threads and the execution of every step of the software flow.
   * 
   * @param args not used
   */
  public static void main(String[] args) {
    //We start by getting the game parameters. Those will be accessed directly in Main as static variables
    setGameParameters();

    //We first by waiting for a button press. This is the start of the program
    Button.waitForAnyPress();

    //Once this is done, we start the localization using the ultrasonic sensor
    //So, we start odometer and USLocalization
    Thread odometerThread = new Thread(odometer);
    odometerThread.start();

    USLocalization localize = new USLocalization();
    Thread localThread = new Thread(localize);
    localThread.start();
    
    //We wait for the localization to be done
    try {localThread.join();} catch (InterruptedException e) {}
    
    //We beep 3 times to signify the localization is done
    Sound.beep();
    Sound.beep();
    Sound.beep();
    
    //We start the nav to get to the entrance of the tunnel
    double[] tunnelEntranceAndExitPosition = GameParameters.getTunnelEntranceAndExitPosition();
    double[][] mapToTunnelGo = {
        {7, 7}, 
    };
    Navigation navigation = new Navigation(mapToTunnelGo, false, false);
    Thread navigationThread = new Thread(navigation);
    navigationThread.start();
    //We wait for the navigation to the search island to be done
    try {navigationThread.join();} catch (InterruptedException e) {}

    //We can now start the navigation to the other side of tunnel
    double[][] mapTunnelThrough = {
        {tunnelEntranceAndExitPosition[2], tunnelEntranceAndExitPosition[3]}
    };    
    navigation = new Navigation(mapTunnelThrough, true, false);
    navigationThread = new Thread(navigation);
    navigationThread.start();
    //We wait for the navigation to the search island to be done
    try {navigationThread.join();} catch (InterruptedException e) {}
    //We beep 3 times to signify we are on the search island
    Sound.beep();
    Sound.beep();
    Sound.beep();

    //We first get 4 points that are well spread out on the island
    int delta = (int)Math.ceil((GameParameters.getIsland_UR_x() - GameParameters.getIsland_LL_x()) / 4.0);
    double x1 = GameParameters.getIsland_LL_x() + delta;
    double x2 = GameParameters.getIsland_UR_x() - delta;
    delta = (int)Math.ceil((GameParameters.getIsland_UR_y() - GameParameters.getIsland_LL_y()) / 4.0);
    double y1 = GameParameters.getIsland_LL_y() + delta;
    double y2 = GameParameters.getIsland_UR_y() - delta;
    double[] firstPoint = {x1, y1};
    double[] secondPoint = {x2, y1};
    double[] thirdPoint = {x1, y2};
    double[] fourthPoint = {x2, y2};
    double[][] mapForSearch = {
        firstPoint, secondPoint, thirdPoint, fourthPoint
    };
    //We create the navigation with this list of 4 points
    navigation = new Navigation(mapForSearch, false, true);
    navigationThread = new Thread(navigation);
    navigationThread.start();
    //We wait for the searching to be done
    try {navigationThread.join();} catch (InterruptedException e) {}
    
    //At this point, we hopefully have hooked the car
    //Once we have the vehicle, we go back to tunnel exit
    double[][] mapToTunnelBack = {
        {tunnelEntranceAndExitPosition[2], tunnelEntranceAndExitPosition[3]}, 
    };
    navigation = new Navigation(mapToTunnelBack, false, false);
    navigationThread = new Thread(navigation);
    navigationThread.start();
    //We wait for the navigation back to our island to be done
    try {navigationThread.join();} catch (InterruptedException e) {}
    
    double[][] mapTunnelThroughBack = {
        {tunnelEntranceAndExitPosition[0], tunnelEntranceAndExitPosition[1]}
    };
    navigation = new Navigation(mapTunnelThroughBack, true, false);
    navigationThread = new Thread(navigation);
    navigationThread.start();
    //We wait for the navigation back to our island to be done
    try {navigationThread.join();} catch (InterruptedException e) {}

    //We can now start the navigation to go back to return position
    double[] returnPosition = GameParameters.getReturnPosition();
    double[][] mapToInitialLocation = {
        {returnPosition[0], returnPosition[1]},
    };
    navigation = new Navigation(mapToInitialLocation, false, false);
    navigationThread = new Thread(navigation);
    navigationThread.start();
    //We wait for the navigation back to our initial position to be done
    try {navigationThread.join();} catch (InterruptedException e) {}
    //We beep 5 times to signify the end
    Sound.beep();
    Sound.beep();
    Sound.beep();
    Sound.beep();
    Sound.beep();
    
    System.exit(0);
  }

  /**
   * A method to fetch the game parameters from the WIFI and set the static
   * params in GameParameters class.
   */
  public static void setGameParameters() {
    if(redTeam == TEAM_NUMBER) {
      GameParameters.setTN_LL_x(tnr.ll.x);
      GameParameters.setTN_LL_y(tnr.ll.y);
      GameParameters.setTN_UR_x(tnr.ur.x);
      GameParameters.setTN_UR_y(tnr.ur.y);
      
      GameParameters.setIsland_LL_x(szr.ll.x);
      GameParameters.setIsland_LL_y(szr.ll.y);
      GameParameters.setIsland_UR_x(szr.ur.x);
      GameParameters.setIsland_UR_y(szr.ur.y);
      
      GameParameters.setOrigin_LL_x(red.ll.x);
      GameParameters.setOrigin_LL_y(red.ll.y);
      GameParameters.setOrigin_UR_x(red.ur.x);
      GameParameters.setOrigin_UR_y(red.ur.y);
    }
    else {
      GameParameters.setTN_LL_x(tng.ll.x);
      GameParameters.setTN_LL_y(tng.ll.y);
      GameParameters.setTN_UR_x(tng.ur.x);
      GameParameters.setTN_UR_y(tng.ur.y);
      
      GameParameters.setIsland_LL_x(szg.ll.x);
      GameParameters.setIsland_LL_y(szg.ll.y);
      GameParameters.setIsland_UR_x(szg.ur.x);
      GameParameters.setIsland_UR_y(szg.ur.y);
      
      GameParameters.setOrigin_LL_x(green.ll.x);
      GameParameters.setOrigin_LL_y(green.ll.y);
      GameParameters.setOrigin_UR_x(green.ur.x);
      GameParameters.setOrigin_UR_y(green.ur.y);
    }
    
    GameParameters.setUp();

  }

  /**
   * A method to sleep a thread for a given duration
   * @param duration - time to sleep (in miliseconds)
   */
  public static void sleepFor(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      // There is nothing to be done here
    }
  }
}