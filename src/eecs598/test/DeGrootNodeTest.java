package eecs598.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eecs598.DeGrootNode;
import eecs598.Node;
import eecs598.NopParameterEstimator;
import eecs598.RandomDrawBeliefEstimator;
import eecs598.test.helpers.ExpectedValueNode;
import eecs598.test.helpers.TestDistributions;

public class DeGrootNodeTest {

	@Test
	public void testOneNeighbor() {
		ExpectedValueNode neighbor = new ExpectedValueNode(42.0);
		DeGrootNode node = new DeGrootNode(new NopParameterEstimator(),
				new RandomDrawBeliefEstimator(new TestDistributions.UniformFactory()));
		
		List<Node> nodeNeighbors = new ArrayList<Node>();
		nodeNeighbors.add(neighbor);
		
		node.newSignal(nodeNeighbors, 0.0);
		
		assertEquals(42.0, node.getParameterEstimate(), 0.0001);
	}
	
	@Test
	public void testTwoNeighbors() {
		ExpectedValueNode neighbor1 = new ExpectedValueNode(42.0);
		ExpectedValueNode neighbor2 = new ExpectedValueNode(0);
		DeGrootNode node = new DeGrootNode(new NopParameterEstimator(),
				new RandomDrawBeliefEstimator(new TestDistributions.UniformFactory()));
		
		List<Node> nodeNeighbors = new ArrayList<Node>();
		nodeNeighbors.add(neighbor1); nodeNeighbors.add(neighbor2);
		
		node.newSignal(nodeNeighbors, 0.0);
		
		assertEquals(21.0, node.getParameterEstimate(), 0.0001);
	}

}
