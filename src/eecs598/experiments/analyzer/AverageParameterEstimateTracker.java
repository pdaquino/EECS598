package eecs598.experiments.analyzer;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;
import eecs598.Edge;
import eecs598.Node;

/**
 * Tracks the parameter estimate average for DeGroot nodes.
 * @author Pedro
 *
 */
public class AverageParameterEstimateTracker implements ExperimentAnalyzer {
	
	/**
	 * Where to output data.
	 */
	private PrintStream stream;	
	
	private NodeType nodeTypeToBeTracked;


	public AverageParameterEstimateTracker(PrintStream stream) {
		super();
		this.stream = stream;
	}
	
	public PrintStream getStream() {
		return stream;
	}


	public void setStream(PrintStream stream) {
		this.stream = stream;
	}

	@Override
	public void notifyTimestep(Graph<Node, Edge> graph, int timestep) {
		Collection<Node> nodes = graph.getVertices();
		Double averageParamEstimate = AverageParameterEstimate.getAverageParameterEstimate(nodes);
		StringBuilder logBlder = new StringBuilder();
		logBlder.append(timestep).append("\t").append(averageParamEstimate);
		stream.println(logBlder.toString());
	}

}
