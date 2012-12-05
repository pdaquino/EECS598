package eecs598;
import java.util.HashMap;
import java.util.List;

import eecs598.probability.*;
/**
 * This interface specifies objects that are able to convert a DeGroot style parameter
 * approximation into a set of beliefs over the set of possible probability distributions.
 * @author Pedro
 *
 */
public interface BeliefEstimator {
	public HashMap<Double, Double> estimateBeliefs(double parameterEstimate, List<Distribution> possibleDistributions);
}
