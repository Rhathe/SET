package gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import gui.Room;

public class Lobby extends Container{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//constructor
	//const
	private int possibleGames = 5;
	protected LobbyTabs theLobbyTabs;
	public Room[] myRooms = new Room[possibleGames];
	
	public Lobby(){
		this.theLobbyTabs = GameScreen.myLobbyTabs;
		
		setLayout(new GridBagLayout());	
		GridBagConstraints constraints = new GridBagConstraints();
		
		//make the starting rooms
		RoomList[] rooms = new RoomList[possibleGames];
 		String[] names;
		names = null;
		for(int i =0; i<possibleGames; i++){
			rooms[i]=new RoomList(names);
		}

		for(int i=0;i<possibleGames;i++){
			//add room
	 		constraints.gridx = i;
	 		constraints.gridy = 0; 			
	 		constraints.gridwidth = 1;
	 		
	 		Room room = new Room(rooms[i],i); //makes a Room
	 		myRooms[i] = room;
	 		add(room, constraints);			//adds the Room to Lobby
		}
	}
	
}
