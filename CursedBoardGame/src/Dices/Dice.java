package Dices;

import java.util.Random;

public class Dice {

	private final Random randomGenerator = new Random(System.currentTimeMillis());
	private final int sides;
	
	/**
	 * Creates a new Dice with 6 sides.
	 */
	public Dice () {
		this(6);
	}
	
	/**
	 * Creates a new Dice.
	 * 
	 * @param sides Number of sides.
	 */
	public Dice (int sides) {
		this.sides = sides > 0 ? sides : 6;
	}

	/**
	 * Returns the number of sides of this Dice.
	 * 
	 * @return Number of sides.
	 */
	public int getSides () {
		return this.sides;
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
