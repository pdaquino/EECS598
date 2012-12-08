package eecs598.experiments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;
import eecs598.Edge;
import eecs598.Node;
import eecs598.experiments.analyzer.ExperimentAnalyzer;
import eecs598.probability.Distribution;

/**
 * Runs an experiment, drawing values from the true distribution and feeding them
 * to every node in the graph.
 * @author Pedro
 *
 */
public class ExperimentController {
	private Distribution trueDistribution;
	List<ExperimentAnalyzer> analyzers = new ArrayList<>();
	
	public ExperimentController(Distribution trueDistribution) {
		super();
		this.trueDistribution = trueDistribution;
	}
	
	public void attachAnalyzer(ExperimentAnalyzer analyzer) {
		this.analyzers.add(analyzer);
	}
	
	public void run(Graph<Node, Edge> graph, int numTimesteps) {
		notifyAllAnalyzers(0, graph);
		for(int i = 0; i < numTimesteps; i++) {
			Collection<Node> nodes = graph.getVertices();
			for(Node node : nodes) {
				node.newSignal(graph.getNeighbors(node), trueDistribution.draw());
			}
			notifyAllAnalyzers(i+1, graph);
		}
	}
	
	public void notifyAllAnalyzers(int timestep, Graph<Node, Edge> graph) {
		for(ExperimentAnalyzer analyzer : analyzers) {
			analyzer.notifyTimestep(graph, timestep);
		}
	}
}
