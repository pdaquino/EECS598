package eecs598.experiments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import eecs598.Edge;
import eecs598.Node;
import eecs598.factories.EdgeFactory;
import eecs598.factories.GraphFactory;
import eecs598.factories.RatioNodeFactory;
import eecs598.probability.Distribution;

public class OnlyNonBayesian {
	
	private List<Distribution> possibleDistributions;
	private Factory<Node> nodeFactory; 
	private Factory<Edge> edgeFactory;
	private Factory<UndirectedGraph<Node, Edge>> graphFactory;
		
	public OnlyNonBayesian(List<Distribution> possibleDistributions) {
		super();
		this.possibleDistributions = possibleDistributions;
		this.nodeFactory = new RatioNodeFactory(1.0, possibleDistributions, null, null);
		this.edgeFactory = new EdgeFactory();
		this.graphFactory = new GraphFactory();
	}

	public void runErdosRenyi(ExperimentController controller, int numVertices, double p) {
		ErdosRenyiGenerator<Node, Edge> erdosRenyiGenerator = new ErdosRenyiGenerator<Node, Edge>(
				graphFactory, nodeFactory, edgeFactory, numVertices, p);
		
		Graph<Node, Edge> graph = erdosRenyiGenerator.create();
		
		controller.run(graph, 1000);
		
		ConvergenceInspector convergenceInspector = new ConvergenceInspector();
		boolean converged = convergenceInspector.haveConverged(graph.getVertices());
		System.out.println("\tConvergence: " + converged);
		if(converged) {
			System.out.println("\tParameter: " + convergenceInspector.getConvergedParameter(graph.getVertices()));
		}
	}
	
	public static void runSanityCheck() {
		int uniformId = 42;
		System.out.println("SanityCheck: Zero and Uniform, Uniform ID = " + uniformId);
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getZeroAndUniform(uniformId, possibleDistributions);
		OnlyNonBayesian onlyNb = new OnlyNonBayesian(possibleDistributions);
		
		onlyNb.runErdosRenyi(new ExperimentController(trueDistribution), 10, 0.2);
	}
	
	public static void run2GaussiansEasy() {
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getTwoGaussiansEasy(possibleDistributions);
		System.out.println("Two Gaussians Easy: True mean = " + trueDistribution.getParameter());
		
		OnlyNonBayesian onlyNb = new OnlyNonBayesian(possibleDistributions);
		
		onlyNb.runErdosRenyi(new ExperimentController(trueDistribution), 10, 0.2);
	}
	
	public static void run2GaussiansHard() {
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getTwoGaussiansHard(possibleDistributions);
		System.out.println("Two Gaussians Hard: True mean = " + trueDistribution.getParameter());
		
		OnlyNonBayesian onlyNb = new OnlyNonBayesian(possibleDistributions);
		
		onlyNb.runErdosRenyi(new ExperimentController(trueDistribution), 10, 0.2);
	}
	
	public static void main(String[] args) {
		//runSanityCheck();
		run2GaussiansEasy();
		run2GaussiansHard();
	}
}
