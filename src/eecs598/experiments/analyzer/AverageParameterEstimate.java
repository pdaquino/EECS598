package eecs598.experiments.analyzer;

import java.util.Collection;

import eecs598.DeGrootNode;
import eecs598.Node;

public class AverageParameterEstimate {

	public static Double getAverageParameterEstimate(Collection<Node> nodes) {
		double parameterEstimateSum = 0;
		int countValidNodes = 0;
		for (Node generalNode : nodes) {

			if (!(generalNode instanceof DeGrootNode)) {
				continue;
			}
			countValidNodes++;
			
			DeGrootNode node = (DeGrootNode)generalNode;
			parameterEstimateSum += node.getParameterEstimate();
		}
		
		return parameterEstimateSum /= countValidNodes;
	}
}
