package eecs598.probability;

import java.io.Serializable;

import cern.jet.random.Normal;
import cern.jet.random.engine.RandomEngine;

public class Gaussian implements Distribution, Serializable {

	private Normal normal;
	private double mean;
	
	private final double stdDev = 1;
	
	public Gaussian(double mean) {
		this.mean = mean;
		this.normal = new Normal(mean, stdDev, RandomEngine.makeDefault());
	}
	
	@Override
	public double draw() {
		return normal.nextDouble();
	}

	@Override
	public double pdf(double x) {
		return normal.pdf(x);
	}

	@Override
	public double getParameter() {
		return mean;
	}

}
