package eecs598.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eecs598.ManyRandomDrawsBeliefEstimator;
import eecs598.factories.probability.GaussianFactory;
import eecs598.probability.Distribution;
import eecs598.probability.Gaussian;

public class ManyRandomDrawsTest {

	@Test
	public void test() {
		List<Distribution> distributions = new ArrayList<>();
		distributions.add(new Gaussian(100.0));
		distributions.add(new Gaussian(105.0));
		ManyRandomDrawsBeliefEstimator estim = new ManyRandomDrawsBeliefEstimator(new GaussianFactory());
		System.out.println(estim.estimateBeliefs(102.53279280389806, distributions));
	}

}
