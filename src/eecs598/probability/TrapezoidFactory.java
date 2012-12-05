package eecs598.probability;

public class TrapezoidFactory implements DistributionFactory {

	@Override
	public Distribution create(double parameter) {
		return new TrapezoidDistribution(parameter);
	}

}
