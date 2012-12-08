package eecs598.util;

public class DoubleEquals {
	
	public static boolean equals(Double d1, Double d2, double threshold) {
		return Math.abs(d1.doubleValue() - d2.doubleValue()) <= threshold;
	}
}
