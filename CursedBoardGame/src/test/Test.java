package test;

import java.util.Random;

import Server.Service;
import Utils.Player;
import Cards.Card;

public class Test {

	public static void main (String [] args) {
		Player a = new Player("A");
		a.move(10);
		new Service();
		Card c = (new Card("You switch position with a random player", (x) -> x.switchPositionWith((Player) Service.getPlayers().values().toArray()[new Random().nextInt(Service.getPlayers().size())])));
		c.accept(a);
		System.out.println(a.toString());

	}
}
