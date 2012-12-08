package eecs598.experiments.analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eecs598.DeGrootNode;
import eecs598.Node;
import eecs598.NonBayesianNode;

public class AverageParameterBelief {

	public static List<ParameterBeliefPair> getAverageBeliefs(Collection<Node> nodes, NodeType nodeTypeToBeTracked) {
		//
		// Sorted list of parameters.
		//
		List<ParameterBeliefPair> parameterBeliefs = new ArrayList<>();
		//
		// Go through all nodes (if nonBayesianOnly is set, skip all NonBayesian)
		// and compute the sum of beliefs in every parameter.
		//	
		int countValidNodes = 0;
		for(Node node : nodes) {
			
			if(!isValidNode(node, nodeTypeToBeTracked)) {
				continue;
			}
			countValidNodes++;
			Map<Double, Double> beliefs = node.getBeliefs();
			if(parameterBeliefs.isEmpty()) {
				//
				// Get a sorted list of parameters. Sorting is important because we have to be
				// consistent.
				//
				parameterBeliefs = makeParameterBeliefsList(beliefs);
			}
			for(int i = 0; i < parameterBeliefs.size(); i++) {
				double beliefInParamI = beliefs.get(parameterBeliefs.get(i).parameter);
				parameterBeliefs.get(i).belief += beliefInParamI;
			}
		}
		
		//
		// Divide all the averages by the number of nodes.
		//
		for(int i = 0; i < parameterBeliefs.size(); i++) {
			parameterBeliefs.get(i).belief /= countValidNodes;
		}
		
		return parameterBeliefs;
	}
	
	private static boolean isValidNode(Node node, NodeType nodeTypeToBeTracked) {
		switch(nodeTypeToBeTracked) {
		case NonBayesian:
			return node instanceof NonBayesianNode;
		case DeGroot:
			return node instanceof DeGrootNode;
		default:
				return true;
		}
	}

	private static List<ParameterBeliefPair> makeParameterBeliefsList(Map<Double, Double> beliefs) {
		Set<Double> parameters = beliefs.keySet();
		List<ParameterBeliefPair> parameterBeliefs = new ArrayList<>(parameters.size());
		for(Double parameter : parameters) {
			parameterBeliefs.add(new ParameterBeliefPair(parameter, 0.0));
		}
		Collections.sort(parameterBeliefs);
		return parameterBeliefs;
	}
}
