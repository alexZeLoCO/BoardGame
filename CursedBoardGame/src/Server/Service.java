package Server;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import Dice.CursedDie;
import Utils.Player;
import Utils.Menu;
import Common.AccionNoPermitida;
import Common.CursedBoardGame;

enum State {
	SETTING_NAME (0),
	MATCHMAKING (1),
	STANDBY (2),
	PLAYING (3),
	END_GAME (10);
	
	private int v;

	private State (int v) {
		this.v = v;
	}
	
	public int getValue () {
		return this.v;
	}
}

public class Service implements CursedBoardGame {

	private static int NEEDED_PLAYERS;
	private volatile static Map<Integer, Player> players = new HashMap<Integer, Player>();
	private volatile static Map<Integer, Boolean> turn = new HashMap<Integer, Boolean>();
	
	private static Menu m = new Menu("Player selection", "$>");
	
	private static boolean first = true;
	private static boolean empty = true;

	private static int round = 0;
	private static String lastPlay = "";
	
	private volatile static Object mutex = new Object();

	private int idClient;
	private State state;
	
	public Service (int idClient) {
		this.idClient = idClient;
		synchronized (mutex) {
			turn.put(idClient, false);
		}
		this.state = State.SETTING_NAME;
	}
	
	/**
	 * Returns true if the lobby is empty.
	 * 
	 * @return True when the lobby is empty
	 */
	public boolean isEmptyLobby () {
		return empty;
	}
	
	/**
	 * Returns the player list
	 * 
	 * @return Player list
	 */
	public static Map<Integer, Player> getPlayers () {
		return players;
	}
	
	/**
	 * Sets the needed players to start a game.
	 * 
	 * @param nPlayers Number of players needed to start a game.
	 */
	public void setLobbySize (int nPlayers) {
		if (this.isEmptyLobby()) {
			NEEDED_PLAYERS = Math.max(2, nPlayers);
		}
		empty = false;
	}

	/**
	 * Sets the name of this player.
	 * 
	 * @param name This player's name.
	 */
	public void setName (String name) {
		players.put(this.idClient, new Player(name.isBlank() ? String.format("Player %d", this.idClient) : name));
		m.add(this.idClient, players.get(this.idClient).getName());
		this.state = State.MATCHMAKING;
	}

	@Override
	public boolean startGame () throws AccionNoPermitida {
		if (this.state != State.MATCHMAKING) {
			throw new AccionNoPermitida ("start game");
		}
		if (!first) {
			this.state = State.STANDBY;
			turn.put(this.idClient, false);
		}
		synchronized (mutex) {
			if (first && players.size() == NEEDED_PLAYERS) {
				first = false;
				this.state = State.PLAYING;
				turn.put(this.idClient, true);
			} 
		}
		System.out.printf("%d %s (%d/%d)\n", this.idClient, this.state.toString(), players.size(), NEEDED_PLAYERS);
		return this.state.getValue() > 1; // Higher than State.MATCHMAKING
	}
	
	@Override
	public int myTurn () throws AccionNoPermitida {
		if (this.state == State.SETTING_NAME) {
			throw new AccionNoPermitida("my turn");
		}
		if (players.size() == 1) {
			this.state = State.END_GAME;
			System.out.println(this.state);
			return this.state.getValue();
		}
		for (Player p : players.values()) {
			if (p.getPosition() >= CELLS) {
				this.state = State.END_GAME;
				return this.state.getValue();
			}
		}
		if (this.state == State.STANDBY) {
			synchronized (mutex) {
				if (turn.get(this.idClient)) {
					this.state = State.PLAYING;
				}
			}
		}
		return this.state == State.PLAYING ? 1 : 0;
	}
	
	/**
	 * Updates the state of the game just after a play.
	 */
	private void updateState () {
		this.state = State.STANDBY;
		synchronized (mutex) {
			turn.put(this.idClient, false);
			turn.put(round++%(players.size())+1, true);
			System.out.printf("U: %s\n", turn.toString());
		}
	}
	
	@Override
	public String play () {
		lastPlay = players.get(this.idClient).play();
		updateState();
		return lastPlay;
	}
	
	/**
	 * String representation of the current state of the game
	 * 
	 * @return String representation of the current state of the game
	 */
	public String show () {
		Player p;
		String s = "";
		for (int i = 0 ; i < 20 ; i++) {
			s+="-";
		}
		s += String.format("\n%s\n\t%10s\t-\t%10s\t-\tPosition\n", lastPlay, "Name", "Die");
		Iterator<Player> itr = players.values().iterator();
		for (int i = 0 ; i < players.size() ; i++) {
			p = itr.next();
			s+=String.format("%d.\t%10s\t-\t%10s %d (x%d)\t-\t%d/%d\n", i+1, p.getName(), p.getDie() instanceof CursedDie ? "Cursed" : "Normal", p.getDie().getSides(), p.getNDice(), p.getPosition(), CELLS);
		}
		for (int i = 0 ; i < 20 ; i++) {
			s+="-";
		}
		return s;
	}

	@Override
	public void close () {
		if (players.size() <= 1) {
			empty = true;
			first = true;
			players.clear();
			turn.clear();
		} else {
			players.remove(this.idClient);
			turn.remove(this.idClient);
		}
		CursedBoardGame.super.close();
	}

	@Override
	public int position() throws AccionNoPermitida {
		if (this.state == State.SETTING_NAME) {
			throw new AccionNoPermitida("position");
		}
		return players.get(this.idClient).getPosition();
	}
	
	/**
	 * Returns true if this is the last player in the lobby or this player has reached the limit cells.
	 * 
	 * @return true if this player has won. 
	 */
	public boolean win () {
		try {
			return players.size() == 1 || this.position() >= CELLS;
		} catch (AccionNoPermitida e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Runs a Selection menu of all the players.
	 * This is used so a player can choose other player. (Cards, etc...)
	 * 
	 * @return Index of the player chosen.
	 */
	public static int runPlayerSelection () {
		return m.runSelection();
	}
	
	/**
	 * Switches position with player p
	 * 
	 * @param p Player to switch positions with
	 * @return String representation of the switch
	 */
	public String playerSwitch (int p) {
		players.get(this.idClient).switchPositionWith(players.get(p));
		return String.format("%s has switched positions with %s", players.get(this.idClient).getName(), players.get(p).getName());
	}
}

