package eecs598.probability;

import java.util.HashMap;
import java.util.Map;

import eecs598.util.NotImplementedException;

/**
 * Helper class for simple probability methods like getting the expected value.
 * @author Pedro
 *
 */
public class ProbabilityUtil {
	
	/**
	 * Computes the expected value of a variable, where the "key" in the
	 * map is the variable's value and the "value" in the map is its probability.
	 * Performs sum(probabilityMap[i].key * probabilityMap[i].value) for all valid i.
	 * @return
	 */
	public static double expectedValue(HashMap<Double, Double> probabilityMap) {
		// TODO write a test case.
		double expectedValue = 0;
		for(Map.Entry<Double, Double> valueProbPair : probabilityMap.entrySet()) {
			double variableValue = valueProbPair.getKey();
			double probability = valueProbPair.getValue();
			if(probability > 0 && probability < 1) {
				expectedValue += variableValue * probability;
			} else {
				throw new IllegalArgumentException(probability + " is not a valid probability.");
			}
		}
		return expectedValue;
	}
}
