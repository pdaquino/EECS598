package eecs598;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import eecs598.probability.Distribution;

public interface Node {
	
	/**
	 * Updates the node's beliefs according to the new signal
	 * We pass the graph in case nodes want to use information from neighbors.
	 * @param signal
	 */
	public void newSignal(Collection<Node> neighbors, double signal);
	
	/**
	 * Returns the node's current set of beliefs on possible states.
	 * @return
	 */
	public HashMap<Double, Double> getBeliefs();
	
	/**
	 * Sets the list of possible distributions.
	 * @param distributions
	 */
	public void setPossibleDistributions(List<Distribution> distributions);
}
