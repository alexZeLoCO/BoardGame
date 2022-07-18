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
	
	/**
	 * Creates a new player with a name.
	 * @param name Name of the player.
	 */
	public Player (String name) {
		this.name = name;
		this.currentDie = new Die();
	}
	
	/**
	 * Returns the position of the player.
	 * @return Position of the player.
	 */
	public int getPosition() {
		return this.position;
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
	 * Rolls this player's die.
	 * 
	 * @return Face of the die that ended upwards.
	 */
	private int roll () {
		return this.currentDie.roll();
	}
	
	/**
	 * Plays a turn of this player.
	 */
	public String play () {
		String out = "";
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
	
	@Override
	public String toString () {
		return String.format("Player %s at %d (Die: %d)", this.name, this.getPosition(), this.currentDie.getSides());
	}
}
