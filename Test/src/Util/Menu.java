package Util;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Menu {

	private Map<Integer, String> data;
	private String title;
	private final String prompt;
	
	private final Scanner scanner = new Scanner(System.in);
	
	public Menu (String title, String prompt) {
		this.title = title;
		this.prompt = prompt;
		this.data = new HashMap<Integer, String> ();
	}
	
	public void add (int idx, String desc) {
		this.data.put(idx, desc);
	}

	public int runSelection () {
		int out;
		do {
			System.out.print(this.toString());
			out = Integer.parseInt(this.scanner.nextLine());
		} while (!this.data.containsKey(out));
		return out;
	}
	
	@Override
	public String toString () {
		String out = String.format("%s\n", this.title);
		for (Map.Entry<Integer, String> e : this.data.entrySet()) {
			out+=String.format("%d. %s\n", e.getKey(), e.getValue());
		}
		out+=String.format("%s ", this.prompt);
		return out;
	}
	
	public void clear () {
		this.data.clear();
	}
}
