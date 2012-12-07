package eecs598.factories.probability;

import eecs598.probability.Distribution;
import eecs598.probability.Gaussian;

public class GaussianFactory implements DistributionFactory {

	@Override
	public Distribution create(double mean) {
		return new Gaussian(mean);
	}

}
