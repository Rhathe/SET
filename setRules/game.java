package setRules;

import java.util.*;

public class game {
	
	public String id;
	/**
	 * Deck of cards consisting of 81 unique cards
	 */
	public Vector<cards> deck;
	/**
	 * The viewable cards that are to be selected when calling a Set
	 */
	public Vector<cards> board;
	
	/**
	 * Creates the 81-card deck and deals out the initial 12-card board
	 */
	public game(String id) {
		this.id = id;
		createDeck();
		createBoard();
	}

	/**
	 * Creates the cards with all values for the 81-card deck
	 * 
	 * Creates a new Vector of cards assigned to the variable deck. It then does 4 nested loops, 
	 * each going from 1 to 3, creating new cards and filling values for the categories of number, color, 
	 * symbol, and shade. It adds these new unique cards to the deck.
	 * 
	 * 
	 */
	private void createDeck() {
		deck = new Vector<cards>();
        
        for (int i = 1; i < 4; ++i) {
        	for (int j = 1; j < 4; ++j) {
        		for (int k = 1; k < 4; ++k) {
        			for (int l = 1; l < 4; ++l) {
        				deck.add(new cards(i,j,k,l));
        			}
        		}
        	}
        }
	}
	
	/**
	 * Creates the initial board consisting of 12 cards
	 */
	private void createBoard() {
		board = new Vector<cards>();
        dealFromDeck(12);
        
        while(true) {
        	if (checkBoard()[0] != 4)
        		break;
        	
        	dealFromDeck(3);
        }
	}
	
	/**
	 * Deals n new cards from deck to board. If n is greater than remaining cards in deck,
	 * only the remaining cards are dealt out
	 * 
	 * @param n Number of cards to deal from deck to board
	 */
	public void dealFromDeck(int n) {
		Random rand = new Random();
		
		if (deck.size() > 0) {
			for (int i = 0; i < n; ++i) {
	        	int size = deck.size();
	        	
	        	if (size <= 0)
	        		return;
	        	
	        	int cardPosInDeck = rand.nextInt(size);
	        	cards newCard = deck.get(cardPosInDeck);
	        	deck.remove(cardPosInDeck);
	        	board.add(newCard);
	        	//System.out.println(i + ": " + newCard.printOut());
			}
		}
	}
	
	/**
	 * Removes card from board. If deck still has cards, new card from deck is placed in same position.
	 * 
	 * @param removedCard card to be removed from board
	 */
	private void replaceCard(cards removedCard) {
		
		int cardPosInBoard = 0;
		
		for (int i = 0; i < board.size(); ++i) {
			if (removedCard.myEquals(board.get(i))) {
				cardPosInBoard = i;
				break;
			}
		}

		board.remove(cardPosInBoard);
		
		if (deck.size() > 0 && board.size() < 12) {
			Random rand = new Random();
			int size = deck.size();
			int cardPosInDeck = rand.nextInt(size);
			cards newCard = deck.get(cardPosInDeck);
			deck.remove(cardPosInDeck);
			board.insertElementAt(newCard, cardPosInBoard); 
		}
		
	}
	
	/**
	 * Removes the 3 given cards from the board and replaces in the same positions
	 * with cards from the deck if cards still remain in deck.
	 * 
	 * @param A first card to be replaced
	 * @param B second card to be replaced
	 * @param C third card to be replaced
	 */
	private void replaceSet(cards A, cards B, cards C) {
		replaceCard(A);
		replaceCard(B);
		replaceCard(C);
	}
	
	public int sendToServer(cards A, cards B, cards C) {
		player guy = new player("Guy Fox", 1);
		return callSet(guy,A,B,C);
	}
	
	/**
	 * Checks to see if cards given by certain player is a Set, rewards/punishes accordingly.
	 * If cards are not on board, do nothing
	 * 
	 * @param guy player calling the Set
	 * @param A first card
	 * @param B second card
	 * @param C third card
	 */
	public int callSet(player guy, cards A, cards B, cards C) {
		
		int test1 = checkIfOnBoard(A); 
		int test2 = checkIfOnBoard(B); 
		int test3 = checkIfOnBoard(C);
		
		//System.out.println(A.printOut() + ":" + test1 + "," + B.printOut() + ":" + test2 + "," + C.printOut() + ":" + test3);
		
		if (test1 != 1 || test2 != 1 || test3 != 1) {
			System.out.println("NOT ON BOARD!");
			return -1;
		}
		
		if (check(A,B,C) == 1) {
			replaceSet(A,B,C);
			guy.win(1);
			System.out.println("You WIN!");
			
			while(true) {
				if (checkBoard()[0] != 4)
					break;
				
				else if (deck.size() <= 0) {
					System.out.println("GAME OVER");
					return 1;
				}
					
				dealFromDeck(3);
			}
			return 1;
		}
		else {
			guy.lose(1);
			System.out.println("You LOSE!");
			return 0;
		}
			
	}
	
	/**
	 * Check if given card is on the board
	 * 
	 * @param A card to be checked
	 * @return 1 if card is on the board, 0 otherwise
	 */
	public int checkIfOnBoard(cards A) {
		for (cards test : board) {
			if (test.myEquals(A) == true) {
				return 1;
			}
		}
		
		return 0;
	}
	
	/**
	 * Checks if current board has at least one set
	 * 
	 * @return 1 if there is a Set on the bard, 0 otherwise
	 */
	public int[] checkBoard() {
		int size = board.size();
		
		for (int i = 0; i < size; ++i) {
			cards A = board.get(i);
			
			for (int j = i + 1; j < size; ++j) {
				cards B = board.get(j);
				
				for (int k = j + 1; k < size; ++k) {
					cards C = board.get(k);
					
					if (check(A,B,C) != 0) {
						int[] returnArr = {i,j,k};
						System.out.println(i+","+j+","+k);
						return returnArr;
					}
				}
			}
		}
		
		int[] failed = {4,4,4};
		return failed;
	}
	
	/**
	 * Check if given 3 cards count as a Set
	 * 
	 * @param A first card
	 * @param B second card
	 * @param C third card
	 * @return 1 if a Set, 0 otherwise
	 */
	public int check(cards A, cards B, cards C) {
		int a = checkCategory(A.num, B.num, C.num);
		int b = checkCategory(A.symbol, B.symbol, C.symbol);
		int c = checkCategory(A.color, B.color, C.color);
		int d = checkCategory(A.shade, B.shade, C.shade);
		
		if (a == 1 && b == 1 && c == 1 && d == 1)
			return 1;
		else 
			return 0;
	}
	
	/**
	 * Checks if the the numbers in given category are all the same or all different, for Set verification.
	 * 
	 * @param a value of category in first card
	 * @param b value of category in second card
	 * @param c value of category in third card
	 * @return 1 if all the same or all different, 0 otherwise
	 */
	private int checkCategory(int a, int b, int c) {
		if (a == b) {
			if (b == c)
				return 1;
			else
				return 0;
		}
		else if (b != c && a != c)
			return 1;
		else
			return 0;
	}
}
