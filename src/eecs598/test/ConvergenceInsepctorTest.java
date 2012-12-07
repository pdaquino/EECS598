package eecs598.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import eecs598.Node;
import eecs598.NonBayesianNode;
import eecs598.experiments.ConvergenceInspector;
import eecs598.probability.Distribution;
import eecs598.test.helpers.TestDistributions;

/**
 * Some of the tests here are copied from other test cases.
 * @author Pedro
 *
 */
public class ConvergenceInsepctorTest {

	@Test
	public void testSingleNodeConvergence() {
		//
		// Simplest test possible: one node with a signal s.t. it can tell the right
		// distribution with 100% likelihood. We use a distribution called ZeroPdf whose
		// pdf always returns 0 to simulate this.
		//
		Distribution uniform = new TestDistributions.Uniform(1);
		Distribution zero = new TestDistributions.ZeroPdf(0);
		
		List<Distribution> distributions = new ArrayList<Distribution>();
		distributions.add(uniform); distributions.add(zero);
		
		NonBayesianNode node = new NonBayesianNode(1, distributions);
		
		node.newSignal(new ArrayList<Node>(), 0.2);
		
		ConvergenceInspector inspector = new ConvergenceInspector();
		Collection<Node> nodes = new ArrayList<Node>();
		nodes.add(node);
		assertTrue(inspector.haveConverged(nodes));
		assertEquals(1.0, inspector.getConvergedParameter(nodes), 0.0000001);
	}

	@Test
	public void testOneHundredNodes() {
		//
		// Like testTwoNodes but with a lot more nodes so that A should converge.
		//

		Distribution zero = new TestDistributions.ZeroPdf(2); // zero and uniform2 have the same id
		Distribution uniform1 = new TestDistributions.Uniform(1);
		Distribution uniform2 = new TestDistributions.Uniform(2);
		
		List<Distribution> distributionsA = new ArrayList<Distribution>();
		distributionsA.add(uniform1); distributionsA.add(uniform2);
		
		List<Distribution> distributionsB = new ArrayList<Distribution>();
		distributionsB.add(uniform1); distributionsB.add(zero);
		
		NonBayesianNode nodeA = new NonBayesianNode(200, distributionsA);
		List<Node> nodeANeighbors = new ArrayList<Node>();
		for(int i = 0; i < 100; i++) {
			NonBayesianNode nodeB = new NonBayesianNode(i, distributionsB);
			nodeB.newSignal(new ArrayList<Node>(), 0.2);

			nodeANeighbors.add(nodeB);
		}
				
		//
		// We make sure that A converges.
		//
		for(int i = 0; i < 100; i++) {
			nodeA.newSignal(nodeANeighbors, 0.2);
		}
				
		ConvergenceInspector inspector = new ConvergenceInspector();
		Collection<Node> nodes = new ArrayList<Node>(nodeANeighbors);
		nodes.add(nodeA);
		assertTrue(inspector.haveConverged(nodes));
		assertEquals(1.0, inspector.getConvergedParameter(nodes), 0.0000001);
	}
	
}
