package Utils;

import java.util.function.Consumer;

public class Card {

	private final String description;
	private final Consumer<Player> action;
	
	public Card (String description, Consumer<Player> action) {
		this.description = description;
		this.action = action;
	}
	
	@Override
	public String toString () {
		return String.format("%s", this.description);
	}

	public void accept (Player victim) {
		System.out.printf("%s drew the card \"%s\"\n", victim.toString(), this.description);
		this.action.accept(victim);
	}
}
