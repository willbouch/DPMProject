package ca.mcgill.ecse211.project;

/**
 * The class to find the pin and deploy the hook.
 */
public class Hook {
  /**
   * A boolean to keep track whether or not the car is towed.
   */
  private static boolean isCarTowed = false;
  
  /**
   * Find the pin and stop the execution of the function 
   * when it is found and hook ready to deploy.
   * 
   * The function takes no argument and returns void
   * since it is responsible for moving the robot (access
   * to motors and sensors from Resources). At the end of
   * this function, the robot will be ready to deploy the hook
   * and already in position to do so.
   * 
   * The goal is to make the robot go around the car either using
   * the dimensions of the car to estimate waypoints or by using
   * the Ultrasonic sensor using bangbang following (will need tests 
   * to determine the best solution). Once the pin is detected 
   * using the colour sensor, the execution stops and deployHook() is called.
   */
  public static void findPin() {
    //TODO
    //Go around the car and sample the colour sensor to check if the pin was detected.
    //If it is, then proceed with deployHook()
    //Otherwise, move slightly around the car and start over
  }
  
  /**
   * Deploy the hook to catch the stranded car.
   * 
   * The motor responsible for the hook has to be started (access
   * is in Resources). This is why the function takes no argument and
   * returns void.
   */
  public static void deployHook() {
    //TODO
    //Start the motor to deploy the hook
    isCarTowed = true;
  }
  
  /**
   * Retracts the towed vehicle.
   * 
   * The motor responsible for the hook has to be started (access
   * is in Resources). This is why the function takes no argument and
   * returns void.
   * 
   * This function will be used when localizing before entering the tunnel
   * on the way back.
   */
  public static void retractHook() {
    //TODO
    //Start the motor to retract the hook
  }
  
  public static boolean isCarTowed() {
    return isCarTowed;
  }
}
