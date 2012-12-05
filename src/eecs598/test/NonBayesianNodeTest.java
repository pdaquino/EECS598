package eecs598.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import edu.uci.ics.jung.graph.Graph;
import eecs598.Edge;
import eecs598.Node;
import eecs598.NonBayesianNode;
import eecs598.probability.Distribution;

public class NonBayesianNodeTest extends NonBayesianNode {

	public NonBayesianNodeTest() { super(0, new ArrayList<Distribution>()); }

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
		
		assertEquals(node.getBeliefs().get(uniform.getParameter()), 1.0, 0.0001);
	}
	
	@Test
	public void testSingleNodeUniform() {
		Distribution uniform1 = new TestDistributions.Uniform(1);
		Distribution uniform2 = new TestDistributions.Uniform(2);
		
		List<Distribution> distributions = new ArrayList<Distribution>();
		distributions.add(uniform1); distributions.add(uniform2);
		
		NonBayesianNode node = new NonBayesianNode(1, distributions);
		
		node.newSignal(new ArrayList<Node>(), 0.2);
		
		assertEquals(0.5, node.getBeliefs().get(uniform1.getParameter()), 0.0001);
		assertEquals(0.5, node.getBeliefs().get(uniform2.getParameter()), 0.0001);
	}
	
	@Test
	public void testTwoNodes() {
		//
		// Two nodes, A and B. A sees two uniform distributions. B sees a uniform
		// and one zero distribution. This test assesses whether A can correctly use
		// B's information to update its belief in a non-bayesian way.
		//

		Distribution zero = new TestDistributions.ZeroPdf(2); // zero and uniform2 have the same id
		Distribution uniform1 = new TestDistributions.Uniform(1);
		Distribution uniform2 = new TestDistributions.Uniform(2);
		
		List<Distribution> distributionsA = new ArrayList<Distribution>();
		distributionsA.add(uniform1); distributionsA.add(uniform2);
		
		List<Distribution> distributionsB = new ArrayList<Distribution>();
		distributionsB.add(uniform1); distributionsB.add(zero);
		
		NonBayesianNode nodeA = new NonBayesianNode(1, distributionsA);
		NonBayesianNode nodeB = new NonBayesianNode(2, distributionsB);
		
		List<Node> nodeANeighbors = new ArrayList<Node>();
		nodeANeighbors.add(nodeB);
		
		//
		// Node B will believe 1.0 in uniform1, 0 in "uniform 2" (which is actually zero).
		//
		nodeB.newSignal(new ArrayList<Node>(), 0.2);
		assertEquals(1.0, nodeB.getBeliefs().get(1.0), 0.0001);
		assertEquals(0.0, nodeB.getBeliefs().get(2.0), 0.0001);
		
		//
		// Node A can't discern anything from this signal, the only information
		// it gets is from B. The final beliefs will be (0.5 + 1, 0.5 + 0) which
		// when normalized are (2/3, 1/3).
		//
		nodeA.newSignal(nodeANeighbors, 0.2);
				
		assertEquals(2.0/3, nodeA.getBeliefs().get(1.0), 0.0001);
		assertEquals(1.0/3, nodeA.getBeliefs().get(2.0), 0.0001);
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
				
		assertEquals(1.0, nodeA.getBeliefs().get(1.0), 0.0001);
		assertEquals(0.0, nodeA.getBeliefs().get(2.0), 0.0001);
	}
	
	@Test
	public void testRandomGraph() {
		//
		// Like testHundredNodes but this time we use a Small World graph and iterate a bunch of times.
		//

		Distribution zero = new TestDistributions.ZeroPdf(2); // zero and uniform2 have the same id
		Distribution uniform1 = new TestDistributions.Uniform(1);
		Distribution uniform2 = new TestDistributions.Uniform(2);
		
		List<Distribution> distributionsA = new ArrayList<Distribution>();
		distributionsA.add(uniform1); distributionsA.add(uniform2);
		
		List<Distribution> distributionsB = new ArrayList<Distribution>();
		distributionsB.add(uniform1); distributionsB.add(zero);
		
		Graph<Node, Edge> graph = TestGraphs.smallWorldNonBayesian(10);
		
		//
		// We only care about A and its neighbors.
		//
		
		Node nodeA = graph.getVertices().iterator().next();
		nodeA.setPossibleDistributions(distributionsA);
		Collection<Node> nodeANeighbors = graph.getNeighbors(nodeA);
		
		//
		// Tell all of A's neighbors what the right distribution is.
		//
		
		for(Node neighbor : nodeANeighbors) {
			neighbor.setPossibleDistributions(distributionsB);
			neighbor.newSignal(new ArrayList<Node>(), 0.1);
		}
		//
		// We make sure that A converges while it gets information from its neighbors. 
		//
		for(int i = 0; i < 100; i++) {
			nodeA.newSignal(nodeANeighbors, 0.2);
		}
				
		assertEquals(1.0, nodeA.getBeliefs().get(1.0), 0.0001);
		assertEquals(0.0, nodeA.getBeliefs().get(2.0), 0.0001);
	}

}
