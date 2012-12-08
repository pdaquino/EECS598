package eecs598.experiments.analyzer;

import java.io.BufferedWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;
import eecs598.Edge;
import eecs598.Node;
import eecs598.NonBayesianNode;

/**
 * This analyzer plots the average belief in all possible parameter values.
 * It outputs data in the following format:
 * TIMESTEP HYPOTHESIS1 HYPOTHESIS1_BELIEF HYPOTHESIS2 HYPOTHESIS2_BELIEF .... HYPOTHESISN HYPOTHESISN_BELIEF
 * 
 * where each column is separated by tabs, every hypothesis is a possible parameter,  and
 * hypothesisi_belief is the average belief in [0, 1] over all nodes in that hypothesis.
 * 
 * @author Pedro
 *
 */
public class AverageBeliefTracker implements ExperimentAnalyzer {
	
	/**
	 * Where to output data.
	 */
	private PrintStream stream;	
	
	private NodeType nodeTypeToBeTracked;


	public AverageBeliefTracker(PrintStream stream) {
		super();
		this.stream = stream;
		this.nodeTypeToBeTracked = NodeType.All;
	}
		
	public AverageBeliefTracker(PrintStream stream, NodeType nodeTypeToBeTracked) {
		super();
		this.stream = stream;
		this.nodeTypeToBeTracked = nodeTypeToBeTracked;
	}


	public PrintStream getStream() {
		return stream;
	}


	public void setStream(PrintStream stream) {
		this.stream = stream;
	}


	public NodeType getNodeTypeToBeTracked() {
		return nodeTypeToBeTracked;
	}


	public void setNodeTypeToBeTracked(NodeType nodeTypeToBeTracked) {
		this.nodeTypeToBeTracked = nodeTypeToBeTracked;
	}


	@Override
	public void notifyTimestep(Graph<Node, Edge> graph, int timestep) {
		Collection<Node> nodes = graph.getVertices();
		List<ParameterBeliefPair> parameterBeliefs = AverageParameterBelief.getAverageBeliefs(nodes, nodeTypeToBeTracked);
		StringBuilder logBlder = new StringBuilder();
		logBlder.append(timestep).append("\t");
		for(ParameterBeliefPair paramBelief : parameterBeliefs) {
			//logBlder.append(paramBelief.parameter).append("\t");
			logBlder.append(paramBelief.belief).append("\t");
		}
		stream.println(logBlder.toString());
	}

}
