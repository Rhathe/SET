package network;

import java.io.*;
import setRules.cards;

public class gameInfo implements Serializable {
	static final long serialVersionUID = 1;
	
	public int[] deck;
	public int[] board;
	public cards A, B, C;
	public Boolean request;
	public String roomName;
	public int it;
	public String playerName;
	public int roomNumber;
	public String roomAction;
	public Boolean setUpName;
	
	
	gameInfo(int[] deck, int[] board, cards A, cards B, cards C) {
		this.deck = deck;
		this.board = board;
		this.A = A;
		this.B = B;
		this.C = C;
		request = false;
		it = 0;
		playerName = "";
		roomAction = "";
		roomNumber = -1;
		roomName = "";
		setUpName = false;
	}
	
	public void nameOfGame(String name) {
		this.roomName = name;
	}
	
	public void nameOfConnection(String playerName) {
		this.playerName = playerName;
		setUpName = true;
	}
	
	public void changeRoom(String playerName, int roomNumber, String roomAction) {
		this.playerName = playerName;
		this.roomAction= roomAction;
		this.roomNumber = roomNumber;
		this.roomName = "Room " + roomNumber;
	}

}
