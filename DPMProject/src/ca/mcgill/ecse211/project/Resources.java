package ca.mcgill.ecse211.project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;;;

/**
 * This class is used to define static resources in one place for easy access and to avoid
 * cluttering the rest of the codebase. All resources can be imported at once like this:
 * 
 * <p>{@code import static ca.mcgill.ecse211.lab3.Resources.*;}
 */
public class Resources {
  
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
  public static final double BASE_WIDTH = 12.65;

  /**
   * The speed at which the robot moves forward in degrees per second.
   */
  public static final int FORWARD_SPEED = 200;

  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED = 50;

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
  public static final double LIGHT_CHANGE_THRESHOLD = 2.0;

  /**
   * sleep time of light sensor in milliseconds
   */
  public static final long LS_SLEEP = (long)((FORWARD_SPEED * WHEEL_RAD * Math.PI / 180) / LINE_WIDTH);

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
   * The light sensor for localization
   */
  public static final EV3ColorSensor lightSensor = new EV3ColorSensor(SensorPort.S2);

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
   * A sampleProvider for collecting light sensor data.
   */
  public static SampleProvider colorSample = lightSensor.getMode("Red");

  /**
   * The left motor.
   */
  public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);

  /**
   * The right motor.
   */
  public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);

  /**
   * The LCD.
   */
  public static final TextLCD lcd = LocalEV3.get().getTextLCD();
  
  /**
   * The odomter
   */
  public static final Odometer odometer = Odometer.getOdometer();
}