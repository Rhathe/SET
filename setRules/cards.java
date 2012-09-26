package setRules;

import java.util.Vector;
import java.io.*;

public class cards implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int num;
	int symbol;
	int color;
	int shade;
	
	public cards(int num, int symbol, int shade, int color) {
		this.num = num;
		this.symbol = symbol;
		this.shade = shade;
		this.color = color;
	}
	
	public String printOut() {
		String myString = new String(num + "," + symbol + "," + shade + "," + color);
		return myString;
	}
	
	int base3() {
		int x = (num-1)*(3*3*3) + (symbol-1)*(3*3)+ (shade-1)*3 + color;
		//System.out.println("ball" + x);
		return x;
	}
	
	boolean myEquals(cards A) {
		if (this.num == A.num &&
			this.symbol == A.symbol &&
			this.shade == A.shade &&
			this.color == A.color)
			return true;
		else
			return false;
	}
	
	/**
	 * Convert a given vector of cards to an array of integer values from 1-81 
	 * 
	 * @param cardVector Vector of cards to be converted
	 * @return Array of ints corresponding to each card
	 */
	public static int[] convertToInt(Vector<cards> cardVector) {
		int size = cardVector.size();
		int[] intVector = new int[size];
		
		for (int i = 0; i < size; ++i) {
			intVector[i] = cardVector.get(i).base3();
		}
		
		return intVector;
	}
	
	/**
	 * Convert given int into a card
	 * 
	 * @param x int from 1-81 that will turn to a card
	 * @return card corresponding to int value
	 */
	public static cards base10(int x) {
		x = x-1;
		int[] arr = new int[4];
		
		arr[0] = x/27 + 1;
		arr[1] = (x/9)%3 + 1;
		arr[2] = (x/3)%3 + 1;
		arr[3] = x%3 + 1;
		
		cards newCard = new cards(arr[0], arr[1], arr[2], arr[3]);
		
		//System.out.println("ballin" + arr[0] + arr[1]+ arr[2]+ arr[3]);
		return newCard;
	}
	
	public static Vector<cards> convertToCards(int[] cardInt) {
		int size = cardInt.length;
		Vector<cards> cardVector = new Vector<cards>();
		
		for (int i = 0; i < size; ++i) {
			cardVector.add(base10(cardInt[i]));
		}
		
		return cardVector;
	}
}

