package eecs598;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import eecs598.probability.Distribution;
import eecs598.probability.ProbabilityUtil;
import eecs598.util.Util;

public class DistanceBeliefEstimator implements BeliefEstimator, Serializable {

	@Override
	public HashMap<Double, Double> estimateBeliefs(double parameterEstimate,
			List<Distribution> possibleDistributions) {
		HashMap<Double, Double> beliefs = new HashMap<>(
				possibleDistributions.size());
		if(parameterEstimate != -1) {
			for (Distribution candidate : possibleDistributions) {
				double belief = Math.exp(-Math.pow(candidate.getParameter() - parameterEstimate, 2));
				beliefs.put(candidate.getParameter(), belief);
			}
			ProbabilityUtil.normalizeToOne(beliefs);
		} else {
			// uniform
			for (Distribution candidate : possibleDistributions) {
				beliefs.put(candidate.getParameter(), 1.0);
			}
			ProbabilityUtil.normalizeToOne(beliefs);
		}
		return beliefs;
	}

}
