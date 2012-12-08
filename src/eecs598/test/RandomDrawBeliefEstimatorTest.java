package eecs598.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import eecs598.RandomDrawBeliefEstimator;
import eecs598.experiments.DistributionRepository;
import eecs598.factories.probability.DistributionFactory;
import eecs598.factories.probability.GaussianFactory;
import eecs598.probability.Distribution;
import eecs598.test.helpers.TestDistributions;

public class RandomDrawBeliefEstimatorTest {

	@Test
	public void testUniform() {
		//
		// Creates a bunch of uniform distributions and checks
		// whether they end up being as likely.
		//
		
		int countPossibleDistributions = 10;
		
		//
		// The parameter doesn't matter for the uniform distribution.
		//
		
		DistributionFactory factory = new TestDistributions.UniformFactory(); 
		RandomDrawBeliefEstimator estimator = new RandomDrawBeliefEstimator(factory);
		
		List<Distribution> possibleDistributions = new ArrayList<Distribution>();
		for(int i = 0; i < countPossibleDistributions; i++) {
			possibleDistributions.add(factory.create(0.0));
		}
		
		HashMap<Double, Double> beliefs = estimator.estimateBeliefs(0.5, possibleDistributions);
		double expectedBelief = 1.0 / countPossibleDistributions;
		
		for(Double belief : beliefs.values()) {
			assertEquals(expectedBelief, belief, 0.0001);
		}
	}
	
	@Test
	public void testGaussian() {
		//55.0506799730004
		List<Distribution> possibleDistributions = new ArrayList<>();
		DistributionRepository.getTwoGaussiansHard(possibleDistributions);
		
		RandomDrawBeliefEstimator estimator = new RandomDrawBeliefEstimator(new GaussianFactory());
		HashMap<Double, Double> beliefs = estimator.estimateBeliefs(55.0506799730004, possibleDistributions);
		
		for(Double belief : beliefs.values()) {
			assertFalse(Double.isNaN(belief));
		}
	}

}
