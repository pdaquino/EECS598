package eecs598.probability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	/**
	 * Normalizes a list of values such that their sum adds up to one.
	 * @return the list of normalized values.
	 */
	public static List<Double> normalizeToOne(List<Double> values) {
		double sum = sum(values);
		List<Double> normalizedValues = new ArrayList<Double>(values.size());
		for(Double value : values) {
			normalizedValues.add(value / sum);
		}
		return normalizedValues;
	}
	
	/**
	 * Normalizes a list of values such that their sum adds up to one.
	 * @return the list of normalized values.
	 */
	public static void normalizeToOne(HashMap<? extends Object, Double> values) {
		double sum = sum(values.values());
		for(Map.Entry<? extends Object, Double> entry : values.entrySet()) {
			entry.setValue(entry.getValue() / sum);
		}
	}
	
	private static double sum(Collection<Double> values) {
		double sum = 0.0;
		for(Double value : values) {
			sum += value;
		}
		return sum;
	}
}