package setRules;

import java.util.*;
import gui.*;

public class player {
	public int id;
	public String name;
	public int score;
	Vector<myGameInfo> myGames;
	public Lobby myLobby;
	
	public player(String name, int id) {
		this.name = name;
		this.id = id;
		score = 0;
		myGames = new Vector<myGameInfo>();
		this.myLobby = GameScreen.myLobby;
	}
	
	public void getCard(cards A) {
		;
	}
	
	public void win(int n) {
		score += n;
	}
	
	public void lose(int n) {
		score -= n;
	}
	
	public int find(String gameName) {
		for (int i = 0; i < myGames.size(); ++ i) {
  			if (myGames.get(i).currGame.id.equals(gameName)){ 
  				return i;
  			}
  		}
		
		return -1;
	}
	
	public void addGame(game currGame, CardLayout currLayout, int score) {
		myGames.add(new myGameInfo(currGame, currLayout, score));
	}
	
	public void removeGame(game currGame) {
		for (int i = 0; i < myGames.size(); ++i) {
			if (myGames.get(i).currGame.id.equals(currGame.id)) {
				myGames.remove(i);
				break;
			}
		}
	}
	
	public game getGame(int i) {
		return myGames.get(i).currGame;
	}
	
	public CardLayout getLayout(int i) {
		return myGames.get(i).currLayout;
	}
}

class myGameInfo {
	game currGame;
	int score;
	CardLayout currLayout;
	
	myGameInfo(game currGame, CardLayout currLayout, int score) {
		this.currGame = currGame;
		this.score = score;
		this.currLayout = currLayout;
	}
}