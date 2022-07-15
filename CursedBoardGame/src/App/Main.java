package App;

import Utils.Player;
import Utils.Card;

public class Main {

	public static void main (String args[]) {
		Player a = new Player ("Alex");
		System.out.println(a);
		Card gg = new Card("Go forward 10 positions", (x) -> x.move(10));
		gg.accept(a);
		System.out.println(a);
	}
}
