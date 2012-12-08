package eecs598;

import java.io.Serializable;

/**
 * An edge connecting two nodes.
 * @author Pedro
 *
 */
public class Edge implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	// Do we need anything fancier?
	//
	private int id;
	public Edge(int id) {
		this.id = id;
	}
	
	public String toString() {
		return "Edge-" + id;
	}
}
