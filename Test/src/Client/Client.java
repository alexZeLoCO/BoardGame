package Client;

import lib.CommClient;
import lib.ChannelException;
import lib.ProtocolMessages;

import java.io.IOException;

import Server.Service;

public class Client {

	private static CommClient com; 
	
	public static void one () throws IOException, ChannelException {
		com.sendEvent(new ProtocolMessages("one"));
		try {
			System.out.printf("%s\n", (String) com.processReply(com.waitReply()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Method two prints the option selected in runSelection();
		com.sendEvent(new ProtocolMessages("two", Service.runSelection()));
	}
	
	public static boolean ready ()  throws IOException, ChannelException {
		com.sendEvent(new ProtocolMessages("ready"));
		try {
			boolean a = (boolean) com.processReply(com.waitReply());
			com.sendEvent(new ProtocolMessages("turn"));
			return (boolean) com.processReply(com.waitReply()) && a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main (String[] args) throws IOException, ChannelException {
		com = new CommClient();
		while (!ready()) {
			try {
			Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		one();
		
	}
}
