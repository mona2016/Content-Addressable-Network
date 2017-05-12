package com.can.peer;
 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.can.dao.Point;
import com.can.dao.Zone;
import com.can.draw.DisplayCAN;

public class Peer1 {

	static boolean ready=true;
	static double maximum = 10.0;
	static double minimum = 0.0;
	String peerID;
	String peerIP;
	Point coordinates;
	
	Zone myZone=new Zone();
	HashMap<String, Peer1> allPeers = new HashMap<>();
	HashMap<String, Zone> AllZones = new HashMap<String, Zone>();
	HashMap<String, Zone> myNeighbors = new HashMap<String, Zone>();
	
	
	
	
	public Peer1(){
		
	}
	
	public Peer1(String peerID, String peerIP, Point coordinates, Zone myZone) {
		super();
		this.peerID = peerID;
		this.peerIP = peerIP;
		this.coordinates = coordinates;
		this.myZone = myZone;
	}


	public static void main(String args[]) throws UnknownHostException{
	
		Peer1 peer = new Peer1();
		peer.allOptions();
	}
	
	private void allOptions() throws UnknownHostException {
		Peer1 firstPeer = new Peer1("P1", getIpAddress(), new Point(getRandomCoordinates(maximum, minimum), getRandomCoordinates(maximum, minimum)),new Zone());
		allPeers.put(firstPeer.peerID, firstPeer);
		AllZones.put(firstPeer.peerID, firstPeer.myZone);
		Scanner sc = new Scanner(System.in);
		try{
			while(ready) {
				
				System.out.println("Please choose one of the following options :- ");
				System.out.println("1. Peer Addition");
				System.out.println("2. Peer Deletion");
				System.out.println("3. View Peer Structure");
				System.out.println("4. Veiw all Peer details");
				
				int action = Integer.parseInt(sc.nextLine().trim());
				
				if(action==1){
					Peer1 peer= getRandomPeer(allPeers);
					joinPeerToNetwork(peer);
					System.out.println("want to try again(y/n)?");
					if(sc.nextLine().equals("n")){
						ready=false;
						sc.close();
					}
				}
				else if(action==2){
						if(allPeers.size()!=1){
						System.out.println("Enter the id of peer that wants to leave: ");
						String input=sc.nextLine().trim();
						
						while(!allPeers.containsKey(input)){
							System.out.println("This peer is not present in the network. Please provide another value: ");
							input=sc.nextLine().trim();
						}
						
						leaveNetwork(allPeers.get(input));
						System.out.println("want to try again(y/n)?");
						if(sc.nextLine().equals("n")){
							ready=false;
							sc.close();
						}
						
					}
						else
							System.out.println("The network has only one Peer. You cannot remove it.");
				}
				
				else if(action==3){
					
					System.out.println("zones of all peers: ");
					for (String x : allPeers.keySet()) {
						System.out.println("peer "+allPeers.get(x).peerID+ " zone : "+ allPeers.get(x).myZone);
					}
					
					DisplayCAN disp = new DisplayCAN();
					disp.startCAN(allPeers);
					System.out.println("want to try again(y/n)?");
					if(sc.nextLine().equals("n")){
						ready=false;
						sc.close();
					}
				}
				else if(action==4){
					System.out.println("____________________________________________");
					System.out.println("Details of all nodes");
					for (String key : allPeers.keySet()) {
						System.out.println("Peer " + key);
						System.out.println("Coordinates: "+ allPeers.get(key).getCoordinates());
						System.out.println("Neighbors: "+allPeers.get(key).myNeighbors.keySet());
						System.out.println("Peer Zone: "+allPeers.get(key).getMyZone());
						System.out.println("____________________________________________");
					}
					System.out.println("____________________________________________");
					
					System.out.println("want to try again(y/n)?");
					if(sc.nextLine().equals("n")){
						ready=false;
						sc.close();
					}
				}
					
				else{
					System.out.println("Try again");
				}
				
			}	
		}catch (NumberFormatException e) {
			
			e.printStackTrace();
		
		} 
		
	}
	
	public void joinPeerToNetwork(Peer1 peer){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Peer ID");
		peerID=sc.nextLine().trim();
		while(allPeers.containsKey(peerID)){
			System.out.println("This ID is already taken by some peer. Please provide another ID: ");
			peerID=sc.nextLine().trim();
		}
		
		System.out.println("Enter the Peer IP");
		peerIP=sc.nextLine().trim();
		
		double x=getRandomCoordinates(maximum,minimum);
		double y=getRandomCoordinates(maximum,minimum);
		
		Peer1 newPeer= new Peer1();
		newPeer.setPeerID(peerID);
		newPeer.setPeerIP(peerIP);
		newPeer.setCoordinates(new Point(x,y));
		//System.out.println("New Peer: "+ newPeer.toString());
		
		// check if the coordinate is in the current zone
		boolean isInZone=false;
	
		//System.out.println("isInZone? "+ isInZone);
		while(isInZone!=true){
			//System.out.println("searching for a zone for new peer");
			peer = getRandomPeer(allPeers);
			isInZone = Peer1.checkPointInZone(newPeer.getCoordinates().getX(), newPeer.getCoordinates().getY(), peer.myZone);
			//System.out.println("isInZone? "+ isInZone);
		}
	
		// split zone
		if(isInZone){
			newPeer.myZone=splitZone(peer.myZone);
			System.out.println("Success !!");
			//System.out.println("new Zone: "+ newPeer.myZone);
			
			updateNewPeer(newPeer,peer);
			updateNeighbors(peer,newPeer);
			
			allPeers.put(newPeer.peerID, newPeer);
			AllZones.put(newPeer.peerID, newPeer.myZone);
			
			System.out.println("______________________");
			System.out.println("neighbors of all nodes: ");
			for (Peer1 p : allPeers.values()) {
				System.out.println("for peer "+p.peerID+" neighbors: "+p.myNeighbors.keySet());
				
			}
			System.out.println("______________________");
			
			
			
		}
		
		
		
	}
	
	public void updateNewPeer(Peer1 newPeer, Peer1 peer){
		
		
		for (String key : peer.myNeighbors.keySet()) {
			if (Peer1.hasCommonEdge(newPeer.getMyZone(), peer.myNeighbors.get(key))){
				if(key!=newPeer.peerID){
					;
					
					newPeer.myNeighbors.put(key, peer.myNeighbors.get(key));
					if(!allPeers.get(key).myNeighbors.containsKey(newPeer.getPeerID()))
						allPeers.get(key).myNeighbors.put(newPeer.peerID, newPeer.getMyZone());
					
				}
			}
		}
		newPeer.myNeighbors.put(peer.peerID, peer.myZone);
		System.out.println("All neighbors of newPeer "+newPeer.peerID+" are: "+newPeer.myNeighbors.keySet());
	
		
	}
	
	public void updateNeighbors(Peer1 peer, Peer1 newPeer){
		
		for (Iterator<Map.Entry<String, Zone>> it = peer.myNeighbors.entrySet().iterator(); 
			     it.hasNext();)
			{
			    Map.Entry<String, Zone> entry = it.next();
			    Zone n = entry.getValue();
			    if(n != null) 
			    	if(!Peer1.hasCommonEdge(peer.getMyZone(), n)){
			    		it.remove();
			    		allPeers.get(entry.getKey()).myNeighbors.remove(peer.getPeerID());		//newly added
			    	}
		   }

		peer.myNeighbors.put(newPeer.getPeerID(), newPeer.myZone);
		
	}
	
	
	public void leaveNetwork(Peer1 peer){
		
		String ID = peer.peerID;
		Zone z = new Zone();
		String k="";
		boolean flag = false;
		for (String key : peer.myNeighbors.keySet()) {
			z=mergeZones(peer.myZone, AllZones.get(key));
			if(z!=null){
				
				k=key;
				flag=true;
				System.out.println("Peer"+peer.getPeerID()+" zone is merged with Peer "+k);
				
				break;
			}
		}
		if (flag){
			allPeers.get(k).setMyZone(z);
			updateNetwork(peer, allPeers.get(k));
			allPeers.remove(peer.getPeerID());
			AllZones.remove(peer.getPeerID());
			System.out.println("*********************************************************");
			for (String key : allPeers.keySet()) {
				System.out.println("neighbors of Peer " + key + " are "+allPeers.get(key).myNeighbors.keySet());
			}
			System.out.println("*********************************************************");
		}
	}
	
	private void updateNetwork(Peer1 departingPeer, Peer1 mergedPeer) {
		
		for (String neighbor : departingPeer.myNeighbors.keySet()) {
			
			if (!neighbor.equals(mergedPeer.getPeerID())){
				if (!mergedPeer.myNeighbors.containsKey(neighbor)){
					mergedPeer.myNeighbors.put(neighbor, allPeers.get(neighbor).getMyZone());
					allPeers.get(neighbor).myNeighbors.put(mergedPeer.peerID, mergedPeer.getMyZone());
					allPeers.get(neighbor).myNeighbors.remove(departingPeer.getPeerID());
				}
			}
			
			for (String  key : departingPeer.myNeighbors.keySet()) {
				allPeers.get(key).myNeighbors.remove(departingPeer.getPeerID());
			}
			
		}
		if(mergedPeer.myNeighbors.containsKey(departingPeer.getPeerID())){
			mergedPeer.myNeighbors.remove(departingPeer.getPeerID());
		}
		
	}

	// z1<-- the departing peer's zone
	// z2<-- the merging peer's zone
	
	public Zone mergeZones(Zone z1, Zone z2){
		
		if((z1.lowerLeft.x==z2.upperLeft.x) && (z1.lowerLeft.y==z2.upperLeft.y) && (z1.lowerRight.x==z2.upperRight.x) && (z1.lowerRight.y==z2.upperRight.y))
		{
			z2.upperLeft.x=z1.upperLeft.x;
			z2.upperLeft.y=z1.upperLeft.y;
			z2.upperRight.x=z1.upperRight.y;
			z2.upperRight.y=z1.upperRight.y;
			return z2;
		}
		else if ((z1.upperLeft.x==z2.lowerLeft.x) && (z1.upperLeft.y==z2.lowerLeft.y) && (z1.upperRight.x==z2.lowerRight.x) && (z1.upperRight.y==z2.lowerRight.y)){
			z2.lowerLeft.x=z1.lowerLeft.x;
			z2.lowerLeft.y=z1.lowerLeft.y;
			z2.lowerRight.x=z1.lowerRight.x;
			z2.lowerRight.y=z1.lowerRight.y;
			return z2;
		}
		else if ((z1.lowerLeft.x==z2.lowerRight.x) && (z1.lowerLeft.y==z2.lowerRight.y) && (z1.upperLeft.x==z2.upperRight.x) && (z1.upperLeft.y == z2.upperRight.y)){
			z2.lowerRight.x = z1.lowerRight.x;
			z2.lowerRight.y = z1.lowerRight.y;
			z2.upperRight.x = z1.upperRight.x;
			z2.upperRight.y = z1.upperRight.y;
			return z2;
		}
		else if ((z1.lowerRight.x==z2.lowerLeft.x) && (z1.lowerRight.y==z2.lowerLeft.y) && (z1.upperRight.x==z2.upperLeft.x) && (z1.upperRight.y == z2.upperLeft.y)){
			z2.lowerLeft.x = z1.lowerLeft.x;
			z2.lowerLeft.y = z1.lowerLeft.y;
			z2.upperLeft.x = z1.upperLeft.x;
			z2.upperLeft.y = z1.upperLeft.y;
			return z2;
		}
		
		
		return null;
		
	}
	
	public Peer1 getRandomPeer(HashMap<String, Peer1> peers){
		
		Random       random    = new Random();
		ArrayList<String> keys      = new ArrayList<String>(peers.keySet());
		String key = keys.get( random.nextInt(keys.size()) );
		Peer1 value = peers.get(key);
		return (value);
		
	}
	
	//Generates Random coordinates
	public double getRandomCoordinates(double max, double min){
		double value=0.0;
		double p = Math.random();
		if (p < 0.5) {
			value = (1 - Math.random()) * (max - min) + min;
			return (value);
			
		}
		value=Math.random() * (max - min) + min;
		return (Math.random() * (max - min) + min);
	}
	
	
	public static boolean checkPointInZone(double x, double y, Zone zone) {
			
			if((x>=zone.lowerLeft.x && x<=zone.lowerRight.x) && (y>=zone.lowerLeft.y && y<=zone.upperLeft.y))
				return true;
			return false;
	
		}
	
	//Splitting zones
	public static Zone splitZone(Zone myZone) {
			
			Zone newZone=null;
			
			if (((myZone.lowerRight.x - myZone.lowerLeft.x) == (myZone.upperLeft.y - myZone.lowerLeft.y)) || ((myZone.lowerRight.x - myZone.lowerLeft.x) > (myZone.upperLeft.y - myZone.lowerLeft.y))) {
				double newX = (myZone.lowerLeft.x + myZone.lowerRight.x) / 2;
				double oldX = myZone.lowerRight.x;
				myZone.lowerRight.x = newX;
				myZone.upperRight.x = newX;
				
				newZone = new Zone(new Point(newX, myZone.lowerLeft.y), new Point(oldX, myZone.lowerRight.y),
						new Point(newX, myZone.upperLeft.y), new Point(oldX, myZone.upperRight.y));
	
			} else if ((myZone.lowerRight.x - myZone.lowerLeft.x) < (myZone.upperRight.y - myZone.lowerRight.y)) {
				double oldY = myZone.upperLeft.y;
				double newY = (myZone.lowerLeft.y + myZone.upperLeft.y) / 2;
				myZone.upperLeft.y = newY;
				myZone.upperRight.y = newY;
				
				newZone = new Zone(new Point(myZone.lowerLeft.x, newY), new Point(myZone.lowerRight.x, newY),
						new Point(myZone.upperLeft.x, oldY), new Point(myZone.upperRight.x, oldY));
			}
			
			return newZone;
		}


	//get the System's IP address that forms the first node
	public static String getIpAddress() throws UnknownHostException {
		InetAddress addr= InetAddress.getLocalHost();
		   byte[] ip=addr.getAddress();
		
	       int i = 4;
	       String ipAddress = "";
	       for (byte raw : ip)
	       {
	           ipAddress += (raw & 0xFF);
	           if (--i > 0)
	           {
	               ipAddress += ".";
	           }
	       }

	       return ipAddress;
	   }
	
	
public static boolean hasCommonEdge(Zone z1, Zone z2) {
		
		boolean commonWidth = (z1.lowerLeft.x == z2.upperLeft.x
				&& (z1.lowerRight.x > z1.lowerLeft.x && z2.upperRight.x > z1.lowerLeft.x) && z1.lowerLeft.y == z2.upperLeft.y)
				|| (z1.upperLeft.x == z2.lowerLeft.x
						&& (z1.upperRight.x > z1.upperLeft.x && z2.lowerRight.x > z1.upperLeft.x) && z1.upperLeft.y == z2.lowerLeft.y)
				
				|| ((z1.lowerRight.x == z2.upperRight.x) && (z1.lowerRight.y == z2.upperRight.y) && ((z1.lowerLeft.x < z1.lowerRight.x) && (z2.upperLeft.x < z2.upperRight.x)))
				|| ((z1.upperRight.x == z2.lowerRight.x) && (z1.upperRight.y == z2.lowerRight.y) && ((z1.upperLeft.x < z1.upperRight.x) && (z2.lowerLeft.x < z1.upperRight.x)))
				
				
				|| (z1.upperLeft.x == z2.lowerLeft.x && z1.upperLeft.y == z2.lowerLeft.y && ((z1.upperRight.x > z1.upperLeft.x) && (z2.lowerRight.x > z1.upperLeft.x)))
				
				
				
				|| (z1.upperLeft.x <= z2.lowerLeft.x && z1.upperLeft.y == z2.lowerLeft.y
						&& z1.upperRight.x >= z2.lowerRight.x && z1.upperLeft.y == z2.lowerLeft.y)
				|| (z2.upperLeft.x <= z1.lowerLeft.x && z2.upperLeft.y <= z1.lowerLeft.y
						&& z2.upperRight.x >= z1.lowerRight.x && z2.upperLeft.y == z1.lowerLeft.y);
		
		boolean commonHeight = (z1.lowerRight.x == z2.lowerLeft.x && z1.lowerRight.y == z2.lowerLeft.y && z1.upperRight.x <= z2.upperLeft.x)
				|| ((z1.lowerRight.x == z2.lowerLeft.x) && (z1.lowerRight.y == z2.lowerLeft.y)
						&& (z1.upperRight.y > z1.lowerRight.y) && (z2.upperLeft.y > z1.lowerRight.y))
				|| ((z2.lowerRight.x == z1.lowerLeft.x) && (z2.lowerRight.y == z1.lowerLeft.y)
						&& (z2.upperRight.y > z2.lowerRight.y) && (z1.upperLeft.y > z2.lowerRight.y))
				|| ((z1.upperRight.x == z2.upperLeft.x) && (z2.upperRight.y == z1.upperLeft.y)
						&& (z1.lowerRight.y < z1.upperRight.y) && (z2.lowerLeft.y < z1.upperRight.y))
				|| ((z2.upperRight.x == z1.upperLeft.x) && (z2.upperRight.y == z1.upperLeft.y)
						&& (z2.lowerRight.y < z2.upperRight.y) && (z1.lowerLeft.y < z2.upperRight.y))
				|| (z1.lowerLeft.x == z2.lowerRight.x && z1.lowerLeft.y == z2.lowerRight.y && z1.upperLeft.x >= z2.upperRight.x);

		return (commonWidth || commonHeight);
	}
	

	/**
	 * @return the peerID
	 */
	public String getPeerID() {
		return peerID;
	}

	/**
	 * @param peerID the peerID to set
	 */
	public void setPeerID(String peerID) {
		this.peerID = peerID;
	}

	/**
	 * @return the peerIP
	 */
	public String getPeerIP() {
		return peerIP;
	}

	/**
	 * @param peerIP the peerIP to set
	 */
	public void setPeerIP(String peerIP) {
		this.peerIP = peerIP;
	}

	/**
	 * @return the myZone
	 */
	public Zone getMyZone() {
		return myZone;
	}

	/**
	 * @param myZone the myZone to set
	 */
	public void setMyZone(Zone myZone) {
		this.myZone = myZone;
	}


	/**
	 * @return the coordinates
	 */
	public Point getCoordinates() {
		return coordinates;
	}

	@Override
	public String toString() {
		return "Peer1 [peerID=" + peerID + ", peerIP=" + peerIP + ", coordinates=" + coordinates + ", myZone=" + myZone
				+ "]";
	}

	

	/**
	 * @return the allPeers
	 */
	public HashMap<String, Peer1> getAllPeers() {
		return allPeers;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
}
	
}


