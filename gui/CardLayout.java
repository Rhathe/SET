package gui;

/* GRID LAYOUT HAS A SERIES OF IMAGE BUTTONS
 * this class visualizes the main game board.
 * It is added to the gameScreen class. 
 */

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import setRules.*;

import network.*;

/* TO DO LIST
 * pictureButton.setVisible(false);
 * getting the SET button to submit the stuff to somewhere
 * delay for blinking
 */

public class CardLayout extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int[] selectedCards = new int[21];
	protected int cardsSelected = 0;
	protected JButton SETButton = new JButton("SET");
	protected JButton DEALButton = new JButton("DEAL");
	public game myGame;
	public SocketToMe mySocket;
	
	public CardLayout(int[] cardNum, game myGame2) {
		myGame = myGame2;
		this.mySocket = GameScreen.mySocket;
		resetPane(cardNum);
		SETButton.addActionListener(setListener);
		DEALButton.addActionListener(dealListener);
	}
	
	private void resetPane(int cardNum[]){
		//CREATE THE GAMEBOARD
		removeAll();
		
		setPreferredSize(new Dimension(1000, 700));
		setLayout(new GridBagLayout());
		GridBagConstraints specs = new GridBagConstraints();
				
		//MAKE CARD BUTTONS
	   	JButton[] picButton = new JButton[cardNum.length];
		//need to allocute the actual buttons, last time was just practice
		for(int i=0;i<picButton.length;i++){
			picButton[i] = new JButton("");
		}
		int columns = 0;
		for (int z=0; z<cardNum.length;z++){
			if(cardNum.length<=12){
				columns = 4;
			}
			else if (cardNum.length>=13 && cardNum.length<=15 ){
				columns = 5;
			}
			else if (cardNum.length>=16 && cardNum.length<=18){
				columns = 6;
			}
			else if (cardNum.length>=19 && cardNum.length<=21){
				columns = 7;
			}
			int x = z%columns;	//columns  
			int y = z/columns;	//rows
			specs.gridx = x; 	//initial grid x
			specs.gridy = y; 	//initial grid y
			specs.insets = new Insets(10,10,10,10);  	//padding			
			add(picButton[z], specs);
				    
		    //Set the image or, if that's invalid, equivalent text.
		    ImageIcon icon;
		    ImageIcon iconR;
		    ImageIcon iconS;
		    ImageIcon iconT;
		    
		    String cardID = Integer.toString(cardNum[z]);
	    	icon = createImageIcon("cards2/"+cardID+".png");
	    	iconR = createImageIcon("cards2/"+cardID+"r.png");	
	    	iconS = createImageIcon("cards2/"+cardID+"s.png");
	    	iconT = createImageIcon("cards2/"+cardID+"t.png");

	    	//add functionality
			String command = "card"+cardID;
			picButton[z].setActionCommand(command);
		    picButton[z].addActionListener(pictureListener);
	    	
		    if (icon != null) {
	            picButton[z].setIcon(icon);
		    	picButton[z].setRolloverIcon(iconR);
		        picButton[z].setRolloverEnabled(true);
		        picButton[z].setSelectedIcon(iconS);
		        picButton[z].setRolloverSelectedIcon(iconT);
		        //setPressedIcon the moment pressed
		        Border myBorder = BorderFactory.createEmptyBorder(4,4,4,4);
	            picButton[z].setBorder(myBorder);
	        } 
	        else {
	        	picButton[z].setText("Image not found. This is the picture button.");
	        	picButton[z].setFont(picButton[z].getFont().deriveFont(Font.ITALIC));
	        	//picturePanel.setLayout(null);
	        	//pictureButton.setBounds(0,0,50,50); //location and size of picture
	        	picButton[z].setHorizontalAlignment(JButton.HORIZONTAL);
	        	picButton[z].setBorder(BorderFactory.createLineBorder(Color.BLACK));
	        }
		}
		
		//The SET Button
		specs.fill = GridBagConstraints.HORIZONTAL;
		specs.ipady = 0;       //reset to default
		specs.insets = new Insets(10,0,0,0);  		//top padding
		if(columns%2==0){
			specs.gridx = columns/2-1;      
			specs.gridwidth = 2;   			//2 columns wide
		}
		else{
			specs.gridx = columns/2;  		
			specs.gridwidth = 1;   			
		}
		specs.gridy = 4;       //bottom row
		
		SETButton.setActionCommand("SET");
		SETButton.setEnabled(false); 
		add(SETButton, specs);
		
		specs.gridy = 6; 
		DEALButton.setActionCommand("DEAL");
		DEALButton.setEnabled(true); 
		add(DEALButton, specs);
		
		//Everything past this is Ramon's doing
		
		JLabel label = new JLabel();
   		label.setText("Cards left in Deck = " + myGame.deck.size());
   		specs.gridy = 5;
    	add(label, specs);
    	validate();
    	repaint();
	}
	
	//Command for the picture button
     ActionListener pictureListener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            String commandPart = command.substring(0,4);
            String cardNum = command.substring(4,command.length());
            JButton jb = (JButton) ae.getSource(); 
            if ("card".equals(commandPart)){
            	if(jb.isSelected()==false){
            		jb.setSelected(true);
            		//add the card to the list of selected cards
            		selectedCards[cardsSelected]=Integer.parseInt(cardNum);
            		cardsSelected++;
            	}
            	else{
            		jb.setSelected(false);
            		//remove the card from the list and shift everything after down one
            		int deselected = Integer.parseInt(cardNum);
            		for(int i=0; i<cardsSelected; i++){
            			if(selectedCards[i]==deselected){
            				for(int j = i; j<20; j++){
            					selectedCards[j]=selectedCards[j+1];
            				}            				
            				selectedCards[20]=0;            				            				
            				break;
            			}
            		}
               		cardsSelected--;
            		//pictureButton.setVisible(false);
            	}
            	if(cardsSelected==3){
        			SETButton.setEnabled(true); 
        		}
        		else{
        			SETButton.setEnabled(false);    				
        		}
                System.out.println(cardNum);           
        	}
        }
    };
    
    //needed to change this from static -> public, not sure if that's ok
    public ActionListener setListener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            if ("SET".equals(command)){
            	//System.out.print("SET! ");
            	int i = 0;
            	/*
            	while(selectedCards[i]!=0){
            		System.out.print(selectedCards[i]+" ");
            		i++;
            	}
            	*/
            	//truncate the list into a 3vector
            	
            	int[] sendList = new int[3];
            	cards[] cardList = new cards[3];
            	
            	for(i=0;i<3;i++){
            		sendList[i] = selectedCards[i];
            		cardList[i] = cards.base10(sendList[i]);
            	}
            	if (myGame.id.equals("Single Player")) {
            		myGame.sendToServer(cardList[0], cardList[1], cardList[2]);
            		doWhenServerReceived();
            	}
            	else {
            		mySocket.sendSet(cardList[0], cardList[1], cardList[2], myGame);
            	}
        	}
        }
    };
    
    public ActionListener dealListener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            if ("DEAL".equals(command)){
            	
            	//myGame.dealFromDeck(3);
            	doWhenServerReceived();
        	}
        }
    };
    
    public void doWhenServerReceived() {
    	cardsSelected = 0;
    	int[] cardsOnBoard;
        cardsOnBoard = cards.convertToInt(myGame.board);
        resetPane(cardsOnBoard);
    }
	    
    //Image loader
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {//used for loading an image
        java.net.URL imgURL = CardLayout.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find: " + path);
            return null;
        }
    }
    //Blinking Cards
    /*static void blinkingCards(int theseCards[]){
    }
    */
    
    
    
}
