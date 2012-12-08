package eecs598;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import eecs598.probability.Distribution;

public abstract class Node {
	
	private int id;
	
	public Node(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
	/**
	 * Updates the node's beliefs according to the new signal
	 * We pass the graph in case nodes want to use information from neighbors.
	 * @param signal
	 */
	public abstract void newSignal(Collection<Node> neighbors, double signal);
	
	/**
	 * Returns the node's current set of beliefs on possible states.
	 * @return
	 */
	public abstract HashMap<Double, Double> getBeliefs();
	
	/**
	 * Sets the list of possible distributions.
	 * @param distributions
	 */
	public abstract void setPossibleDistributions(List<Distribution> distributions);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
