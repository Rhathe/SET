package gui;

import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import network.SocketToMe;
import network.serverStuff;

import setRules.*;

import java.awt.GridLayout;

public class LobbyTabs extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
/** LobbyTabs is a JPanel that consists of two tabs (game and lobby)
 * In order to create this JPanel simply call "LobbyTabs()".
 */
	
	JTabbedPane tabbedPane;
	public SocketToMe mySocket;
	public Lobby lobby;
	
	public LobbyTabs() {
        super(new GridLayout(1, 1));
        this.mySocket = GameScreen.mySocket;
        tabbedPane = new JTabbedPane();
        
        //First tab
        addGame(-20);
        
        //SECOND TAB
        lobby = new Lobby();
		tabbedPane.addTab("Lobby", lobby);

		add(tabbedPane);
    }
   

	/** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = LobbyTabs.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    public void addGame(int roomNumber) {
    	//Setup Game Stuff
    	String name;
    	if (roomNumber != -20)
    		name = "Room " + roomNumber;
    	else
    		name = "Single Player";
    	
        game myGame = new game(name);
        int[] cardsOnBoard = new int[15];
        cardsOnBoard = cards.convertToInt(myGame.board);
        CardLayout gameboard = new CardLayout(cardsOnBoard, myGame);

        mySocket.playa.addGame(myGame, gameboard, 0);
        
        tabbedPane.addTab(name, gameboard);
    }
}
