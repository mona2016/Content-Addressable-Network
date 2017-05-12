package com.can.dao;

import com.can.dao.Point;

public class Zone {

	public Point lowerLeft, lowerRight, upperRight, upperLeft;

	
	public Zone(){
		lowerLeft = new Point(0.0, 0.0);
		lowerRight = new Point(10.0, 0.0);
		upperLeft = new Point(0.0, 10.0);
		upperRight = new Point(10.0, 10.0);
	}

	public Zone(Point lowerLeft, Point lowerRight, Point upperLeft, Point upperRight) {
		super();
		this.lowerLeft = lowerLeft;
		this.lowerRight = lowerRight;
		this.upperRight = upperRight;
		this.upperLeft = upperLeft;
	}
	
	

	/**
	 * @return the lowerLeft
	 */
	public Point getLowerLeft() {
		return lowerLeft;
	}

	/**
	 * @param lowerLeft the lowerLeft to set
	 */
	public void setLowerLeft(Point lowerLeft) {
		this.lowerLeft = lowerLeft;
	}

	/**
	 * @return the lowerRight
	 */
	public Point getLowerRight() {
		return lowerRight;
	}

	/**
	 * @param lowerRight the lowerRight to set
	 */
	public void setLowerRight(Point lowerRight) {
		this.lowerRight = lowerRight;
	}

	/**
	 * @return the upperRight
	 */
	public Point getUpperRight() {
		return upperRight;
	}

	/**
	 * @param upperRight the upperRight to set
	 */
	public void setUpperRight(Point upperRight) {
		this.upperRight = upperRight;
	}

	/**
	 * @return the upperLeft
	 */
	public Point getUpperLeft() {
		return upperLeft;
	}

	/**
	 * @param upperLeft the upperLeft to set
	 */
	public void setUpperLeft(Point upperLeft) {
		this.upperLeft = upperLeft;
	}

	@Override
	public String toString() {
		return "Zone [lowerLeft=" + lowerLeft + ", lowerRight=" + lowerRight + ", upperLeft=" + upperLeft + ", upperRight=" + upperRight
				+ "]";
	}
	
	
	
	
}
/*

public static Zone splitZone(Zone myZone) {
	
	Zone newZone=null;
	
	if (((myZone.lowerRight.x - myZone.lowerLeft.x) == (myZone.upperLeft.y - myZone.lowerLeft.y)) || ((myZone.lowerRight.x - myZone.lowerLeft.x) > (myZone.upperLeft.y - myZone.lowerLeft.y))) {
		double newX = (myZone.lowerLeft.x + myZone.lowerRight.x) / 2;
		double oldX = myZone.lowerRight.x;
		myZone.lowerRight.x = newX;
		myZone.upperRight.x = newX;
		
		newZone = new Zone(new Point(newX, myZone.lowerRight.y), new Point(oldX, myZone.lowerRight.y),
				new Point(newX, myZone.upperRight.y), new Point(oldX, myZone.upperRight.y));

	} else if ((myZone.lowerRight.x - myZone.lowerLeft.x) < (myZone.upperRight.y - myZone.lowerRight.y)) {
		double oldY = myZone.upperLeft.y;
		double newY = (myZone.lowerLeft.y + myZone.upperLeft.y) / 2;
		myZone.upperLeft.y = newY;
		myZone.upperRight.y = newY;
		
		newZone = new Zone(new Point(myZone.upperLeft.x, newY), new Point(myZone.upperRight.x, newY),
				new Point(myZone.upperLeft.x, oldY), new Point(myZone.upperRight.x, oldY));
	}
	
	return newZone;
}

*/