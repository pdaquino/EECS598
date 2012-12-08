package eecs598.util;

public class Util {
	public static void throwIfNaN(Double d) {
		if(Double.isNaN(d)) {
			throw new IllegalStateException("NaN");
		}
	}
}
