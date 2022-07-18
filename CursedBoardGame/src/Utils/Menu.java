package Utils;

import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;

public class Menu {

	private final Scanner scanner = new Scanner (System.in);

	private int size;
	private Map<Integer, Menu.Entry> data;
	
	private String title;
	private final String prompt;
	
	public Menu (String title, String prompt) {
		this.title = title;
		this.prompt = prompt;
		this.size = 0;
		this.data = new TreeMap<Integer, Menu.Entry>();
	}
	
	public int size () {
		return this.size;
	}

	public void add (int idx, String desc) {
		this.data.put(idx, new Entry(idx, desc));
		this.size++;
	}
	
	public String getSelection () {
		return this.toString() + this.prompt;
	}
	
	public int runSelection () {
		System.out.printf("%s%s", this.toString(), this.prompt);
		int selection = Integer.parseInt(scanner.nextLine());
		return selection;
	}

	@Override
	public String toString () {
		String out = String.format("%s\n", this.title);
		for (Entry e : this.data.values()) {
			out+=String.format("%s\n", e.toString());
		}
		return out;
	}

	private final class Entry {
		private int index;
		private String description;
		
		public Entry (int idx, String desc) {
			this.index = idx;
			this.description = desc;
		}
		
		public int getIdx () {
			return this.index;
		}
		
		public String getDesc () {
			return this.description;
		}
		
		@Override
		public String toString () {
			return String.format("%d. %s", this.getIdx(), this.getDesc());
		}

	}
}
