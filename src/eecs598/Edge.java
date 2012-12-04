package eecs598;

/**
 * An edge connecting two nodes.
 * @author Pedro
 *
 */
public class Edge {
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
