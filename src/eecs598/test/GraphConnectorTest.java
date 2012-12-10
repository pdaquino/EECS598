package eecs598.test;

import static org.junit.Assert.*;

import org.apache.commons.collections15.Factory;
import org.junit.Test;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import eecs598.experiments.GraphConnector;

public class GraphConnectorTest {

	public Factory<Integer> getIntegerEdgeFactory() {
		return new Factory<Integer>() {
			int id = 1;
			@Override
			public Integer create() {
				return id++;
			}
		};
	}
	
	@Test
	public void test() {
		Graph<Integer, Integer> g = new SparseMultigraph<Integer, Integer>();
		// Add some vertices. From above we defined these to be type Integer.
		g.addVertex((Integer)1);
		g.addVertex((Integer)2);
		g.addVertex((Integer)3); 
		
		GraphConnector connector = new GraphConnector();
		connector.makeConnected(g, getIntegerEdgeFactory());
		
		assertEquals(2, g.getEdgeCount());
	}
	
	@Test
	public void test2() {
		Graph<Integer, Integer> g = new SparseMultigraph<Integer, Integer>();
		// Add some vertices. From above we defined these to be type Integer.
		g.addVertex((Integer)1);
		g.addVertex((Integer)2);
		g.addVertex((Integer)3); 
		
		GraphConnector connector = new GraphConnector();
		connector.makeConnected(g, getIntegerEdgeFactory());
		
		assertEquals(2, g.getEdgeCount());
		assertTrue(connector.isConnected(g));
	}

}
