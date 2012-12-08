package eecs598;

import java.util.HashMap;
import java.util.List;

import eecs598.factories.probability.DistributionFactory;
import eecs598.probability.Distribution;
import eecs598.probability.ProbabilityUtil;
import eecs598.util.Util;

public class ManyRandomDrawsBeliefEstimator extends RandomDrawBeliefEstimator {

	private final int numDraws = 50;
	
	public ManyRandomDrawsBeliefEstimator(
			DistributionFactory distributionFactory) {
		super(distributionFactory);
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
		for(Distribution candidate : possibleDistributions) {
			beliefs.put(candidate.getParameter(), 1.0);
		}
		
		Distribution estimatedDistribution = distributionFactory.create(parameterEstimate);
		for (int i = 0; i < numDraws; i++) {
			double randomSample = estimatedDistribution.draw();
			for (Distribution candidate : possibleDistributions) {
				double pdf = candidate.pdf(randomSample);
				Util.throwIfNaN(pdf);
				double currentPdf = beliefs.get(candidate.getParameter());
				beliefs.put(candidate.getParameter(), currentPdf * pdf);
			}
			ProbabilityUtil.normalizeToOne(beliefs);
		}
				
		return beliefs;
	}

}
