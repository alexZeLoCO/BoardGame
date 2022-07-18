package Dice;

/**
 * CursedDie Class
 * 
 * Represents a Die that is cursed.
 * This type of Die will deal negative values from the half onwards.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class CursedDie extends Die {
	
	/**
	 * Makes a new 6-sided cursed die.
	 */
	public CursedDie () {
		this(Sides.SIX);
	}
	
	/**
	 * Makes a new cursed die.
	 * 
	 * @param s Number of sides
	 */
	public CursedDie (Sides s) {
		super(s, false);
	}
	
	@Override
	public int roll () {
		int out = super.roll();
		return out <= this.getSides()/2 ? out : (-1)*out;
	}
}
