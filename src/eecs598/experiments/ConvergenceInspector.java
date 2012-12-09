package eecs598.experiments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import eecs598.DeGrootNode;
import eecs598.Node;
import eecs598.NonBayesianNode;
import eecs598.util.DoubleEquals;

/**
 * Examines a collection and node and returns information about the
 * convergence.
 * @author Pedro
 *
 */
public class ConvergenceInspector {
	
	private final double beliefConvergenceThreshold = 0.0001;
	
	/**
	 * We define convergence as:
	 * 	- For NonBayesian: all nodes agreeing that a particular parameter has likelihood 1.
	 *  - For DeGroot: all nodes agreeing on the same parameter estimate.
	 *  - Total convergence: the DeGroot nodes' estimate matches the NonBayesian belief.
	 * @param nodes all nodes in the graph.
	 * @return
	 */
	public boolean haveConverged(Collection<Node> nodes) {
		// TODO look at DeGroot nodes too!
		Collection<NonBayesianNode> nonBayesianNodes = new ArrayList<>();
		Collection<DeGrootNode> deGrootNodes = new ArrayList<>();
		
		separateNodeTypes(nodes, nonBayesianNodes, deGrootNodes, null);
		
		return haveNonBayesianNodesConverged(nonBayesianNodes);
	}
	
	public double getConvergedParameter(Collection<Node> nodes) {
		// TODO look at DeGroot nodes too!
		Collection<NonBayesianNode> nonBayesianNodes = new ArrayList<>();
		Collection<DeGrootNode> deGrootNodes = new ArrayList<>();
		
		separateNodeTypes(nodes, nonBayesianNodes, deGrootNodes, null);
		
		return getNonBayesianConvergedParameter(nonBayesianNodes);
	}
	
	protected void separateNodeTypes(Collection<Node> allNodes,
			Collection<NonBayesianNode> nonBayesianNodes,
			Collection<DeGrootNode> deGrootNodes,
			Collection<Node> otherNodes) {
		for(Node node : allNodes) {
			if(node instanceof NonBayesianNode) {
				nonBayesianNodes.add((NonBayesianNode)node);
			} else if(node instanceof DeGrootNode) {
				deGrootNodes.add((DeGrootNode)node);
			} else if(otherNodes != null) {
				otherNodes.add(node);
			}
		}
	}
	
	protected boolean haveNonBayesianNodesConverged(Collection<NonBayesianNode> nodes) {
		double consensusParameter = Double.MAX_VALUE;
		for (NonBayesianNode node : nodes) {
			Double convergedParameter = getConvergedParameter(node.getBeliefs());
			if(convergedParameter == null) {
				return false;
			} else if(consensusParameter == Double.MAX_VALUE) {
				consensusParameter = convergedParameter;
			} else if(consensusParameter != convergedParameter.doubleValue()) {
				return false;
			}
		}
		return true;
	}
	
	protected double getNonBayesianConvergedParameter(Collection<NonBayesianNode> nodes) {
		for (NonBayesianNode node : nodes) {
			Double convergedParameter = getConvergedParameter(node.getBeliefs());
			if(convergedParameter == null) {
				throw new IllegalArgumentException("The nodes have not converged.");
			} else {
				return convergedParameter;
			}
		}
		throw new IllegalArgumentException("There weren't any NonBayesian nodes");
	}
	
	/**
	 * If the belief map has one parameter with 100% belief, returns that parameter, otherwise
	 * return null.
	 * @param beliefs
	 * @return
	 */
	protected Double getConvergedParameter(Map<Double, Double> beliefs) {
		for(Map.Entry<Double, Double> parameterBelief : beliefs.entrySet()) {
			if(DoubleEquals.equals(parameterBelief.getValue(), 1.0, beliefConvergenceThreshold)) {
				return parameterBelief.getKey();
			}
		}
		//
		// There was no convergence.
		//
		return null;
	}

}
