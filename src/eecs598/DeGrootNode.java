package eecs598;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eecs598.probability.Distribution;
import eecs598.probability.ProbabilityUtil;
import eecs598.util.Util;

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
public class DeGrootNode extends Node implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// TODO I am not considering the node's own estimate for the first time
	// it receives a signal -- I am just copying the average of the neighbor's.
	// Does it make sense? 
	private double estimate = -1;
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
	
	public DeGrootNode(int id, ParameterEstimator parameterEstimator,
			BeliefEstimator beliefEstimator) {
		super(id);
		this.parameterEstimator = parameterEstimator;
		this.beliefEstimator = beliefEstimator;
	}

	public DeGrootNode(int id, ParameterEstimator parameterEstimator,
			BeliefEstimator beliefEstimator,
			List<Distribution> possibleDistributions) {
		super(id);
		this.parameterEstimator = parameterEstimator;
		this.beliefEstimator = beliefEstimator;
		this.possibleDistributions = possibleDistributions;
	}

	@Override
	public void newSignal(Collection<Node> neighbors, double signal) {
		if(getId() == 5) {
			// XXX
			int a = 42;
		}
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
//			double weight = getNeighborInfluence(this, neighbors); // self-influence
//			sumEstimates += weight*this.estimate;
//			sumWeights +=  weight; 
		} else {
			firstSignal = false;
		}
		
		for(Node neighbor : neighbors) {
			double weight = getNeighborInfluence(neighbor, neighbors);
			Map<Double,Double> neighborsBeliefs = neighbor.getBeliefs();
			double expectedValue = ProbabilityUtil.expectedValue(neighborsBeliefs);
			double weightedEstimate = weight * expectedValue;
			sumEstimates += weightedEstimate;
			sumWeights += weight;
		}
		
		if(sumWeights > 0) {
			this.estimate = sumEstimates / sumWeights;
		} else {
			this.estimate = 0;
		}
		
		Util.throwIfNaN(this.estimate);
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
			//throw new IllegalStateException("Self-loop in node " + toString() + ". Self-loops are not allowed.");
			//return 0;
		}
		if(neighbor == this) {
			return 0;
		}
		if(neighbor instanceof NonBayesianNode) {
			return 1;
		} else {
			return 1;
		}
//		int neighborCount = neighbors.size();// + 1; // +1 accounts for self-edge
//		return 1.0/neighborCount;		
	}


	@Override
	public HashMap<Double, Double> getBeliefs() {
		// XXX test code
		
		return beliefEstimator.estimateBeliefs(estimate, possibleDistributions);
	}

	public double getParameterEstimate() {
		return estimate;
	}

	@Override
	public void setPossibleDistributions(List<Distribution> distributions) {
		this.setPossibleDistributions(distributions);
	}

	@Override
	public void resetBeliefs() {
		this.estimate = -1.0;
		
	}
	
	public String toString() {
		return "DeGrootNode " + getId() + "[ param = " + estimate + "] [" + getBeliefs() + "]";
	}

}
