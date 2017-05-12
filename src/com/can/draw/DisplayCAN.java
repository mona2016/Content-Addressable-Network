package com.can.draw;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


import com.can.peer.Peer1;
import com.can.dao.Point;

public class DisplayCAN {
	
	private DrawingPanel panel = new DrawingPanel();
	private JFrame frame;
	HashMap<String, Peer1> allValues = new HashMap<>();
	
	public void createCAN(){
		
        frame = new JFrame("Content Addressable Network");
        frame.add(panel);
       
        double width, height;
        
        
        Set<String> keySet = allValues.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
        	//System.out.println(" starting the drawing:");
        	Peer1 peer = allValues.get(it.next());
        	Point nodeCoordinates = peer.getCoordinates();
        	double llx = 10*peer.getMyZone().lowerLeft.x;
        	double lly = 10*peer.getMyZone().lowerLeft.y;
        	double lrx = 10*peer.getMyZone().lowerRight.x;
        	double uly = 10*peer.getMyZone().upperLeft.y;
        	double ulx = 10*peer.getMyZone().upperLeft.x;
        	double x= (llx+lrx)/2;
        	double y= (lly+uly)/2;
        	width = (lrx-llx);
        	height = (uly-lly);
        	Rectangle rectangle = new Rectangle(ulx,uly,width,height);
        	
            panel.addRectangle(rectangle);
           
        }

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

	}
	
	public void startCAN(HashMap<String, Peer1> peers){
		allValues.putAll(peers);
		display();
		
	}
	
	public void display(){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createCAN();
			}
		});
		
	}

}
