package com.can.draw;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
List<Rectangle> rectangles = new ArrayList<>();
List<Point2D> points = new ArrayList<>();


   @Override
   protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       for (Rectangle rectangle : rectangles) {
           rectangle.draw(g);
       }
      
       
   }

   public void addRectangle(Rectangle rectangle) {
       rectangles.add(rectangle);
       repaint();
   }

   @Override
   public Dimension getPreferredSize() {
       return new Dimension(400, 400);
   }
}