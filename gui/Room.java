package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import network.*;

public class Room extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static int currentNum = 0;
	protected final int hardMax = 10; //const
	protected static GridBagConstraints constraints = new GridBagConstraints();
	protected static String currentPlayer = GameScreen.playa.name;
	public RoomList playerList;
	public int roomNumber;
	public String[] ipAddr;
	protected Lobby myLobby;
	public Boolean gameStarted = false;
	public SocketToMe mySocket;
	
	public Room(RoomList playerList2, int roomNumber2){
		playerList = playerList2;
		roomNumber = roomNumber2;
		myLobby = GameScreen.myLobby;
		mySocket = GameScreen.mySocket;
		resetRoom(roomNumber);
	}
	
	public void resetRoom(int roomNumber){
		removeAll();
		setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(200, 300));        
		setBorder(BorderFactory.createTitledBorder("Room "+Integer.toString(roomNumber)));
		//labels
        int i = 0;
        for(i=0;i<hardMax;i++){
            constraints.gridwidth = 2;
        	JLabel label = new JLabel();
        	constraints.gridx = 0;
        	constraints.gridy = i;
        	constraints.fill = GridBagConstraints.NONE;
       		label.setText("_____________________");
        	add(label, constraints);
        	if(i<playerList.roomMember.length){
        		JLabel label2 = new JLabel();
           		label2.setText(playerList.roomMember[i]);
            	add(label2, constraints);        		
        	}
        }
        //button - THIS MODIFIES THE PASSED ROOMLIST, STRUCTURE BETTER
        JButton button; 
        button = new JButton("Add Me");
 		constraints.gridx = 0;
 		constraints.gridy = hardMax+1; 
 		constraints.gridwidth = 1;
 		constraints.fill = GridBagConstraints.HORIZONTAL;
 		button.setActionCommand("Add");
		button.addActionListener(buttonPressed);
 		add(button, constraints);
 		if (gameStarted == true)
			button.setEnabled(false);
		else
			button.setEnabled(true); 
		
		//remove button
        button = new JButton("Remove");
 		constraints.gridx = 1;
 		constraints.gridy = hardMax+1; 
 		constraints.gridwidth = 1;
 		constraints.fill = GridBagConstraints.HORIZONTAL;
		button.setActionCommand("Remove");
		button.addActionListener(buttonPressed);
		add(button, constraints);
		if (gameStarted == true)
			button.setEnabled(false);
		else
			button.setEnabled(true); 
		
		//StartGame button
        button = new JButton("Start Game");
 		constraints.gridx = 0;
 		constraints.gridy = hardMax+2; 
 		constraints.gridwidth = 2;
 		constraints.fill = GridBagConstraints.NONE;
		button.setActionCommand("StartGame");
		button.addActionListener(buttonPressed);
		add(button, constraints);
		if (playerList.getNumPlayers() == 0 || gameStarted == true)
			button.setEnabled(false);
		else
			button.setEnabled(true); 
		
		validate(); //Needs to be here to display things when using the add() function
		repaint();
	}

    public ActionListener buttonPressed = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
	        String command = ae.getActionCommand();
	        if ("Add".equals(command)){
	        	mySocket.joinGame(roomNumber);
	        }
	       	else if ("Remove".equals(command)){
	       		mySocket.leaveGame(roomNumber);
	       	}
	       	else if ("StartGame".equals(command)){
	       		mySocket.requestGame(roomNumber);
	       	}
	       	else{
	       		System.out.print("wtf!");
	       	}
        }
    };
}
