package Common;

/**
 * CursedBoardGame Interface.
 * 
 * Represents all the actions needed for the CursedBoardGame to work.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public interface CursedBoardGame extends lib.DefaultService {
	
	public final static int CELLS = 100; // Total number of cells in the game.
										 // When a player reaches the CELLS number, they win.
	public final static int END_GAME = 10; // State code that represents that a player has won.
	
	/**
	 * True if there are opponents.
	 * 
	 * @return True if the game can start.
	 */
	boolean startGame() throws AccionNoPermitida;
	
	/**
	 * Returns 0 if the player does not have the turn.
	 * Returns 1 if the player has the turn.
	 * Returns {@code true} if the game has ended.
	 * 
	 * @return 0, 1 or {@code true}.
	 */
	int myTurn () throws AccionNoPermitida;
	
	/**
	 * Plays one round for this player.
	 * 
	 * @return Description of dealt card.
	 */
	String play () throws AccionNoPermitida;
	
	/**
	 * Returns this player's position.
	 * @return This player's position.
	 */
	int position () throws AccionNoPermitida;
	
}
