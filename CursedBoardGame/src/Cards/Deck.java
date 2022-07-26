package Cards;

import java.util.ArrayList;
import java.util.Random;

import Dice.Sides;
import Server.Service;
import Utils.Player;
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
		this(1);
	}
	
	/**
	 * Creates a new empty deck.
	 * 
	 * @param nCards Number of cards for each instance.
	 */
	private Deck (int nCards) {
		this.N_CARDS = Math.max(1, Math.abs(nCards));
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
	private void makeDieCards () {
		for (Sides s : Sides.values()) {
			this.add(new Card(String.format("You now use a normal %s-sided die", s.toString()), (x) -> x.setDie(DiceFactory.getDie(s, false)), 500));
		}
		for (Sides s : Sides.values())  {
			this.add(new Card(String.format("You now use a cursed %s-sided die", s.toString()), (x) -> x.setDie(DiceFactory.getDie(s, true)), 500));
		}
		this.add(new Card("Your die has been reset", (x) -> x.setDie(DiceFactory.getDie(Sides.SIX, false)), 500));
		this.add(new Card("Your die is now cursed/normal", (x) -> x.setDie(DiceFactory.curseSwitch(x.getDie())), 500));
		this.add(new Card("You now have no die", (x) -> x.setNDice(0), 750));
		this.add(new Card("You now have 1 die", (x) -> x.setNDice(1), 750));
		for (int i = 2 ; i < 5 ; i++) {
			int a = i;
			this.add(new Card(String.format("You now have %d dice", i), (x) -> x.setNDice(a), 750));
		}
	}

	/**
	 * Adds all the Cards that are related to positions.
	 */
	private void makePositionCards () {
		this.add(new Card("Go forward 10 positions", (x) -> x.move(10), 200));
		this.add(new Card("Go backwards 10 positions", (x) -> x.move(-10), 200));
		this.add(new Card("You go back to the start", (x) -> x.move(x.getPosition()*(-1)), 1000));
		this.add(new Card("Everybody goes back to the start", (x) -> Service.getPlayers().values().forEach((y) -> y.move(y.getPosition()*(-1))), 1000));
		this.add(new Card("You switch position with a random player", (x) -> x.switchPositionWith((Player) Service.getPlayers().values().toArray()[generator.nextInt(Service.getPlayers().size())]), 500));
	}
	
	/**
	 * Adds all the Cards that are related to turns.
	 */
	private void makeTurnCards () {
		for (int i = 1 ; i < 11 ; i++) {
			int a = i;
			this.add(new Card("You now lose " + i + " turns", (x) -> x.setSkipTurns(a), 150));
		}
	}

    /**
     * Adds all the Cards that are related to money.
     */
    private void makeMoneyCards () {
        for (int i = 0 ; i < 100 ; i++) {
            double d = function(generator.nextDouble());
            this.add(new Card(String.format("You earn %.2f$", d), (x) -> x.earn(Double.parseDouble(String.format("%.2f", d))), 500));
        }
        this.add(new Card(String.format("You lose all your money."), (x) -> x.earn(x.getMoney()*(-1)), 500));
    }

	/**
	 * Adds all the Cards.
	 */
	public void makeDeck () {
		this.makeDieCards();
		this.makePositionCards();
		this.makeTurnCards();
        this.makeMoneyCards();
	}

    /**
     * Returns the money acquired
     *
     * @param d Percentage [0, 1]
     * @return $
     */
    private double function (double d) {
        return Math.pow(d, Math.pow(Math.E, d+1))*50; 
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
