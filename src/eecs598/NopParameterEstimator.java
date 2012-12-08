package eecs598;

import java.io.Serializable;

/**
 * This class doesn't do anything with the new signal.
 * @author Pedro
 *
 */
public class NopParameterEstimator implements ParameterEstimator, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public double estimateParameter(double signal, double currentEstimate) {
		return currentEstimate;
	}

}
