package Items;

import java.util.function.Consumer;

/**
 * Buyable class
 * 
 * A Buyable has a description, cost and an action
 * that is performed when used.
 * 
 * @author Alejandro Rodriguez Lopez
 *
 */
public class Buyable {

	private final int code; // Unique code
	private String desc; // Description
	private double cost; // Cost
	private Consumer<Object> act; // Action
	
	/**
	 * Creates a new Buyable
	 * 
	 * @param desc Description
	 * @param costs Cost
	 * @param act Action
	 */
	public Buyable (int code, String desc, double cost, Consumer<Object> act) {
		this.code = code;
		this.desc = desc;
		this.cost = cost;
		this.act = act;
	}
	
	/**
	 * Returns the code of this Buyable
	 * 
	 * @return Code
	 */
	public int getCode () {
		return this.code;
	}

	/**
	 * Returns the Description of this Buyable
	 * 
	 * @return Description
	 */
	public String getDesc () {
		return this.desc;
	}

	/**
	 * Returns the cost of this buyable.
	 * 
	 * @return Cost
	 */
	public double getCost () {
		return this.cost;
	}
	
	/**
	 * Returns the action performed by this Buyable
	 * 
	 * @return Action of this Buyable
	 */
	public Consumer<Object> getAct () {
		return this.act;
	}
	
	/**
	 * Consumes this buyable
	 * 
	 * @param target Target object of this buyable
	 */
	public void use (Object target) {
		this.getAct().accept(target);
	}
	
	@Override
	public String toString () {
		return String.format("%s (%.2f$)", this.getDesc(), this.getCost());
	}
	
}
