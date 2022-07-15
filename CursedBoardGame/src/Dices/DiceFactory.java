package Dices;

public class DiceFactory {

	private static Dice getDice (Sides s) {
		switch (s) {
			case THREE:  return new Dice(3);
			case SIX: 	 return new Dice();
			case EIGHT:  return new Dice(8);
			case TEN: 	 return new Dice(10);
			case TWELVE: return new Dice(12);
			case TWENTY: return new Dice(20);
		}
		return new Dice();
	}
	
	private static Dice getCursedDice (Sides s) {
		switch (s) {
			case THREE:  return new CursedDice(3);
			case SIX: 	 return new CursedDice();
			case EIGHT:  return new CursedDice(8);
			case TEN: 	 return new CursedDice(10);
			case TWELVE: return new CursedDice(12);
			case TWENTY: return new CursedDice(20);
		}
		return new Dice();
	}
	
	public static Dice getDice (Sides s, boolean cursed) {
		return cursed ? DiceFactory.getCursedDice(s) : DiceFactory.getDice(s);
	}

}
