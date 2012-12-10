package eecs598.experiments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Factory;

import eecs598.BeliefEstimator;
import eecs598.ManyRandomDrawsBeliefEstimator;
import eecs598.factories.probability.DistributionFactory;
import eecs598.factories.probability.GaussianFactory;
import eecs598.probability.Distribution;

public class ExperimentalSetup {
	private List<Distribution> possibleDistributions;
	private Distribution trueDistribution;
	private DistributionFactory distributionFactory;
	
	public ExperimentalSetup(List<Distribution> possibleDistributions,
			Distribution trueDistribution,
			DistributionFactory distributionFactory) {
		super();
		this.possibleDistributions = possibleDistributions;
		this.trueDistribution = trueDistribution;
		this.distributionFactory = distributionFactory;
	}

	public List<Distribution> getPossibleDistributions() {
		return possibleDistributions;
	}
	
	public Distribution getTrueDistribution() {
		return trueDistribution;
	}
	
	public DistributionFactory getDistributionFactory() {
		return distributionFactory;
	}

	public static Factory<ExperimentalSetup> nGaussiansHardFactory(final int nGaussians) {
		return new Factory<ExperimentalSetup>() {
			@Override
			public ExperimentalSetup create() {
				List<Distribution> possibleDistributions = new ArrayList<Distribution>();
				Distribution trueDistribution = DistributionRepository.getNGaussiansHard(nGaussians, possibleDistributions);
				ExperimentalSetup setup = new ExperimentalSetup(possibleDistributions, trueDistribution, new GaussianFactory());
				return setup;
			}
			
		};
	}
	public static Factory<ExperimentalSetup> nGaussiansMediumFactory(final int nGaussians) {
		return new Factory<ExperimentalSetup>() {
			@Override
			public ExperimentalSetup create() {
				List<Distribution> possibleDistributions = new ArrayList<Distribution>();
				Distribution trueDistribution = DistributionRepository.getNGaussiansMedium(nGaussians, possibleDistributions);
				ExperimentalSetup setup = new ExperimentalSetup(possibleDistributions, trueDistribution, new GaussianFactory());
				return setup;
			}
		};
	}
}
