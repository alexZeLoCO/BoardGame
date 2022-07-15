package Utils;

import Dices.DiceFactory;
import Dices.Dice;
import Dices.Sides;

public class Player {

	private int position;
	private final String name;
	private Dice currentDice;
	
	public Player (String name) {
		this.name = name;
		this.currentDice = DiceFactory.getDice(Sides.SIX, false);
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public void setDice (Dice d) {
		this.currentDice = d;
	}
	
	private int roll () {
		return this.currentDice.roll();
	}
	
	public void play () {
		this.move(this.roll());
		// TODO: Get random card from the bullsh*t deck
	}

	public void move (int n) {
		this.position+=n;
	}
	
	public String toString () {
		return String.format("Player %s at %d", this.name, this.getPosition());
	}
}
