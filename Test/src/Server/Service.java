package Server;

import Util.Menu;

public class Service {

	private volatile static Object mutex = new Object(); // Exclusion
	private volatile static int users = 0; // Number of users
	private volatile static Menu m = new Menu ("Test", "->"); // Menu FIXME
	private boolean turn = false; // True if client 1

	/**
	 * Creates a new Service
	 * 
	 * @param idClient
	 */
	public Service (int idClient) {
		synchronized (mutex) {
			if (users++ == 0) {
				this.turn = true;
			}
			m.add(idClient, String.format("User_%d", idClient));
		}
		System.out.printf("ADDED ENTRY %d -> User_%d TO MENU\n", idClient, idClient);
	}
	
	/**
	 * Used to wait for client 2
	 * 
	 * @return True when there are 2 clients or more
	 */
	public boolean ready () {
		return users > 1;
	}

	/**
	 * Used to trap client 2
	 * 
	 * @return True if this is client 1
	 */
	public boolean turn () {
		return turn;
	}

	/**
	 * Used for context.
	 * The menu is printed here from the Service's perspective
	 * 
	 * @return Information string
	 */
	public String one () {
		System.out.printf("\n//////////////////////////////\nMENU:\n%s\n//////////////////////////////\n", m.toString());
		return "Information needed. Select option.";
	}
	
	/**
	 * Used for context.
	 * 
	 * @param d Option selected
	 * @return Information string
	 */
	public String two (int d) {
		return "Option chossen " + d;
	}
	
	/**
	 * Runs the menu. Allows the user to choose one of the clients.
	 * 
	 * @return Index of the client selected
	 */
	public static int runSelection () {
		return m.runSelection();
	}
	
	/**
	 * Closes the Service
	 */
	public void close () {
		m.clear();
	}
}
