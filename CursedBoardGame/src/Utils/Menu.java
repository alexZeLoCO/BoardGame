package Utils;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.Scanner;

/**
 * Menu Class.
 * A Menu is a Map of entries indexed by an Integer.
 * An Entry has an index and a description.
 * Optionally, an Entry can have a Lambda that will
 * be run when the option is chosen.
 * 
 * A Menu can be run to let the user choose one of the
 * Entries by inputting the Integer that indexes it.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class Menu {

	private final Scanner scanner = new Scanner (System.in); // Input

	private int size; // Number of entries
	private Map<Integer, Menu.Entry> data; // Data array (Entries)
	
	private String title; // Title of the Menu
	private final String prompt; // Prompt
	
	/**
	 * Creates a new Menu
	 * 
	 * @param title Title of the Menu
	 * @param prompt Prompt
	 */
	public Menu (String title, String prompt) {
		this.title = title;
		this.prompt = prompt;
		this.size = 0;
		this.data = new TreeMap<Integer, Menu.Entry>();
	}
	
	/**
	 * Returns the size of this Menu
	 * 
	 * @return Number of entries in this Menu
	 */
	public int size () {
		return this.size;
	}

	/**
	 * Adds an entry to this Menu
	 * 
	 * @param idx Index of the Entry
	 * @param desc Description of the Entry
	 */
	public void add (int idx, String desc) {
		this.data.put(idx, new Entry(idx, desc));
		this.size++;
	}
	
	/**
	 * Adds an entry to this Menu
	 * 
	 * @param idx Index of the Entry
	 * @param desc Description of the Entry
	 * @param act Action of the Entry
	 */
	public void add (int idx, String desc, Consumer<Object> act) {
		this.data.put(idx, new Entry(idx, desc, act));
		this.size++;
	}
	
	/**
	 * Returns the Title, Entries and prompt of this Menu
	 * 
	 * @return Title, Entries and prompt
	 */
	public String getSelection () {
		return this.toString() + this.prompt;
	}
	
	/**
	 * Runs the Menu. If the option chosen has a Lambda, it will be run.
	 * 
	 * @return Index of the option selected.
	 */
	public int runSelection () {
		System.out.printf("%s%s", this.toString(), this.prompt);
		int selection = Integer.parseInt(scanner.nextLine());
		if (this.data.get(selection).hasLambda()) {
			this.data.get(selection).accept(null);
		}
		return selection;
	}

	@Override
	public String toString () {
		String out = String.format("%s\n", this.title);
		for (Entry e : this.data.values()) {
			out+=String.format("%s\n", e.toString());
		}
		return out;
	}

	/**
	 * Entry of the Menu.
	 * 
	 * @author Alejandro Rodriguez Lopez
	 *
	 */
	private final class Entry {
		private int index; // Index of the Entry
		private String description; // Description of the Entry
		private Consumer<Object> act; // Action of the entry (Optional)
		
		/**
		 * Creates a new Entry without an action
		 * 
		 * @param idx Index of the Entry
		 * @param desc Description of the Entry
		 */
		public Entry (int idx, String desc) {
			this(idx, desc, null);
		}
		
		/**
		 * Creates a new Entry with an action
		 * 
		 * @param idx Index of the Entry
		 * @param desc Description of the Entry
		 * @param act Action of the Entry
		 */
		public Entry (int idx, String desc, Consumer<Object> act) {
			this.index = idx;
			this.description = desc;
			this.act = act;
		}
		
		/**
		 * Returns the Index of the Entry
		 * 
		 * @return Index of the Entry
		 */
		public int getIdx () {
			return this.index;
		}
		
		/**
		 * Returns the Description of the Entry
		 * 
		 * @return Description of the Entry
		 */
		public String getDesc () {
			return this.description;
		}
		
		/**
		 * Returns the Action of the Entry
		 * 
		 * @return Action of the Entry
		 */
		public Consumer<Object> getAct () {
			return this.act;
		}
		
		/**
		 * Returns True if the Entry has an action (Lambda)
		 * 
		 * @return True if the Entry has an action
		 */
		public boolean hasLambda () {
			return this.act != null;
		}
		
		/**
		 * Runs the Action of this Entry
		 * 
		 * @param o Object to be applied
		 */
		public void accept(Object o) {
			this.act.accept(o);
		}
		
		@Override
		public String toString () {
			return String.format("%d. %s", this.getIdx(), this.getDesc());
		}

	}
}
