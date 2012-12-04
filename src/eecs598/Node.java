package eecs598;

import java.util.HashMap;

import edu.uci.ics.jung.graph.Graph;

public interface Node {
	
	/**
	 * Updates the node's beliefs according to the new signal
	 * We pass the graph in case nodes want to use information from neighbors.
	 * @param signal
	 */
	public void newSignal(Graph<Node, Edge> graph, double signal);
	
	/**
	 * Returns the node's current set of beliefs on possible states.
	 * @return
	 */
	public HashMap<Double, Double> getBeliefs();
}
