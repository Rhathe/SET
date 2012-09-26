package gui;

/* THIS LAYOUT INVOLVES BUTTONS AND PANELS
 * i switched to a grid layout format. 
 * don't use this. 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class myLayout extends JPanel {// makes panels

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public myLayout(){
		super(new BorderLayout());
	
	    //BANNER
	    //Set up banner to use as custom preview panel
	    JLabel banner = new JLabel("We Rock The House!",
	                        JLabel.CENTER);
	    banner.setForeground(new Color(76, 94, 118));
	    banner.setBackground(new Color(133, 172, 216));
	    banner.setOpaque(true);
	    banner.setFont(new Font("SansSerif", Font.BOLD, 24));
	    banner.setPreferredSize(new Dimension(100, 65));
	    //add the banner to a panel
	    JPanel bannerPanel = new JPanel(new BorderLayout());
	    bannerPanel.add(banner, BorderLayout.NORTH);
	    bannerPanel.setBorder(BorderFactory.createTitledBorder("Banner"));
	
	    //PANEL
	    JButton panelButton = new JButton("I like buttons!");
	    panelButton.setActionCommand("button1");
	    panelButton.addActionListener(buttonListener);
	    //panelButton.setPreferredSize(new Dimension(100, 65));
	    JPanel panel = new JPanel();
	    panel.add(panelButton);
	    panel.setBorder(BorderFactory.createTitledBorder("Amazing Titled Border"));
	    //panel.setBounds(50, 50, 100, 100);

	    //PICTURE PANEL
	    JButton pictureButton = new JButton("");
	    pictureButton.setActionCommand("picture");
	    pictureButton.addActionListener(pictureListener);
	    JPanel picturePanel = new JPanel(); //use FlowLayout
	    picturePanel.setPreferredSize(new Dimension(100, 65));
	    picturePanel.add(pictureButton);
	    picturePanel.setBorder(BorderFactory.createTitledBorder("Amazing Picture Panel"));
	    //Set the image or, if that's invalid, equivalent text.
        ImageIcon icon = createImageIcon("chair.jpg");
        if (icon != null) {
            pictureButton.setIcon(icon);
            pictureButton.setToolTipText("The Picture Tooltip");
            Border myBorder = BorderFactory.createEmptyBorder(4,4,4,4);
            pictureButton.setBorder(myBorder);
        } 
        else {
        	pictureButton.setText("Image not found. This is the picture button.");
        	pictureButton.setFont(pictureButton.getFont().deriveFont(Font.ITALIC));
        	picturePanel.setLayout(null);
        	//pictureButton.setBounds(0,0,50,50); //location and size of picture
        	pictureButton.setHorizontalAlignment(JButton.HORIZONTAL);
        	pictureButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        
        add(bannerPanel, BorderLayout.PAGE_START);
	    add(panel, BorderLayout.PAGE_END);
        add(picturePanel, BorderLayout.CENTER);

	}
	
	//The command for the button press   
    ActionListener buttonListener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            if ("button1".equals(command))
            	System.out.println("BUTTONS!");
        }
    };
    
    //Command for the picture button
    ActionListener pictureListener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            if ("picture".equals(command))
            	System.out.println("I'm a picture!");
        }
    };
    
    //Image loader
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {//used for loading an image
        java.net.URL imgURL = GameScreen.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
	
}
