package Dices;

public class CursedDice extends Dice {
	public CursedDice () {
		this(6);
	}
	
	public CursedDice (int sides) {
		super(sides);
	}
	
	@Override
	public int roll () {
		int out = super.roll();
		return out <= this.getSides()/2 ? out : (-1)*out;
	}
}
