package eecs598.factories.probability;

import eecs598.probability.Distribution;

public interface DistributionFactory {
	public Distribution create(double parameter);
}
