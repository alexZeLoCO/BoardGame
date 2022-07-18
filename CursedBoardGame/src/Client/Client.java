package Client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

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
			System.out.printf("%s\n\n", com.processReply(com.waitReply()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public static void main(String[] args) {

		try {
			// establecer la comunicación con el servidor
			// crear el canal de comunicación y establecer la
			// conexión con el servicio por defecto en localhost

			// com = new CommClient();
			com = new CommClient("192.168.0.37", 5000);
			// TODO: com = new CommClient(servidor.ddns.net, puerto); (?)

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

				System.out.println("Enter to roll the dice!");
				scanner.nextLine();
				play();

			}

			if (n != CursedBoardGame.END_GAME) { // se abandona el juego
				System.out.printf("\nYou left the game!\n");
			} else { // el juego ha terminado
				System.out.printf("\nGame ended: %s\n",
						position() < CursedBoardGame.CELLS ? "You lose" : "You win");
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