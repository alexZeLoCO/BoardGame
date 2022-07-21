package Items;

import java.util.Map;
import java.util.TreeMap;

import Utils.Menu;
import Utils.Player;

/**
 * Bag class
 * 
 * A Bag is a list of Items indexed by their Code.
 * A Bag has an owner to be used as a reference.
 * A Bag keeps track of all Items.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class Bag {
	
	private Player owner; // Owner of the bag
	private Map<Integer, Item> data; // Code, (Buyable Existences)
	private Menu selection; // Item selection menu

	/**
	 * Creates a new bag
	 * @param owner
	 */
	public Bag (Player owner) {
		this.owner = owner;
		this.data = new TreeMap<Integer, Item>();
		this.selection = new Menu("Choose an item", "$>");
	}
	
	/**
	 * Adds an item to the inventory
	 * 
	 * @param b Buyabe to be added
	 */
	public void addItem (Buyable b) { 
		if (this.data.containsKey(b)) {
			this.data.get(b).add();
		} else {
			this.data.put(b.getCode(), new Item(b));
			this.selection.add(b.getCode(), b.getDesc());
		}
	}
	
	/**
	 * Removes an Item from the existences.
	 * If there are no more left after removing, the entry will be removed as well.
	 * 
	 * @param b Buyable to be removed
	 */
	public void removeItem (Buyable b) {
		this.data.get(b.getCode()).remove();
		if (this.data.get(b.getCode()).hasLeft()) {
			this.data.remove(b.getCode());
			this.selection.remove(b.getCode());
		}
	}
	
	/**
	 * Shows the items list. Prompts the user for an item.
	 * Uses the selected item.
	 * 
	 * @param target Player, the item will be used against this player.
	 */
	public void use (Object target) {
		this.data.get(this.selection.runSelection()).b.use(target);
	}
	
	/**
	 * Pair Buyable & Existences
	 * 
	 * @author Alejandro Rodriguez Lopez
	 *
	 */
	private final class Item { 
		private Buyable b; // Buyable
		private int n; // Existences

		/**
		 * Creates a new Item
		 * 
		 * @param b Buyable
		 */
		public Item (Buyable b) {
			this.b = b;
			this.n = 1;
		}
		
		/**
		 * Adds one item
		 */
		public void add () {
			this.n++;
		}
		
		/**
		 * Removes one item
		 */
		public void remove () {
			this.n--;
		}
		
		/**
		 * Returns true if there is at least one item left
		 * 
		 * @return true if there is at least one item left
		 */
		public boolean hasLeft () {
			return this.n > 0;
		}
	}
}
