package ca.mcgill.ecse211.project;

import java.math.BigDecimal;
import java.util.Map;
import ca.mcgill.ecse211.project.playingfield.Point;
import ca.mcgill.ecse211.project.playingfield.Region;
import ca.mcgill.ecse211.wificlient.WifiConnection;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

/**
 * This class is used to define static resources in one place for easy access and to avoid
 * cluttering the rest of the codebase. All resources can be imported at once like this:
 * 
 * <p>{@code import static ca.mcgill.ecse211.lab3.Resources.*;}
 */
public class Resources {
  /**
   * The default server IP used by the profs and TA's.
   */
  public static final String DEFAULT_SERVER_IP = "192.168.2.3";
  
  /**
   * The IP address of the server that transmits data to the robot. For the beta demo and
   * competition, replace this line with
   * 
   * <p>{@code public static final String SERVER_IP = DEFAULT_SERVER_IP;}
   */
  public static final String SERVER_IP = "192.168.2.3"; // = DEFAULT_SERVER_IP;
  
  /**
   * Your team number.
   */
  public static final int TEAM_NUMBER = 3;
  
  /** 
   * Enables printing of debug info from the WiFi class. 
   */
  public static final boolean ENABLE_DEBUG_WIFI_PRINT = true;
  
  /**
   * Enable this to attempt to receive Wi-Fi parameters at the start of the program.
   */
  public static final boolean RECEIVE_WIFI_PARAMS = true;
  
  /**
   * Max x position.
   */
  public static final int MAX_X = 15;
  
  /**
   * Max y position.
   */
  public static final int MAX_Y = 9;

  /**
   * The wheel radius in centimeters.
   */
  public static final double WHEEL_RAD = 2.11;

  /**
   * The robot width in centimeters.
   */
  public static final double BASE_WIDTH = 13.23;

  /**
   * The speed at which the robot moves forward in degrees per second.
   */
  public static final int FORWARD_SPEED = 150;

  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED = 100;

  /**
   * Sample size of the buffer used for filtering light sensor data
   */
  public static final int LS_SAMPLE_SIZE = 5;

  /**
   * Width of the line.
   */
  public static final double LINE_WIDTH = 0.5;

  /**
   * Change in light levels to decide line presence.
   */
  public static final double LIGHT_CHANGE_THRESHOLD = 1.7;

  /**
   * sleep time of light sensor in milliseconds
   */
  public static final long LS_SLEEP = (long)((ROTATE_SPEED * WHEEL_RAD * Math.PI / 180) / LINE_WIDTH);

  /**
   * sleep time of ultrasonic sensor in milliseconds
   */
  public static final long US_SLEEP = 100;

  /**
   * Timeout period in milliseconds.
   */
  public static final int TIMEOUT_PERIOD = 3000;

  /**
   * The tile size in centimeters. Note that 30.48 cm = 1 ft.
   */
  public static final double TILE_SIZE = 30.48;

  /**
   * Used for ultrasonic sensors, limit for invalid sample count in filters before high distance is used.
   */
  public static final int INVALID_SAMPLE_LIMIT = 20;

  /**
   * The ultrasonic sensor.
   */
  public static final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S1);

  /**
   * The left light sensor for localization
   */
  public static final EV3ColorSensor leftLightSensor = new EV3ColorSensor(SensorPort.S2);
  
  /**
   * The right light sensor for localization
   */
  public static final EV3ColorSensor rightLightSensor = new EV3ColorSensor(SensorPort.S3);
  
  /**
   * The colour sensor for obstacle detection
   */
  public static final EV3ColorSensor colourSensor = new EV3ColorSensor(SensorPort.S4);

  /**
   * The distance of the Light Sensor to the center of the robot in centimeters.
   */
  public static final double LS_DIST = 14;
  
  /** 
   * 360/(2xPixRw)  Rw=2.8cm. 
   */
  public static final int DIST_TO_DEG = 21;
  
  /**
   * The motor acceleration in degrees per second squared.
   */
  public static final int ACCELERATION = 3000;
  
  /**
   * The distance between light sensors and center of rotation in feet.
   */
  public static final double SENSOR_TO_CENTER = 0.095;

  /**
   * A sampleProvider for collecting left light sensor data.
   */
  public static SampleProvider leftColorSample = leftLightSensor.getMode("Red");
  
  /**
   * A sampleProvider for collecting right light sensor data.
   */
  public static SampleProvider rightColorSample = rightLightSensor.getMode("Red");

  /**
   * The left motor.
   */
  public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.C);

  /**
   * The right motor.
   */
  public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);
  
  /**
   * The hook motor.
   */
  public static final EV3LargeRegulatedMotor hookMotor = new EV3LargeRegulatedMotor(MotorPort.B);

  /**
   * The LCD.
   */
  public static final TextLCD lcd = LocalEV3.get().getTextLCD();
  
  /**
   * The odometer
   */
  public static final Odometer odometer = Odometer.getOdometer();
  
  /**
   * Container for the Wi-Fi parameters.
   */
  public static Map<String, Object> wifiParameters;
  
  // This static initializer MUST be declared before any Wi-Fi parameters.
  static {
    receiveWifiParameters();
  }
  
  /** Red team number. */
  public static int redTeam = getWP("RedTeam");

  /** Red team's starting corner. */
  public static int redCorner = getWP("RedCorner");

  /** Green team number. */
  public static int greenTeam = getWP("GreenTeam");

  /** Green team's starting corner. */
  public static int greenCorner = getWP("GreenCorner");

  /** The Red Zone. */
  public static Region red = makeRegion("Red");

  /** The Green Zone. */
  public static Region green = makeRegion("Green");

  /** The Island. */
  public static Region island = makeRegion("Island");

  /** The red tunnel footprint. */
  public static Region tnr = makeRegion("TNR");

  /** The green tunnel footprint. */
  public static Region tng = makeRegion("TNG");

  /** The red search zone. */
  public static Region szr = makeRegion("SZR");

  /** The green search zone. */
  public static Region szg = makeRegion("SZG");
  
  /**
   * Receives Wi-Fi parameters from the server program.
   */
  public static void receiveWifiParameters() {
    // Only initialize the parameters if needed
    if (!RECEIVE_WIFI_PARAMS || wifiParameters != null) {
      return;
    }
    System.out.println("Waiting to receive Wi-Fi parameters.");

    // Connect to server and get the data, catching any errors that might occur
    try (WifiConnection conn =
        new WifiConnection(SERVER_IP, TEAM_NUMBER, ENABLE_DEBUG_WIFI_PRINT)) {
      /*
       * getData() will connect to the server and wait until the user/TA presses the "Start" button
       * in the GUI on their laptop with the data filled in. Once it's waiting, you can kill it by
       * pressing the back/escape button on the EV3. getData() will throw exceptions if something
       * goes wrong.
       */
      wifiParameters = conn.getData();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  /**
   * Returns the Wi-Fi parameter int value associated with the given key.
   * 
   * @param key the Wi-Fi parameter key
   * @return the Wi-Fi parameter int value associated with the given key
   */
  public static int getWP(String key) {
    if (wifiParameters != null) {
      return ((BigDecimal) wifiParameters.get(key)).intValue();
    } else {
      return 0;
    }
  }
  
  /** 
   * Makes a point given a Wi-Fi parameter prefix.
   */
  public static Point makePoint(String paramPrefix) {
    return new Point(getWP(paramPrefix + "_x"), getWP(paramPrefix + "_y"));
  }
  
  /**
   * Makes a region given a Wi-Fi parameter prefix.
   */
  public static Region makeRegion(String paramPrefix) {
    return new Region(makePoint(paramPrefix + "_LL"), makePoint(paramPrefix + "_UR"));
  }
}