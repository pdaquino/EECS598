package eecs598.factories;

import java.util.List;
import java.util.Random;

import org.apache.commons.collections15.Factory;

import eecs598.BeliefEstimator;
import eecs598.DeGrootNode;
import eecs598.Node;
import eecs598.NonBayesianNode;
import eecs598.ParameterEstimator;
import eecs598.probability.Distribution;

/**
 * This factory creates nodes in a given ratio of NonBayesian to DeGroot, e.g.
 * 79% NonBayesian.
 * @author Pedro
 *
 */
public class RatioNodeFactory implements Factory<Node> {

	private double nonBayesianRatio;
	private Random random = new Random();
	
	int nodeCounter = 1; // used for node ids
	
	List<Distribution> possibleDistributions; // NonBayesian nodes need this.
	ParameterEstimator parameterEstimator; // DeGroot nodes need this.
	BeliefEstimator beliefEstimator; // DeGroot nodes need this.

	public RatioNodeFactory(double nonBayesianRatio,
			List<Distribution> possibleDistributions,
			ParameterEstimator parameterEstimator,
			BeliefEstimator beliefEstimator) {
		super();
		this.nonBayesianRatio = nonBayesianRatio;
		this.possibleDistributions = possibleDistributions;
		this.parameterEstimator = parameterEstimator;
		this.beliefEstimator = beliefEstimator;
	}

	@Override
	/**
	 * Creates either a non-bayesian or a DeGroot node.
	 */
	public Node create() {
		Node node = null;
		if(random.nextDouble() <= nonBayesianRatio) {
			node = new NonBayesianNode(nodeCounter, possibleDistributions);
		} else {
			node = new DeGrootNode(nodeCounter, parameterEstimator, beliefEstimator, possibleDistributions);
		}
		nodeCounter++;
		return node;
	}
}
