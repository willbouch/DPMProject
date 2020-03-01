package ca.mcgill.ecse211.project;


import static ca.mcgill.ecse211.project.Resources.*;

/**
 * A class that handles the navigation done throughout the lab, including traveling
 */
public class Navigation implements Runnable {
    //variables used in code
	private double currentX;
	private double currentY;
	private double currentTheta;
	private double deltaX;
	private double deltaY;
	
	private double[][] map;
	
	public Navigation(double[][] map) {
	  this.map = map;
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
		currentX = odometer.getXyt()[0];
		currentY = odometer.getXyt()[1];
		
		//difference in x and y direction
		deltaX = x - currentX;
		deltaY = y - currentY;

		// Calc angle to turn
		currentTheta = odometer.getXyt()[2];
		double calcTheta = Math.atan2(deltaX, deltaY) - currentTheta;

		turnTo(calcTheta);
		
		//At this point, we are facing towards the next waypoint
	    double lastDistance = Math.pow(x - odometer.getXyt()[0], 2) + Math.pow(y - odometer.getXyt()[1], 2);
		while(true) {
		  Driver.setSpeed(FORWARD_SPEED);
	      leftMotor.forward();
	      rightMotor.forward();
	      
		  //Check if we reached the destination
	      double currentDistance = Math.pow(x - odometer.getXyt()[0], 2) + Math.pow(y - odometer.getXyt()[1], 2);
	      if(currentDistance > lastDistance) {
	    	Driver.setSpeed(0);
	        break;
	      }
		   
		  lastDistance = currentDistance;
		  //Slow down computations
		  Main.sleepFor(US_SLEEP);
		}

		//We have now reached the waypoint

		Driver.setSpeed(0);
	}
	
	/**
	 * Turns the robot by a set theta in radians.
	 * @param theta in radians
	 */
	public void turnTo(double theta) {
		// Ensures we are turning the minimum angle
		if (theta > Math.PI) {
			theta -= 2 * Math.PI;
		} else if (theta < -Math.PI) {
			theta += 2 * Math.PI;
		}
		leftMotor.setSpeed(ROTATE_SPEED);
		rightMotor.setSpeed(ROTATE_SPEED);
		// Left turn for negative angle or right turn is the angle is positibe
		if (theta < 0) {
			leftMotor.rotate(-Driver.convertAngle(WHEEL_RAD, BASE_WIDTH, -(theta * 180) / Math.PI), true);
			rightMotor.rotate(Driver.convertAngle(WHEEL_RAD, BASE_WIDTH, -(theta * 180) / Math.PI), false);
		} else {
			leftMotor.rotate(Driver.convertAngle(WHEEL_RAD, BASE_WIDTH, (theta * 180) / Math.PI), true);
			rightMotor.rotate(-Driver.convertAngle(WHEEL_RAD, BASE_WIDTH, (theta * 180) / Math.PI), false);
		}
	}
}