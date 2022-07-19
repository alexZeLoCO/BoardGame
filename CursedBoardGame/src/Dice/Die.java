package Dice;

import java.util.Random;

/**
 * Die Abstract Class
 * 
 * Represents a Die.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class Die {

	private final Random randomGenerator = new Random(System.currentTimeMillis()); // Random utility

	private Sides sides; // Sides of this Die
	
	/**
	 * Creates a new Die with 6 sides.
	 */
	public Die () {
		this(Sides.SIX);
	}
	
	/**
	 * Creates a new uncursed Die.
	 * 
	 * @param s Number of sides.
	 */
	public Die (Sides s) {
		this.setSides(s);
	}

	/**
	 * Sets the number of sides in this Die
	 * 
	 * @param s Number of sides
	 */
	protected void setSides (Sides s) {
		this.sides = s;
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
	 * Rolls this Dice.
	 * 
	 * @return Face that landed upwards.
	 */
	public int roll () {
		return this.randomGenerator.nextInt(1, this.getSides()+1);
	}
		
}
