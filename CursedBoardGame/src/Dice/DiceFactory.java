package Dice;

import java.util.Map;
import java.util.HashMap;

/**
 * DieFactory Class. (Factory Method + Singleton)
 * 
 * Represents a Factory of Dice, both normal and cursed.
 * The Dice act as Singletons stored in HashMaps.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class DiceFactory {

	private static Map<Sides, Die> normalDice = new HashMap<Sides, Die>(); // Normal Dice Singleton
	private static Map<Sides, Die> cursedDice = new HashMap<Sides, Die>(); // Cursed Dice Singleton

	/**
	 * Gets a normal Die. 
	 * 
	 * @param s Number of sides.
	 * 
	 * @returns Normal s-sided Die.
	 */
	private static Die getDie (Sides s) {
		if (!normalDice.containsKey(s)) {
			normalDice.put(s, new Die(s, false));
		}
		return normalDice.get(s);
	}
	
	/**
	 * Gets a cursed Die.
	 * 
	 * @param s Number of sides.
	 * 
	 * @return Cursed s-sided Die.
	 */
	private static Die getCursedDie (Sides s) {
		if (!cursedDice.containsKey(s)) {
			cursedDice.put(s, new Die(s, true));
		}
		return cursedDice.get(s);
	}
	
	/**
	 * Given a Die, returns the Cursed or the normal version of it depending on whether it was or not cursed already.
	 * 
	 * @param d Die.
	 * 
	 * @return Normal Die if d was cursed. Cursed Die if d was not cursed.
	 */
	public static Die curseSwitch (Die d) {
		return d.isCursed() ? DiceFactory.getDie(d.getSidesEnum()) : DiceFactory.getCursedDie(d.getSidesEnum());
	}

	/**
	 * Returns the specified Die.
	 * 
	 * @param s Number of sides.
	 * @param cursed True if cursed.
	 * 
	 * @return s-Sided die.
	 */
	public static Die getDie (Sides s, boolean cursed) {
		return cursed ? DiceFactory.getCursedDie(s) : DiceFactory.getDie(s);
	}

}
