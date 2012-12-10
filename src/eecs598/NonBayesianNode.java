package eecs598;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import eecs598.probability.Distribution;
import eecs598.probability.ProbabilityUtil;
import eecs598.util.Util;

public class NonBayesianNode extends Node implements Serializable {


    // @todo[DDC] Should be taken as arguments
    // Ratio of all nodes that pay attention to their received signal
    private static final double listensToSignalRatio = 1.0;

    // Does this particular node pay attention to its received signal
    private boolean listensToSignal;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Associates each possible distribution to a belief between
	 * between 0 and 1.
	 */
	private HashMap<Double, Double> beliefs;
	
	/**
	 * An order list of possible distributions.
	 */
	private List<Distribution> distributions;
		
	public NonBayesianNode(int id, List<Distribution> possibleDistributions) {
		super(id);
                Random random = new Random();
                this.listensToSignal = (random.nextDouble() < NonBayesianNode.listensToSignalRatio);
		setPossibleDistributions(possibleDistributions);
	}
	
	@Override
	public synchronized void newSignal(Collection<Node> neighbors, double signal) {
		//
		// Perform the bayesian update + the degroot-style averaging of neighbors.
		//
                if(this.listensToSignal) { doBayesianUpdate(neighbors, signal); }
		ProbabilityUtil.normalizeToOne(beliefs);
		doNonBayesianUpdate(neighbors, signal);
		ProbabilityUtil.normalizeToOne(beliefs);
	}
	
	protected void doBayesianUpdate(Collection<Node> neighbors, double signal) {
		//
		// Let w be the signal and T a distribution that might have generated w.
		//
		// P(T | w) = (P(w | T)/P(w))P(T)
		//
		// The first term is proportional to the pdf of T. The second is the prior,
		// currently in the beliefs map. We have to normalize the pdfs in order to
		// get P(w|T)/P(W).
		//
		
		//
		// Get p(w|T)/p(w).
		//
		List<Double> pdfsAtW = new ArrayList<Double>(distributions.size());
		for(Distribution dist : distributions) {
			double pdf = dist.pdf(signal); 
			pdfsAtW.add(pdf);
		}
		
		List<Double> normalizedPdfs = ProbabilityUtil.normalizeToOne(pdfsAtW);
		
		//
		// Update the belief as u_{t+1} = p(w|T)*u_{t}/p(w).
		//
		for(int i = 0; i < pdfsAtW.size(); i++) {
			Distribution dist = distributions.get(i);
			double prior = beliefs.get(dist.getParameter());
			double updatedBelief = normalizedPdfs.get(i) * prior;
			double selfInfluence = getNeighborInfluence(this, neighbors);
			beliefs.put(dist.getParameter(), selfInfluence * updatedBelief);
		}
	}

	protected void doNonBayesianUpdate(Collection<Node> neighbors, double signal) { 
		//
		// Weighted update of our beliefs based on every neighbor's beliefs.
		//
		for(Node neighbor : neighbors) {
			
			if(neighbor.equals(this)) {
				//throw new IllegalStateException("Self-loop in node " + toString() + ". Self-loops are not allowed.");
				continue;
			}
				
			HashMap<Double, Double> neighborsBeliefs = neighbor.getBeliefs();
			double influence = getNeighborInfluence(neighbor, neighbors);
			
			//
			// Update one belief at a time.
			//
			for(Map.Entry<Double, Double> paramBeliefPair : neighborsBeliefs.entrySet()) {
				double parameter = paramBeliefPair.getKey();
				double belief = paramBeliefPair.getValue();
				double weightedNeighborBelief = influence * belief;
				Util.throwIfNaN(weightedNeighborBelief);
				//
				// Update our belief by adding the weighted neighbor's.
				//
				beliefs.put(parameter, beliefs.get(parameter) + weightedNeighborBelief);
			}
		}
	}
	
	/**
	 * Returns the influence, between 0 and 1, that a given neihgbor exerts over this node.
	 * For every node, the sum of all its neighbors' influences must be 1. We assume that
	 * every node has a self-edge so that it can influence itself.
	 * @param neighbor The neighbor whose influence will be returned.
	 * @return
	 */
	protected double getNeighborInfluence(Node neighbor, Collection<Node> neighbors) {
		// TODO I'm not too happy with the design here... It's going to be a pain
		// we ever really want to have different distributions. This logic will
		// pretty much be replicated in the DeGroot node too. Maybe we should
		// create a new class for assigning influence values?
		
		//
		// We just have a uniform distribution.
		//
		if(neighbors.contains(this)) {
			//throw new IllegalStateException("Self-loop in node " + toString() + ". Self-loops are not allowed.");
		}
		int neighborCount = neighbors.size(); // +1 accounts for self-edge
		if(!neighbors.contains(this)) {
			neighborCount++;
		}
		return 1.0/neighborCount;		
	}

	public List<Distribution> getDistributions() {
		return distributions;
	}

	public void setPossibleDistributions(List<Distribution> possibleDistributions) {
		this.distributions = possibleDistributions;

		this.beliefs = new HashMap<>(possibleDistributions.size());
		double uniformPrior = 1.0/possibleDistributions.size();

		for(Distribution dist : possibleDistributions) {
			this.beliefs.put(dist.getParameter(), uniformPrior);
		}
	}

	@Override
	public synchronized HashMap<Double, Double> getBeliefs() {
		/**
		 * Returning a shallow copy should be fine -- I don't think we will update
		 * the Doubles inside of the HashMap.
		 */
		return new HashMap<>(beliefs);
	}

	@Override
	public String toString() {
		return "NonBayesianNode " + getId() + " [" + beliefs + "]";
	}
}
