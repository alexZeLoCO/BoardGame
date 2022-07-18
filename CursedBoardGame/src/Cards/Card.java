package Cards;

import java.util.function.Consumer;

import Utils.Player;

/**
 * Card class.
 * A Card is drawn by the player each round after moving.
 * 
 * A Card has a description of what it does.
 * A Card has a lambda function that will accept the player.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class Card {

	/**
	 * Description of what the card does.
	 */
	private final String description;
	
	/**
	 * Action of this card.
	 */
	private final Consumer<Player> action;
	
	/**
	 * Creates a new card.
	 * @param description Description of the card.
	 * @param action Action to be performed when the card is drew.
	 */
	public Card (String description, Consumer<Player> action) {
		this.description = description;
		this.action = action;
	}

	/**
	 * Returns this card's description.
	 * 
	 * @return Description of this card.
	 */
	public String getDescription () {
		return this.description;
	}

	/**
	 * Performs the action when a player draws this card.
	 * @param victim Player who drew.
	 */
	public String accept (Player victim) {
		System.out.printf("%s drew the card \"%s\"\n", victim.toString(), this.description);
		this.action.accept(victim);
		return String.format("%s drew the card \"%s\"\n", victim.toString(), this.description);
	}

	@Override
	public String toString () {
		return String.format("%s", this.description);
	}
	
}
