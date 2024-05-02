package owen;
import robocode.*;
import robocode.util.Utils;
//import java.awt.Color;

// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

/**
 * OwenBot1 - a robot by (your name here)
 */
public class OwenBot1 extends Robot
{
	private boolean orbitClockwise = true;
	double firePower = 1;
	/**
	 * run: OwenBot1's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		turnRight(360);
		while(true){
			orbit();
			
			
			turnGunRight(360);
		}
		
		
	}
	

	private void orbit() {
        // Orbit around an opponent
        if (orbitClockwise) {
            turnRight(5); // Adjust the turn angle for orbiting
            ahead(100); // Adjust the distance for orbiting
        } else {
            turnLeft(10); // Adjust the turn angle for orbiting
            ahead(100); // Adjust the distance for orbiting
        }
    }
	
	private void randomMovement() {
        // Move randomly
        double randomDirection = Math.random() > 0.5 ? 1 : -1;
        ahead(100 * randomDirection); // Adjust the distance for random movement
        turnRight(90 * randomDirection); // Adjust the turn angle for random movement
    }
	

	private double firePowerAdjuster(double enemyDistance) {
		//use higher power for closer enemies vice versa
		if(enemyDistance < 200) {
			return 3;
		} else if (enemyDistance < 400) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
	

		//divides enemy's distance by velocity of bullet to calculate time
		double enemyDistance = e.getDistance();
	


		//adjusts firepower
		double firePower = firePowerAdjuster(enemyDistance);

		double bulletTime = enemyDistance / (20 - (3 * firePower));
		System.out.println("Bullet Travel Time: " + bulletTime);
	

		double enemyBearing = Math.toRadians(getHeading() + e.getBearing());
		double enemyX = getX() + enemyDistance * Math.sin(enemyBearing);
		double enemyY = getY() + enemyDistance * Math.cos(enemyBearing);
		double enemyHeading = e.getHeadingRadians();
		double enemyVelocity = e.getVelocity();
		System.out.print("enemy velocity: " + enemyVelocity);
		//sin(enemyHeading) = x component of where enemy is heading (amount of movement in the x direction)
		//cos(enemyHeading) = y component of where enemy is heading
		//multiplying by velocity gives the actual movement of how far they moved
		//multiplying by bulletTine gives vector that is the predicted position of robot based on the bulletTime
		double futureX = enemyX + Math.sin(enemyHeading) * enemyVelocity * bulletTime;
		double futureY = enemyY + Math.cos(enemyHeading) * enemyVelocity * bulletTime;

		//gets angle between x-axis and the line between your robot and enemy
		//gives difference in angles so your gun can turn the correct amount
		double gunTurnAngle = Utils.normalRelativeAngle(Math.atan2(futureX - getX(), futureY - getY()) - Math.toRadians(getGunHeading()));

		turnGunRight(Math.toDegrees(gunTurnAngle));
		fire(firePower);	
		
		

		if(!(e.getBearing() >= 45 && e.getBearing() <= 135) || !(e.getBearing() >= 225 && e.getBearing()  <= 315)){
			if(e.getBearing() < 180){
				setAdjustRadarForRobotTurn(false);
				turnRight(e.getBearing() - 90);
				setAdjustRadarForRobotTurn(true);
			} else {
				setAdjustRadarForRobotTurn(false);
				turnLeft(360 - e.getBearing() - 90);
				setAdjustRadarForRobotTurn(true);
			}
			
		}
		
		//double enemyDistance = e.getDistance();
		//double enemyVelocitiy = e.getVelocity();
		
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		ahead(50);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
			
		back(200);
	}	
}
