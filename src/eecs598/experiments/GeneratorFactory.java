package eecs598.experiments;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.graph.UndirectedGraph;
import eecs598.Edge;
import eecs598.Node;

public interface GeneratorFactory {
	public GraphGenerator<Node, Edge> create(Factory<Node> nodeFactory, Factory<Edge> edgeFactory,
			Factory<UndirectedGraph<Node, Edge>> graphFactory);
}
