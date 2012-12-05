package eecs598.test.helpers;

import java.util.ArrayList;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import eecs598.Edge;
import eecs598.Node;
import eecs598.NonBayesianNode;
import eecs598.probability.Distribution;

public class TestGraphs {
	public static class NonBayesianFactory implements Factory<Node> {
		int id = 1;
		@Override
		public Node create() {
			return new NonBayesianNode(id++, new ArrayList<Distribution>());
		}
	}
	
	public static class EdgeFactory implements Factory<Edge> {
		int id = 1;
		@Override
		public Edge create() {
			return new Edge(id++);
		}
	}
	
	public static class GraphFactory implements Factory<Graph<Node, Edge>> {

		@Override
		public Graph<Node, Edge> create() {
			return new SparseGraph<Node, Edge>();
		}
	}
	
	public static Graph<Node, Edge> smallWorldNonBayesian(int latticeSize) {
		KleinbergSmallWorldGenerator<Node, Edge> kleinbergGenerator = new KleinbergSmallWorldGenerator<>(
				new GraphFactory(), new NonBayesianFactory(), new EdgeFactory(), latticeSize, 5);
		
		return kleinbergGenerator.create();
	}
}
