package test;

import Utils.Menu;

public class Test {
	public static void main (String [] args) {
		Menu m = new Menu("test", "test");
		m.add(1, "Alex");
		System.out.println(m.runSelection());
	}
}
