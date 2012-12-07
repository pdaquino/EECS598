package eecs598.test.helpers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import eecs598.Node;
import eecs598.probability.Distribution;

/**
 * A node that returns a set of beliefs such that the expected
 * parameter value is a user-specified value.
 * @author Pedro
 *
 */
public class ExpectedValueNode extends Node {
	private double expectedValue = 0;
	public ExpectedValueNode(int id, double expectedBeliefValue) {
		super(id);
		this.expectedValue = expectedBeliefValue;
	}
	
	@Override
	public void newSignal(Collection<Node> neighbors, double signal) {
	}

	/**
	 * Returns a probability distribution whose expected value is expectedValue.
	 * No other guarantee is made about it; in special it does not follow
	 * whatever might be passed in setPossibleDistributions.
	 * @return
	 */
	@Override
	public HashMap<Double, Double> getBeliefs() {
		HashMap<Double, Double> beliefs = new HashMap<>();
		beliefs.put(expectedValue, 1.0);
		return beliefs;
	}

	@Override
	public void setPossibleDistributions(List<Distribution> distributions) {

	}

}
