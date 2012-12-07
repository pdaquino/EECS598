package eecs598.factories;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import eecs598.Edge;
import eecs598.Node;

public class GraphFactory implements Factory<UndirectedGraph<Node, Edge>> {

	@Override
	public UndirectedGraph<Node, Edge> create() {
		return new UndirectedSparseGraph<Node, Edge>();
	}
}
