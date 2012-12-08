package eecs598.experiments.analyzer;

import edu.uci.ics.jung.graph.Graph;
import eecs598.Edge;
import eecs598.Node;

public interface ExperimentAnalyzer {
	public void notifyTimestep(Graph<Node, Edge> graph, int timestep);
}
