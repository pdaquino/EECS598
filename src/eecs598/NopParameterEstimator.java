package eecs598;

/**
 * This class doesn't do anything with the new signal.
 * @author Pedro
 *
 */
public class NopParameterEstimator implements ParameterEstimator {

	@Override
	public double estimateParameter(double signal, double currentEstimate) {
		return currentEstimate;
	}

}
