package network;

import java.io.*;
import java.net.*;

import gui.*;
import setRules.*;

public class SocketToMe extends Thread {

	ObjectOutputStream oos;
	ObjectInputStream ois;
	public gameInfo result;
	public player playa;
	public LobbyTabs myLobbyTabs;
	
    public SocketToMe(String ipAddr, int port) {
        Socket mySocket = null;
        this.playa = GameScreen.playa;
        
        System.out.println("Attempting to connect to " + ipAddr + " at port " + port);
        
        try {
            mySocket = new Socket(ipAddr, port);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + ipAddr);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + ipAddr);
            System.exit(1);
        }
        
        try {
        	oos = new ObjectOutputStream(mySocket.getOutputStream());
        	ois = new ObjectInputStream(mySocket.getInputStream());
        } catch (IOException e) {
        	System.out.println(e.getMessage());
        }
        
        this.start();
    }
    
    public void send(gameInfo packet) {
    	System.out.println("Sending!");
    	try { 
    		oos.writeObject(packet);
    		oos.flush();
    	}
    	catch(Exception e) {
    		System.err.println("Sending FAILURE!" + e.getMessage());
    	}
    }
    
    public void sendSet(cards A, cards B, cards C, game myGame) {
    	gameInfo sent = new gameInfo(null, null, A, B, C);
    	sent.nameOfGame(myGame.id);
    	send(sent);
    }
    
    public void requestGame(int roomNumber) {
    	gameInfo sent = new gameInfo(null, null, null, null, null);
    	sent.changeRoom(GameScreen.playa.name, roomNumber, "StartGame");
    	send(sent);
    }
    
    public void joinGame(int roomNumber) {
    	gameInfo sent = new gameInfo(null, null, null, null, null);
    	sent.changeRoom(GameScreen.playa.name, roomNumber, "Add");
    	send(sent);
    }
    
    public void leaveGame(int roomNumber) {
    	gameInfo sent = new gameInfo(null, null, null, null, null);
    	sent.changeRoom(GameScreen.playa.name, roomNumber, "Remove");
    	send(sent);
    }
    
	public void run() {
		
		gameInfo sent = new gameInfo(null, null, null, null, null);
    	sent.nameOfConnection(GameScreen.playa.name);
    	send(sent);
		
		while(true) {
			try {
	    	  	result = (gameInfo) ois.readObject();
	    	  	if (result != null) {
	    	  		
	    	  		if (result.roomAction.equals("Add")) {
	    	  			Room daRoom = GameScreen.myLobby.myRooms[result.roomNumber];
		    	  		daRoom.playerList.addMember(result.playerName);
			        	daRoom.resetRoom(result.roomNumber);
			        	continue;
	    	  		}
	    	  		else if (result.roomAction.equals("Remove")) {
	    	  			Room daRoom = GameScreen.myLobby.myRooms[result.roomNumber];
		    	  		daRoom.playerList.removeMember(result.playerName);
			        	daRoom.resetRoom(result.roomNumber);
			        	continue;
	    	  		}
	    	  		else if (result.roomAction.equals("GameStarted")) {
	    	  			Room daRoom = GameScreen.myLobby.myRooms[result.roomNumber];
			       		daRoom.gameStarted = true;
			       		daRoom.resetRoom(result.roomNumber);
			       		continue;
	    	  		}
	    	  		else if (result.roomAction.equals("YoureIn")) {
	    	  			GameScreen.myLobbyTabs.addGame(result.roomNumber);
	    	  		}
		        	
	    	  		int i = playa.find(result.roomName);
	    	  		
	    	  		game gameBoard = playa.getGame(i);
	    	  		System.out.println("Client received data for Game in " + gameBoard.id);
	    	  		CardLayout boardLayout = playa.getLayout(i);	    	  		
	    	  		gameBoard.deck = cards.convertToCards(result.deck);
	    	  		gameBoard.board = cards.convertToCards(result.board);
	    	  		boardLayout.doWhenServerReceived();
	    	  	}
	      	} catch(Exception e) {System.out.println(e.getMessage());}
		}
	}
}

