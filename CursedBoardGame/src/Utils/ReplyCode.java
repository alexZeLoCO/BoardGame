package Utils;

import java.util.function.Supplier;

import Server.Service;

public enum ReplyCode {

	NONE (0),
	CHOOSE_PLAYER (1, () -> Service.runPlayerSelection());
	
	int v;
	Supplier<Integer> act;

	private ReplyCode (int v) {
		this(v, () -> 0);
	}
	
	private ReplyCode (int v, Supplier<Integer> act) {
		this.v = v;
		this.act = act;
	}

	int getV () {
		return this.v;
	}
	
	public int run () {
		return this.act.get();
	}
	
}