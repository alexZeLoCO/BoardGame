package Utils;

import Cards.Deck;
import Cards.Card;
import Dice.Die;

/**
 * Player Class.
 * 
 * Represents one of the players in the game.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class Player {

	private int position; // Current position of the player
	private final String name; // Name of the player
	private Die currentDie; // Current die of the player
	private int nDie; // Number of dice
	private int skipTurns; // Number of turns to skip
	private double money; // Money of the player
                          
	/**
	 * Creates a new player with a name.
	 * @param name Name of the player.
	 */
	public Player (String name) {
		this.name = name;
		this.currentDie = new Die();
		this.nDie = 1;
		this.skipTurns = 0;
        this.money = 0;
	}
	
	/**
	 * Returns the position of the player.
	 * @return Position of the player.
	 */
	public int getPosition() {
		return this.position;
	}
	
	/**
	 * Returns the number of dice this player has.
	 * 
	 * @return Number of dice this player has.
	 */
	public int getNDice () {
		return this.nDie;
	}
	
	/**
	 * Sets the number of dice this player has.
	 * 
	 * @param n Number of dice.
	 */
	public void setNDice (int n) {
		this.nDie = Math.abs(n);
	}
	
	/**
	 * Returns the number of turns left to skip.
	 * 
	 * @return Number of turns left to skip.
	 */
	public int getSkipTurns () {
		return this.skipTurns;
	}

	/**
	 * Sets the number of skips turns left.
	 * 
	 * @param s Number of skip turns.
	 */
	public void setSkipTurns (int s) {
		this.skipTurns = s;
	}

	/**
	 * Sets this player's die.
	 * @param d Die.
	 */
	public void setDie (Die d) {
		this.currentDie = d;
	}
	
	/**
	 * Returns the current die.
	 * 
	 * @return Current die.
	 */
	public Die getDie () {
		return this.currentDie;
	}

	/**
	 * Returns the player's name.
	 * 
	 * @return Player's name.
	 */
	public String getName () {
		return this.name;
	}

    /**
     * Returns the player's money.
     * 
     * @return Player's money.
     */
    public double getMoney () {
        return this.money;
    }

	/**
	 * Rolls this player's dice.
	 * 
	 * @return sum of the upwards-facing faces of this player dice.
	 */
	private int roll () {
		int s = 0;
		for (int i = 0 ; i < this.getNDice() ; i++) {
			s+=this.currentDie.roll();
		}
		return s;
	}
	
	/**
	 * Plays a turn of this player.
	 */
	public String play () {
		String out = "";
		if (this.getSkipTurns() > 0) {
			return String.format("You skip this turn!\nSkip turns left: %d\n", --this.skipTurns);
		}
		out = this.move(this.roll());
		Card c = Deck.getInstance().drawCard();
		out += String.format("%s has dealt the card: \"%s\"\n", this.getName(), c.getDescription());
		c.accept(this);
		return out;
	}

	/**
	 * Moves this player n positions.
	 * @param n Positions to be moved.
	 */
	public String move (int n) {
		String dir = "forward";
		if (n < 0) {
			n = Math.min(this.getPosition(), Math.abs(n)) * (-1);
			dir = "backwards";
		}
		System.out.printf("%s has moved %s %d positions\n", this.name, dir, n);
		this.position+=n;
		return String.format("%s has moved %s %d positions\n", this.name, dir, n);
	}

    /**
     * Gives d$ to the player.
     *
     * @param d $
     */
    public void earn (double d) {
        this.money+=d;
    }
	
	/**
	 * Switches position with a given player
	 * 
	 * @param p Player to switch positions with
	 */
	public void switchPositionWith (Player p) {
		int pos = p.getPosition();
		p.move(this.getPosition() - pos);
		this.move(pos - this.getPosition());
	}

	@Override
	public String toString () {
		return String.format("Player %s at %d (Die: %d)", this.name, this.getPosition(), this.currentDie.getSides());
	}
}
