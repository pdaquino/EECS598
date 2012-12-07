package eecs598.experiments;

import java.util.Collection;

import edu.uci.ics.jung.graph.Graph;
import eecs598.Edge;
import eecs598.Node;
import eecs598.probability.Distribution;

/**
 * Runs an experiment, drawing values from the true distribution and feeding them
 * to every node in the graph.
 * @author Pedro
 *
 */
public class ExperimentController {
	private Distribution trueDistribution;

	public ExperimentController(Distribution trueDistribution) {
		super();
		this.trueDistribution = trueDistribution;
	}
	
	public void run(Graph<Node, Edge> graph, int numTimesteps) {
		for(int i = 0; i < numTimesteps; i++) {
			Collection<Node> nodes = graph.getVertices();
			for(Node node : nodes) {
				node.newSignal(graph.getNeighbors(node), trueDistribution.draw());
			}
		}
	}
}
