package gui;

/* GUI WINDOW AND TOOLBAR
 * this class creates the MENU toolbar
 * it also has the MAIN and CREATEANDSHOWGUI
 * so it executes a GUI -david
 */

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import network.*;
import setRules.*;

public class GameScreen {
	public static SocketToMe mySocket;
	public static serverStuff myServer;
	public static Lobby myLobby;
	public static LobbyTabs myLobbyTabs;
	public static player playa = new player("John Doe" + System.currentTimeMillis() , 1);
	
	//THE MENU BAR
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
 
        //Create the menu bar.
        menuBar = new JMenuBar();

        //FIRST MENU
        //make the first menu and add it to the menu bar
        menu = new JMenu("A Menu");
        menuBar.add(menu);
        
	        //a group of JMenuItems
        	//make a menu item and add it to the menu.
	        menuItem = new JMenuItem("How do you feel about lists?");
	        menu.add(menuItem);
	
	        menuItem = new JMenuItem("We all like lists");
	        menu.add(menuItem);
	
	        menuItem = new JMenuItem("Lists are fun");
	        menu.add(menuItem);
	
	        menuItem = new JMenuItem("Fun fun fun");
	        menu.add(menuItem);
	
	        menu.addSeparator(); //adds a visual separation (a line)
	
	        //submenu still in menu
	        submenu = new JMenu("Submenu");

	        	menuItem = new JMenuItem("secrets");
	        	submenu.add(menuItem);
	
		        menuItem = new JMenuItem("extra secrets");
		        submenu.add(menuItem);

		    menu.add(submenu);
		    
	        menu.addSeparator();
	        menuItem = new JMenuItem("Exit");
	        menuItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent ae) {
	                System.exit(0);
	            }
	        });
	        menu.add(menuItem);

        //SECOND MENU
	    //these have functionality
        menu = new JMenu("Another Menu");
        menuBar.add(menu); //add to menuBar not menu
	        menuItem = new JMenuItem("POne");
	        menuItem.setActionCommand("POne"); //identifies the command
	        menuItem.addActionListener(menuListener); //directs it to a particular function
	        menu.add(menuItem);
	        menuItem = new JMenuItem("PTwo");
	        menuItem.setActionCommand("PTwo");
	        menuItem.addActionListener(menuListener);
	        menu.add(menuItem);
	        menuItem = new JMenuItem("PThree");
	        menuItem.setActionCommand("PThree");
	        menuItem.addActionListener(menuListener);
	        menu.add(menuItem);
	        menuItem = new JMenuItem("PFour");
	        menuItem.setActionCommand("PFour"); 
	        menuItem.addActionListener(menuListener);
	        menu.add(menuItem);
        return menuBar;
    }
    
    //SETUP THE MENU COMMAND FUNCTIONALITY
    ActionListener menuListener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            if ("POne".equals(command))
            	System.out.println("You're brilliant");
            else if ("PTwo".equals(command))
            	System.out.println("You're amazing");
            else if ("PThree".equals(command))
            	System.out.println("You're beautiful");
            else if ("PFour".equals(command))
            	System.out.println("You're gorgeous");

        }
    };
 
    //UNINTERESTING MAIN FUNCTION
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args[0],args[1]); 
            }
        });
    }
    
    //BASICALLY AN EXTENSION OF MAIN
    private static void createAndShowGUI(String address, int port) {

		mySocket = new SocketToMe(address, port);
		myLobbyTabs = new LobbyTabs();
		myLobby = myLobbyTabs.lobby;
		playa.myLobby = myLobby;
		
        JFrame frame = new JFrame("Amazing GUI"); //title bar, and makes a frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        //Add Menu
        GameScreen demo = new GameScreen();
        frame.setJMenuBar(demo.createMenuBar());
        
        //Add content to the window.
        frame.add(myLobbyTabs, BorderLayout.CENTER);
		
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}

