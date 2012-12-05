package eecs598;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import eecs598.probability.Distribution;
import eecs598.probability.ProbabilityUtil;

/**
 * Represents a DeGroot node, which maintains an estimate of the parameter. The main
 * mechanism through which it achieves that is using its neighbors information.
 * 
 * This class requires two objects to work. One of them is responsible for incorporating
 * a signal into the current estimate. The other class converts the parameter
 * estimate into a probability distribution over the possible underlying distributions.
 * 
 * @author Pedro
 *
 */
public class DeGrootNode implements Node {
	
	// TODO I am not considering the node's own estimate for the first time
	// it receives a signal -- I am just copying the average of the neighbor's.
	// Does it make sense? 
	private double estimate = 0.0;
	private boolean firstSignal = true;
	
	/**
	 * Given a signal, updates the current estimate.
	 */
	private ParameterEstimator parameterEstimator;
	
	/**
	 * Converts the estimate into a set of beliefs, i.e. a probability
	 * distribution.
	 */
	private BeliefEstimator beliefEstimator;
	
	/**
	 * We use this only to estimate the beliefs.
	 */
	private List<Distribution> possibleDistributions;
	
	public DeGrootNode(ParameterEstimator parameterEstimator,
			BeliefEstimator beliefEstimator) {
		super();
		this.parameterEstimator = parameterEstimator;
		this.beliefEstimator = beliefEstimator;
	}

	@Override
	public void newSignal(Collection<Node> neighbors, double signal) {
		//
		// Use the signal to update our estimate.
		//
		estimate = parameterEstimator.estimateParameter(signal, estimate);

		//
		// Average the neighbors' beliefs, with weights.
		//
		double sumEstimates = 0.0;
		double sumWeights = 0.0;
		
		//
		// Skips our estimate in the first signal, because it is completely
		// arbitrary.
		//
		
		if(!firstSignal) {
			sumEstimates += this.estimate;
			sumWeights += getNeighborInfluence(this, neighbors); // self-influence
		} else {
			firstSignal = false;
		}
		
		for(Node neighbor : neighbors) {
			double weight = getNeighborInfluence(neighbor, neighbors);
			double weightedEstimate = weight * ProbabilityUtil.expectedValue(neighbor.getBeliefs());
			sumEstimates += weightedEstimate;
			sumWeights += weight;
		}
		
		this.estimate = sumEstimates / sumWeights;
	}
	
	/**
	 * Returns the influence, between 0 and 1, that a given neihgbor exerts over this node.
	 * For every node, the sum of all its neighbors' influences must be 1. We assume that
	 * every node has a self-edge so that it can influence itself.
	 * @param neighbor The neighbor whose influence will be returned.
	 * @return
	 */
	protected double getNeighborInfluence(Node neighbor, Collection<Node> neighbors) {
		// TODO This is copy/paste from NonBayesianNode.. Code smell
		
		//
		// We just have a uniform distribution.
		//
		if(neighbors.contains(this)) {
			throw new IllegalStateException("Self-loop in node " + toString() + ". Self-loops are not allowed.");
		}
		int neighborCount = neighbors.size() + 1; // +1 accounts for self-edge
		return 1.0/neighborCount;		
	}


	@Override
	public HashMap<Double, Double> getBeliefs() {
		return beliefEstimator.estimateBeliefs(estimate, possibleDistributions);
	}

	public double getParameterEstimate() {
		return estimate;
	}

	@Override
	public void setPossibleDistributions(List<Distribution> distributions) {
		this.setPossibleDistributions(distributions);
	}

}
