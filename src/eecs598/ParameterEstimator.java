package eecs598;

public interface ParameterEstimator {
	/**
	 * Return a new estimate of the parameter after receiving a signal.
	 * @param signal
	 * @param currentEstimate
	 * @return
	 */
	public double estimateParameter(double signal, double currentEstimate);
}
