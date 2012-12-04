package eecs598;

import java.util.HashMap;

import edu.uci.ics.jung.graph.Graph;
import eecs598.util.NotImplementedException;

public class NonBayesianNode implements Node {

	/**
	 * Associates each possible distribution to a belief between
	 * between 0 and 1.
	 */
	private HashMap<Double, Double> beliefs;
	
	//
	// When NB nodes are constructed, we need to pass it
	// all the possible Distributions, and then we extract the
	// possible parameters and put them in the beliefs map with
	// a uniform prior.
	//
	
	@Override
	public void newSignal(Graph<Node, Edge> graph, double signal) {
		// TODO Auto-generated method stub
		//
		// Perform the bayesian update + the degroot-style averaging of neighbors.
		//
		throw new NotImplementedException();
	}

	@Override
	public HashMap<Double, Double> getBeliefs() {
		/**
		 * Returning a shallow copy be fine -- I don't think we will update
		 * the Doubles inside of the HashMap.
		 */
		return new HashMap<>(beliefs);
	}

}
