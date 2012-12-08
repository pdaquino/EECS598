package eecs598.factories.probability;

import eecs598.probability.Distribution;
import eecs598.probability.TrapezoidDistribution;

public class TrapezoidFactory implements DistributionFactory {

	@Override
	public Distribution create(double parameter) {
		return new TrapezoidDistribution(parameter);
	}

}
