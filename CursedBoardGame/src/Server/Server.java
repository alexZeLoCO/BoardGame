package Server;

import java.io.IOException;

import lib.ChannelException;
import lib.CommServer;
import optional.Trace;

import Cards.Card;
import Cards.Deck;

class Server {
	private static void registrarOperaciones(CommServer com) {
		com.addFunction("startGame", (o, x) -> ((Service) o).startGame());
		com.addFunction("myTurn", (o, x) -> ((Service)o).myTurn());
		com.addFunction("position", (o, x) -> ((Service)o).position());
		com.addFunction("play", (o, x) -> ((Service)o).play());
		com.addFunction("show", (o, x) -> ((Service)o).show());
		com.addAction("setName", (o, x) -> ((Service)o).setName((String) x[0]), true);
		com.addAction("setLobbySize", (o, x) -> ((Service)o).setLobbySize((int) x[0]), true);
		com.addFunction("isEmptyLobby", (o, x) -> ((Service)o).isEmptyLobby());
		com.addFunction("win", (o, x) -> ((Service)o).win());
		com.addAction("buy", (o, x) -> ((Service)o).buy(Deck.getInstance().getCardAt(Integer.parseInt((String) x[0]))), true);
		com.addFunction("money", (o, x) -> ((Service)o).money());
	}

	public static void main(String[] args) {
		CommServer com; // canal de comunicación del servidor
		int idCliente; // identificador del cliente

		try {
			// crear el canal de comunicación del servidor
			com = new CommServer();
			// TODO: comm = new CommServer(puerto); (?) // Default 5000 (?)

			// activar la traza en el servidor (opcional)
			Trace.activateTrace(com);

			// activar el registro de mensajes del servidor (opcional)
			com.activateMessageLog();

			// registrar operaciones del servicio
			registrarOperaciones(com);
			
			// ofrecer el servicio (queda a la escucha)
			while (true) {
				// espera por un cliente
				idCliente = com.waitForClient();

				// conversación con el cliente en un hilo
				Trace.printf("-- Creando hilo para el cliente %d.\n",
						idCliente);
				new Thread(new Hilos(idCliente, com)).start();
				Trace.printf("-- Creado hilo para el cliente %d.\n",
						idCliente);
			}
		} catch (IOException | ChannelException e) {
			System.err.printf("Error: %s\n", e.getMessage());
			e.printStackTrace();
		}
	}

}
