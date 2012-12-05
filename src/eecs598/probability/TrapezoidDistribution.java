package eecs598.probability;

import eecs598.util.NotImplementedException;

/**
 * A distribution whose pdf's support is in [0, 1] and follows a
 * line with slope, which is the parameter, in [-2, 2].
 * @author Pedro
 *
 */
public class TrapezoidDistribution implements Distribution {
	
	private double m = 0.0;
	
	public TrapezoidDistribution(double m) {
		super();
		this.m = m;
	}

	@Override
	public double draw() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public double pdf(double x) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public double getParameter() {
		return m;
	}

}
