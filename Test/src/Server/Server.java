package Server;

import java.io.IOException;

import lib.ChannelException;
import lib.CommServer;
import optional.Trace;

public class Server {
	private static void registrarOperaciones(CommServer com) {
		com.addFunction("one", (o, x) -> ((Service)o).one());
		com.addFunction("two", (o, x) -> ((Service)o).two((int) x[1]));
		com.addFunction("ready", (o, x) -> ((Service)o).ready());
		com.addFunction("turn", (o, x) -> ((Service)o).turn());
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
				new Thread(new Threads(idCliente, com)).start();
				Trace.printf("-- Creado hilo para el cliente %d.\n",
						idCliente);
			}
		} catch (IOException | ChannelException e) {
			System.err.printf("Error: %s\n", e.getMessage());
			e.printStackTrace();
		}
	}


}
