package eecs598;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;
import eecs598.probability.Distribution;
import eecs598.util.NotImplementedException;

public class DeGrootNode implements Node {
	
	@Override
	public void newSignal(Collection<Node> neighbors, double signal) {
		// TODO Auto-generated method stub
		//
		// This is probably a good time to iterate over this node's neighbors
		// and average their expected values of the parameters.
		//
		throw new NotImplementedException();
		
		
	}

	@Override
	public HashMap<Double, Double> getBeliefs() {
		// TODO Auto-generated method stub
		//
		// This is where we go from the DeGroot node's estimate of
		// the true parameter to a distribution over possible states of 
		// the world. We should write this in a way that makes it easy
		// for us to change the algorithm later. Perhaps we should use a new
		// class for that.
		//
		throw new NotImplementedException();
	}

	@Override
	public void setPossibleDistributions(List<Distribution> distributions) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

}
