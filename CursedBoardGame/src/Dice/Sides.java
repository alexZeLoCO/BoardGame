package Dice;

/**
 * Sides Enum
 * 
 * Represents all the Sides that a Die could have.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public enum Sides {
	THREE	(3),
	SIX		(6),
	EIGHT	(8),
	TEN 	(10),
	TWELVE  (12),
	TWENTY  (20);
	 
	public final int value; // Number of sides
	
	/**
	 * Creates a new Sides Enum
	 * 
	 * @param value Number of sides
	 */
	private Sides (int value) {
		this.value = value;
	}
	
	/**
	 * Returns the number of sides of this enum
	 * 
	 * @return Number of sides of this enum
	 */
	public int getValue() {
		return this.value;
	}
}
