package eecs598;

import java.util.HashMap;
import java.util.List;

import eecs598.probability.Distribution;
import eecs598.probability.DistributionFactory;
import eecs598.probability.ProbabilityUtil;

/**
 * Estimates the beliefs by constructing a distribution that
 * matches the esimated parameter, sampling from it and then
 * seeing how likely that sample was to have come from the candidate
 * distributions.
 * 
 * @author Pedro
 *
 */
public class RandomDrawBeliefEstimator implements BeliefEstimator {
	
	/**
	 * This class creates parametrized distributions. We use it to construct
	 * a customized distribution with our current estimate of the parameter.
	 */
	private DistributionFactory distributionFactory;
	
	public RandomDrawBeliefEstimator(DistributionFactory distributionFactory) {
		super();
		this.distributionFactory = distributionFactory;
	}
	@Override
	public HashMap<Double, Double> estimateBeliefs(double parameterEstimate,
			List<Distribution> possibleDistributions) {
		//
		// We estimate the beliefs using the following process. We first construct
		// a distribution whose parameter is our current estimate; we then draw from
		// this distribution. The belief of a possible distribution is the
		// likelihood of this random sample having originated from that distribution,
		// which is a value proportional to that distribution's pdf.
		//
		HashMap<Double, Double> beliefs = new HashMap<>(possibleDistributions.size());
		
		// TODO try sampling many times and averaging them out?
		
		Distribution estimatedDistribution = distributionFactory.create(parameterEstimate);
		
		double randomSample = estimatedDistribution.draw();
		for(Distribution candidate : possibleDistributions) {
			double pdf = candidate.pdf(randomSample);
			beliefs.put(candidate.getParameter(), pdf);
		}
		
		ProbabilityUtil.normalizeToOne(beliefs);
		
		return beliefs;
	}

}
