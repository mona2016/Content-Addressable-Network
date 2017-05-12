package com.can.draw;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Rectangle {

 double x, y, width, height;
	 
	 public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
	 
	 public void draw(Graphics g) {
		 Graphics2D g2 = (Graphics2D)g;
		 g2.draw(new Rectangle2D.Double(x,y,width,height));
    }
}
