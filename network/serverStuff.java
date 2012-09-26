package network;

import java.io.*;
import java.net.*;
import java.util.*;
import setRules.*;
import gui.*;

public class serverStuff extends Thread {

   private ServerSocket myServerSocket;
   public Vector<Connect> myClients = new Vector<Connect>();
   public Vector<game> allGames = new Vector<game>();
   public Vector<Room> allRooms = new Vector<Room>();
   
   public serverStuff(int port) {
     try {
    	 myServerSocket = new ServerSocket(port);
     } catch(Exception e) {
    	 System.err.println(e.getMessage());
     }
     System.out.println("Server listening on port " + port);
     this.start();
   }
   
   public void addGame(game myGame) {
	   allGames.add(myGame);
   }

   public void sendUpdateForRoom(gameInfo packet, String roomName) {
	   for (Connect client : myClients) {
		   if (client.hasGame(roomName) == true) {
			   client.sendUpdate(packet);
		   }
	   }
   }
   
   public void sendUpdateStartGame(gameInfo packet, int roomNumber, game myGame) {
	   
	   Room daRoom = null;
	   for (Room someRoom : allRooms) {
		   if (roomNumber == someRoom.roomNumber) {
			   daRoom = someRoom;
		   }
	   }
	   
	   for (Connect client : myClients) {
		   for (String playerName : daRoom.playerList.roomMember) {
			   if (client.name.equals(playerName) == true) {
				   client.myGames.add(myGame);
				   client.sendUpdate(packet);
				   break;
			   }
		   }
	   }
   }
   
   public void sendUpdateAll(gameInfo packet) {
	   for (Connect client : myClients) {
		   client.sendUpdate(packet);
	   }
   }
   
   public void removeConnection(Connect c) {
	   for (int test = 0;  test < myClients.size(); ++test) {
		   if (c.equals(myClients.get(test)) == true) {
			   myClients.remove(test);
		   }
	   }
   }
   
   public void run() {
	   while(true) {
		   try {
			   System.out.println("Waiting for connections.");
			   Socket client = myServerSocket.accept();
			   System.out.println("Accepted a connection from: "+client.getInetAddress());
			   myClients.add(new Connect(client, this));		   
		   } catch(Exception e) {
			   System.err.println(e.getMessage());
		   }
	   }
   }
   
   public static void main(String[] args) {
	   new serverStuff(31215);
   }
}

class Connect extends Thread {
   private Socket client = null;
   public String name;
   private serverStuff myServer;
   private ObjectInputStream ois = null;
   private ObjectOutputStream oos = null;
   public Vector<game> myGames = new Vector<game>();
   public int it = 0;

   public Connect(Socket clientSocket, serverStuff myServer) {
	   this.myServer = myServer;
	   client = clientSocket;
     
	   try {
		   ois = new ObjectInputStream(client.getInputStream());
		   oos = new ObjectOutputStream(client.getOutputStream());
	   } catch(Exception e1) {
		   try {
			   client.close();
		   } catch(Exception e) {
			   System.out.println(e.getMessage());
		   }
		   return;
	   }
	   this.start();
   }

   public void sendUpdate(gameInfo packet) {
	   System.out.println("Sending UPDATE to" + name);
	   	
	   try { 
		   oos.writeObject(packet);
		   oos.flush();
	   }
	   catch(Exception e) {
		   System.out.println(e.getMessage());
	   }
   }
   
   public Boolean hasGame(String roomName) {
	   for (game someGame : myGames) {
		   if (someGame.id.equals(roomName)) {
			   return true;
		   }
	   }
	   
	   return false;
   }
   
   public void run() {
	   while(true) {
		   if (client.isClosed() == true) {
			   try {
				   myServer.removeConnection(this);
			   } catch(Exception e) {
				   e.getStackTrace();
			   }
		   }
		   
		   try {
			   gameInfo result = (gameInfo) ois.readObject();
			   if (result != null) {
				   System.out.println("Received, PROCESSING");
	  	  		
				   if (result.setUpName == true) {
					   name = result.playerName;
				   }
				   else if (result.roomAction.equals("StartGame")) {
					   System.out.println("Request Granting for Room " + result.roomNumber);
					   game myGame = new game(result.roomName); 
					   myServer.allGames.add(myGame);
					   myGames.add(myGame);
		  	  		
					   gameInfo update = new gameInfo(null, null,null,null,null);
					   update.changeRoom(null, result.roomNumber, "GameStarted");
					   myServer.sendUpdateAll(update);
					   
					   update = new gameInfo(cards.convertToInt(myGame.deck), cards.convertToInt(myGame.board),null,null,null);
					   update.changeRoom(null, result.roomNumber, "YoureIn");
					   myServer.sendUpdateStartGame(update, result.roomNumber, myGame);
				   }	  	  		
				   else if (result.roomAction.equals("Add")) {	  	  			
					   System.out.println("Adding to Room " + result.roomNumber);
					   
					   Room daRoom = null;
					   for (int i = 0; i < myServer.allRooms.size(); ++i) {
						   if (result.roomNumber == myServer.allRooms.get(i).roomNumber) {
							   daRoom = myServer.allRooms.get(i);
							   break;
						   }
					   }
					   if (daRoom == null) {
						   daRoom = new Room(new RoomList(null), result.roomNumber);
						   myServer.allRooms.add(daRoom);
					   }
					   
					   daRoom.playerList.addMember(result.playerName);		  	  		
	  	  			
					   gameInfo update = new gameInfo(null, null,null,null,null);
					   update.changeRoom(result.playerName, result.roomNumber, "Add");
					   myServer.sendUpdateAll(update);
				   }		  	  	
				   else if (result.roomAction.equals("Remove")) {	  	  			
					   System.out.println("Removing from Room " + result.roomNumber);	  	  			
					   
					   Room daRoom = null;
					   for (int i = 0; i < myServer.allRooms.size(); ++i) {
						   if (result.roomNumber == myServer.allRooms.get(i).roomNumber) {
							   daRoom = myServer.allRooms.get(i);
							   break;
						   }
					   }
					   if (daRoom == null) {
						   daRoom = new Room(new RoomList(null), result.roomNumber);
						   myServer.allRooms.add(daRoom);
					   }
					   
					   daRoom.playerList.removeMember(result.playerName);		  	  		
	  	  			
					   gameInfo update = new gameInfo(null, null,null,null,null);		  	  		
					   update.changeRoom(result.playerName, result.roomNumber, "Remove");		  	  		
					   myServer.sendUpdateAll(update);	  	  		
				   }		  	  	
				   else {		  	  		
					   cards A = result.A;		  	  		
					   cards B = result.B;		  	  		
					   cards C = result.C;		  	  		
		  	  		
					   for (int i = 0; i < myGames.size(); ++i) {		  	  			
						   String id = myGames.get(i).id;		  	  			
		  	  			
						   if (id.equals(result.roomName)){ 		  	  				
							   System.out.println("Found " + id);		  	  				
							   game myGame = myGames.get(i);		  	  				
			  	  			
							   myGame.sendToServer(A,B,C);				  	  		
				  	  		
							   gameInfo update = new gameInfo(cards.convertToInt(myGame.deck), cards.convertToInt(myGame.board),null,null,null);				  	  		
							   update.nameOfGame(result.roomName);				  	  		
							   myServer.sendUpdateForRoom(update, result.roomName);		  	  				
							   break;		  	  			
						   }		  	  		
					   }		  	  	
				   }	  	  	
			   }	      
		   } catch(Exception e) {
			   System.err.println("Number of Clients: " + myServer.myClients.size());
			   System.err.println("Connection closed? " + e.getMessage());
			   myServer.removeConnection(this);
			   System.err.println("Number of Clients: " + myServer.myClients.size());
			   return;
		   }      
	   }   
   }
}

