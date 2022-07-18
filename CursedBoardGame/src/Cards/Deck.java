package Cards;

import java.util.ArrayList;
import java.util.Random;

import Dice.Sides;
import Dice.DiceFactory;

/**
 * Class Deck. (Singleton)
 * 
 * A Deck is a list of Cards.
 * A Deck keeps track of how many instances of each Card are left.
 * A Deck contains a Random object to choose a random Card.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class Deck {
	
	// Number of each type of card in the deck.
	private final int N_CARDS;

	private static Deck instance;
	
	private ArrayList<Card> deck; // Cards of the deck
	private ArrayList<Integer> left; // Number of each card in the deck
	private final Random generator = new Random(System.currentTimeMillis()); // Random utility
	
	private int totalCards; // Total number of cards
	
	/**
	 * Creates a new empty deck.
	 */
	private Deck () {
		this(2);
	}
	
	/**
	 * Creates a new empty deck.
	 * 
	 * @param nCards Number of cards for each instance.
	 */
	private Deck (int nCards) {
		this.N_CARDS = nCards;
		this.totalCards = 0;
		this.deck = new ArrayList<Card>();
		this.left = new ArrayList<Integer>();
		this.makeDeck();
	}
	
	/**
	 * Gets the deck
	 * 
	 * @return Deck
	 */
	public static Deck getInstance () {
		if (instance == null) {
			instance = new Deck();
		}
		return instance;
	}

	/**
	 * Returns the number of unique cards in the deck.
	 * It does not matter whether there are any left.
	 * 
	 * @return Number of unique cards in the deck.
	 */
	private int cards () {
		return this.deck.size();
	}

	/**
	 * Returns the actual number of cards in the deck.
	 * 
	 * @return Number of cards left in the deck.
	 */
	public int totalCards () {
		return totalCards;
	}

	/**
	 * Adds a card to the deck
	 * 
	 * @param c Card to be added
	 * @return True if added correctly.
	 */
	public boolean add (Card c) {
		if (this.deck.contains(c)) {
			return false;
		}
		this.totalCards+=N_CARDS;
		this.left.add(N_CARDS);
		return this.deck.add(c);
	}

	/**
	 * Gets a card from the deck.
	 * 
	 * @return Card from the deck.
	 */
	public Card drawCard () {
		int idx;
		do {
			idx = this.generator.nextInt(this.cards());
		} while (left.get(idx) == 0);
		left.set(idx, left.get(idx)-1);
		if (--totalCards == 0) {
			this.shuffle();
		}
		return deck.get(idx);
	}
	
	/**
	 * Adds all the Cards that are related to dice.
	 */
	public void makeDieCards () {
		for (Sides s : Sides.values()) {
			this.add(new Card(String.format("You now use a normal %s-sided die", s.toString()), (x) -> x.setDie(DiceFactory.getDie(s, false))));
		}
		for (Sides s : Sides.values())  {
			this.add(new Card(String.format("You now use a cursed %s-sided die", s.toString()), (x) -> x.setDie(DiceFactory.getDie(s, true))));
		}
		this.add(new Card("Your die has been reset", (x) -> x.setDie(DiceFactory.getDie(Sides.SIX, false))));
		this.add(new Card("Your die is now cursed", (x) -> x.setDie(DiceFactory.curseSwitch(x.getDie()))));
	}

	/**
	 * Adds all the Cards.
	 */
	public void makeDeck () {
		this.makeDieCards();
		this.add(new Card("Go forward 10 positions", (x) -> x.move(10)));
		this.add(new Card("Go backwards 10 positions", (x) -> x.move(-10)));
		this.add(new Card("You go back to the start", (x) -> x.move(x.getPosition()*(-1))));
	}

	/**
	 * Resets the number of cards.
	 */
	private void shuffle () {
		System.out.println("Deck has run out of cards! Shuffling!");
		for (int i = 0 ; i < this.cards(); i++) {
			this.left.add(i, N_CARDS);
		}
	}
}
