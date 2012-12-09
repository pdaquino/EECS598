package eecs598.util;

import java.util.Collection;

public class MeanAndStdDev {
	private double mean;
	private double stdDev;
	public MeanAndStdDev(double mean, double stdDev) {
		super();
		this.mean = mean;
		this.stdDev = stdDev;
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getStdDev() {
		return stdDev;
	}
	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}
	
	public static MeanAndStdDev fromSample(Collection<Double> samples) {
		double mean = 0.0;
		for(Double sample : samples) {
			mean += sample;
		}
		mean /= samples.size();
		double variance = 0.0;
		for(Double sample : samples) {
			variance += Math.pow(sample - mean, 2);
		}
		double stdDev = Math.sqrt(variance/samples.size());
		return new MeanAndStdDev(mean, stdDev);
	}
}

