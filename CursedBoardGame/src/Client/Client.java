package Client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import Utils.Menu;
import Cards.Deck;

import Common.CursedBoardGame;
import lib.ChannelException;
import lib.CommClient;
import lib.ProtocolMessages;

/**
 * Client Class.
 * 
 * This class shall be run by the clients.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class Client {

	/**
	 * Canal de comunicación del cliente. La comunicación con el
	 * servidor se establece al crear este objeto.
	 */
	private static CommClient com; // canal de comunicación del cliente

	// Input system
	private static Scanner scanner = new Scanner(System.in);
	
	private static Menu m = new Menu ("Play menu", "//>");

	/**
	 * Checks if the game has started.
	 * 
	 * @return True if the game started.
	 * @throws IOException
	 * @throws ChannelException
	 */
	public static boolean start () throws IOException, ChannelException {
		boolean start = false;
		com.sendEvent(new ProtocolMessages("startGame"));
		try {
			start = (boolean) com.processReply(com.waitReply());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return start;
	}
	
	/**
	 * Returns 1 if it is this Client's turn.
	 * 
	 * @return 1 if it is this Client's turn. 0 if it is not.
	 * @throws IOException
	 * @throws ChannelException
	 */
	public static int myTurn () throws IOException, ChannelException {
		int out = 0;
		com.sendEvent(new ProtocolMessages("myTurn"));
		try {
			out = (int) com.processReply(com.waitReply());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	/**
	 * Retuns the current position on the board of this Client.
	 * 
	 * @return Current position.
	 * @throws IOException
	 * @throws ChannelException
	 */
	public static int position () throws IOException, ChannelException {
		int pos = 0;
		com.sendEvent(new ProtocolMessages("position"));
		try {
			pos = (int) com.processReply(com.waitReply());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pos;
	}
	
	/**
	 * Returns your current amount of money
	 * 
	 * @return Current amount of money
	 * @throws IOException
	 * @throws ChannelException
	 */
	public static double money () throws IOException, ChannelException {
		com.sendEvent(new ProtocolMessages("money"));
		double d = 0;
		try {
			d = (double) com.processReply(com.waitReply());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}
	
	/**
	 * Shows the state of the game.
	 * 
	 * @throws IOException
	 * @throws ChannelException
	 */
	public static void show () throws IOException, ChannelException {
		com.sendEvent(new ProtocolMessages("show"));
		try {
			System.out.println(com.processReply(com.waitReply()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Rolls the die, moves and draws a card.
	 * 
	 * @throws IOException
	 * @throws ChannelException
	 */
	public static void play () throws IOException, ChannelException {
		com.sendEvent(new ProtocolMessages("play"));
		try {
			Object o = (Object) com.processReply(com.waitReply());
			System.out.printf("%s\n\n", o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Buys an item.
	 * 
	 * @throws IOException
	 * @throws ChannelException
	 */
	public static void buy () throws IOException, ChannelException {
		System.out.printf("%s\n//> ",Deck.getInstance().cardsUnder(money()));
		com.sendEvent(new ProtocolMessages("buy", scanner.nextLine()));
	}

	/**
	 * Sets the current player's name.
	 * 
	 * @throws IOException
	 * @throws ChannelException
	 */
	public static void setName () throws IOException, ChannelException {
		System.out.print("Name: ");
		com.sendEvent(new ProtocolMessages("setName", scanner.nextLine()));
	}
	
	/**
	 * Sets the number of needed players to start a game if the lobby is empty.
	 * 
	 * @throws IOException
	 * @throws ChannelException
	 */
	public static void setPlayers ()  throws IOException, ChannelException {
		com.sendEvent(new ProtocolMessages("isEmptyLobby"));
		try {
			if ((boolean) com.processReply(com.waitReply())) {
				System.out.print("Enter lobby size: ");
				com.sendEvent(new ProtocolMessages("setLobbySize", Integer.parseInt(scanner.nextLine())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if this player has won
	 * 
	 * @return True if this player has won
	 */
	public static boolean win () throws IOException, ChannelException {
		com.sendEvent(new ProtocolMessages("win"));
		try {
			return (boolean) com.processReply(com.waitReply());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void makeMenu () {
		m.add(0, "play");
		m.add(1, "buy");
	}

	public static void main(String[] args) {

		try {
			// establecer la comunicación con el servidor
			// crear el canal de comunicación y establecer la
			// conexión con el servicio por defecto en localhost

			if (args.length < 2) {
				com = new CommClient();
			} else {
				com = new CommClient(args[0], Integer.parseInt(args[1]));
			}

			// activa el registro de mensajes del cliente
			com.activateMessageLog(); // opcional
		} catch (UnknownHostException e) {
			System.err.printf("Unknown server. %s\n", e.getMessage());
			System.exit(-1); // salida con error
		} catch (IOException | ChannelException e) {
			System.err.printf("Error: %s\n", e.getMessage());
			System.exit(-1); // salida con error
		}

		try {

			makeMenu();
			setPlayers();
			setName();
			
			// si es posible (oponente disponible), comenzar el juego
			while (!start()) {
				System.out.println("Waiting for players");
				Thread.sleep(5000);
			}

			int n = myTurn();
			while (n != CursedBoardGame.END_GAME) {

				do {
					n = myTurn();
					show();
					Thread.sleep(2000);
				} while (n == 0); // esperando el turno

				switch(m.runSelectionIdx()) {
					case (1): buy(); break;
				}

				System.out.println("Enter to roll the dice!");
				scanner.nextLine();
				play();

			}

			if (n != CursedBoardGame.END_GAME) { // se abandona el juego
				System.out.printf("\nYou left the game!\n");
			} else { // el juego ha terminado
				System.out.printf("\nGame ended: %s\n",
						win() ? "You win" : "You lose");
			}
		} catch (IOException | ChannelException e) {
			System.err.printf("Error: %s\n", e.getMessage());
		} catch (Exception e) { // excepción del servicio
			System.err.printf("Error: %s\n", e.getMessage());
		} finally {
			// cerrar la entrada
			scanner.close();
			// cerrar el canal de comunicación y
			// desconectar el cliente
			com.disconnect();
		}

	} // main

}