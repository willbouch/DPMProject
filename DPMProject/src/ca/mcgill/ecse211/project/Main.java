package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.*;

import lejos.hardware.Button;
import lejos.hardware.Sound;

/**
 * The main driver class for the project.
 */
public class Main {
  /**
   * The main entry point.
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

    //Adjust position and orientation with light localization
    //TODO
    //We beep 3 times to signify the localization is done
    Sound.beep();
    Sound.beep();
    Sound.beep();

    //We can now start the navigation to the other side of tunnel
    double[] tunnelEntranceAndExitPosition = GameParameters.getTunnelEntranceAndExitPosition();
    double[][] mapToTunnelGo = {
        {tunnelEntranceAndExitPosition[0], tunnelEntranceAndExitPosition[2]}, 
        {tunnelEntranceAndExitPosition[1], tunnelEntranceAndExitPosition[3]}
    };
    Navigation navigation = new Navigation(mapToTunnelGo);
    Thread navigationThread = new Thread(navigation);
    navigationThread.start();
    //We wait for the navigation to the search island to be done
    try {navigationThread.join();} catch (InterruptedException e) {}
    //We beep 3 times to signify we are on the search island
    Sound.beep();
    Sound.beep();
    Sound.beep();

    //TODO implement the search

    //Once we have the vehicle, we go back to tunnel exit and go through
    double[][] mapToTunnelBack = {
        {tunnelEntranceAndExitPosition[2], tunnelEntranceAndExitPosition[0]}, 
        {tunnelEntranceAndExitPosition[3], tunnelEntranceAndExitPosition[1]}
    };
    navigation = new Navigation(mapToTunnelBack);
    navigationThread = new Thread(navigation);
    navigationThread.start();
    //We wait for the navigation back to our island to be done
    try {navigationThread.join();} catch (InterruptedException e) {}

    //We can now start the navigation to go back to return position
    int[] returnPosition = GameParameters.getReturnPosition();
    double[][] mapToInitialLocation = {
        {returnPosition[0]},
        {returnPosition[1]}
    };
    navigation = new Navigation(mapToInitialLocation);
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
   * params in GameParameters class
   */
  public static void setGameParameters() {
    //TODO
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