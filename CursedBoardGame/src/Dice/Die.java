package Dice;

import java.util.Random;

/**
 * Die Class
 * 
 * Represents a Die.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class Die {

	private final Random randomGenerator = new Random(System.currentTimeMillis()); // Random utility

	private final Sides sides; // Sides of this Die
	private boolean cursed; // True if this Die is cursed
	
	/**
	 * Creates a new Die with 6 sides.
	 */
	public Die () {
		this(Sides.SIX, false);
	}
	
	/**
	 * Creates a new uncursed Die.
	 * 
	 * @param s Number of sides.
	 */
	public Die (Sides s) {
		this(s, false);
	}

	/**
	 * Creates a new Die.
	 * 
	 * @param sides Number of sides.
	 * @param cursed True if the die is cursed.
	 */
	public Die (Sides s, boolean cursed) {
		this.sides = s;
		this.cursed = cursed;
	}

	/**
	 * Returns the number of sides of this die in enum form.
	 * @return Number of sides.
	 */
	public Sides getSidesEnum () {
		return this.sides;
	}

	/**
	 * Returns the number of sides of this Dice.
	 * 
	 * @return Number of sides.
	 */
	public int getSides () {
		return this.sides.getValue();
	}
	
	/**
	 * Returns true if the die is cursed.
	 * 
	 * @return True if cursed.
	 */
	public boolean isCursed () {
		return this.cursed;
	}

	/**
	 * Rolls this Dice.
	 * 
	 * @return Face that landed upwards.
	 */
	public int roll () {
		return this.randomGenerator.nextInt(this.getSides()) + 1;
	}
		
}
