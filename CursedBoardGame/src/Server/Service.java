package Server;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import Utils.Player;
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

	private static final int NEEDED_PLAYERS = 2;
	private volatile static Map<Integer, Player> players = new HashMap<Integer, Player>();
	private volatile static Map<Integer, Boolean> turn = new HashMap<Integer, Boolean>();
	
	private static boolean first = true;

	private static int round = 0;
	private static String lastPlay = "";
	
	private volatile static Object mutex = new Object();

	private int idClient;
	private State state;
	
	public Service (int idClient) {
		this.idClient = idClient;
		synchronized (mutex) {
			players.put(idClient, new Player(String.format("Player%d", idClient)));
			turn.put(idClient, false);
		}
		this.state = State.SETTING_NAME;
	}
	
	/**
	 * Sets the name of this player.
	 * 
	 * @param name This player's name.
	 */
	public void setName (String name) {
		players.put(this.idClient, new Player(name));
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
		System.out.println(this.idClient + " " + this.state);
		return this.state.getValue() > 0;
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
		s += String.format("\n%s\n\tName\t-\tDie\t-\tPosition\n", lastPlay);
		Iterator<Player> itr = players.values().iterator();
		for (int i = 0 ; i < players.size() ; i++) {
			p = itr.next();
			s+=String.format("%d.\t%s\t-\t%s %d\t-\t%d/%d\n", i+1, p.getName(), p.getDie().isCursed() ? "Cursed" : "Normal", p.getDie().getSides(), p.getPosition(), CELLS);
		}
		for (int i = 0 ; i < 20 ; i++) {
			s+="-";
		}
		return s;
	}

	@Override
	public void close () {
		if (players.size() <= 1) {
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
}

