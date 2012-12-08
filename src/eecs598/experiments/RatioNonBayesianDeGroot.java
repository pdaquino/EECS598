package eecs598.experiments;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import eecs598.Edge;
import eecs598.ManyRandomDrawsBeliefEstimator;
import eecs598.Node;
import eecs598.NopParameterEstimator;
import eecs598.RandomDrawBeliefEstimator;
import eecs598.experiments.analyzer.AverageBeliefTracker;
import eecs598.experiments.analyzer.NodeType;
import eecs598.experiments.analyzer.AverageParameterEstimateTracker;
import eecs598.experiments.visualization.NetworkVisualizer;
import eecs598.factories.EdgeFactory;
import eecs598.factories.GraphFactory;
import eecs598.factories.RatioNodeFactory;
import eecs598.factories.probability.DistributionFactory;
import eecs598.factories.probability.GaussianFactory;
import eecs598.probability.Distribution;

public class RatioNonBayesianDeGroot {
	
	private double ratioNonBayesian;
	
	private List<Distribution> possibleDistributions;
	private Factory<Node> nodeFactory; 
	private Factory<Edge> edgeFactory;
	private Factory<UndirectedGraph<Node, Edge>> graphFactory;
		
	public RatioNonBayesianDeGroot(double ratioNonBayesian,
			List<Distribution> possibleDistributions,
			DistributionFactory distributionFactory) {
		super();
		this.ratioNonBayesian = ratioNonBayesian;
		this.possibleDistributions = possibleDistributions;
		this.nodeFactory = new RatioNodeFactory(
				ratioNonBayesian, possibleDistributions,
				new NopParameterEstimator(), new ManyRandomDrawsBeliefEstimator(distributionFactory));
		this.edgeFactory = new EdgeFactory();
		this.graphFactory = new GraphFactory();
	}

	public Graph<Node, Edge> runErdosRenyi(ExperimentController controller, int numVertices, double p) {
		ErdosRenyiGenerator<Node, Edge> erdosRenyiGenerator = new ErdosRenyiGenerator<Node, Edge>(
				graphFactory, nodeFactory, edgeFactory, numVertices, p);
		
		Graph<Node, Edge> graph = erdosRenyiGenerator.create();
		
		controller.run(graph, 5000);
		
		ConvergenceInspector convergenceInspector = new ConvergenceInspector();
		boolean converged = convergenceInspector.haveConverged(graph.getVertices());
//		System.out.println("\tConvergence: " + converged);
//		if(converged) {
//			System.out.println("\tParameter: " + convergenceInspector.getConvergedParameter(graph.getVertices()));
//		}
		if(!converged) {
			System.out.println("Did not converge");
		}
		return graph;
	}
	
	public static Graph<Node, Edge> runSanityCheck(double ratio) {
		int uniformId = 42;
		System.out.println("SanityCheck: Zero and Uniform, Uniform ID = " + uniformId);
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getZeroAndUniform(uniformId, possibleDistributions);
		RatioNonBayesianDeGroot onlyNb = new RatioNonBayesianDeGroot(ratio, possibleDistributions, new GaussianFactory());
		
		return onlyNb.runErdosRenyi(new ExperimentController(trueDistribution), 10, 0.2);
	}
	
	public static Graph<Node, Edge> run2GaussiansEasy(double ratio) {
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getTwoGaussiansEasy(possibleDistributions);
		System.out.println("Two Gaussians Easy: True mean = " + trueDistribution.getParameter());
		
		RatioNonBayesianDeGroot onlyNb = new RatioNonBayesianDeGroot(ratio, possibleDistributions, new GaussianFactory());
		
		ExperimentController controller = new ExperimentController(trueDistribution);
		//controller.attachAnalyzer(new AverageBeliefTracker(System.out));
		controller.attachAnalyzer(new AverageParameterEstimateTracker(System.out));
		
		return onlyNb.runErdosRenyi(controller, 10, 0.2);
	}
	
	public static Graph<Node, Edge> run2GaussiansHard(double ratio) {
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getTwoGaussiansHard(possibleDistributions);
		System.out.println("Two Gaussians Hard: True mean = " + trueDistribution.getParameter());
		
		RatioNonBayesianDeGroot onlyNb = new RatioNonBayesianDeGroot(ratio, possibleDistributions, new GaussianFactory());
		
		ExperimentController controller = new ExperimentController(trueDistribution);
		controller.attachAnalyzer(new AverageBeliefTracker(System.out, NodeType.NonBayesian));
		controller.attachAnalyzer(new AverageBeliefTracker(System.err, NodeType.DeGroot));
		//controller.attachAnalyzer(new ParameterEstimateTracker(System.err));
		
		return onlyNb.runErdosRenyi(controller, 20, 0.1);
	}
	
	public static Graph<Node, Edge> runNGaussiansHard(int n, double ratio) {
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getNGaussiansHard(n, possibleDistributions);
		System.out.println("Two Gaussians Hard: True mean = " + trueDistribution.getParameter());
		
		RatioNonBayesianDeGroot onlyNb = new RatioNonBayesianDeGroot(ratio, possibleDistributions, new GaussianFactory());
		
		ExperimentController controller = new ExperimentController(trueDistribution);
		//controller.attachAnalyzer(new AverageBeliefTracker(System.out, NodeType.NonBayesian));
		//controller.attachAnalyzer(new AverageBeliefTracker(System.err, NodeType.DeGroot));
		//controller.attachAnalyzer(new ParameterEstimateTracker(System.err));
		
		return onlyNb.runErdosRenyi(controller, 20, 0.1);
	}
	
	public static void main(String[] args) throws IOException {
		double ratio = 0.8;
		//runSanityCheck(ratio);
		//run2GaussiansEasy(ratio);
		//Graph graph = run2GaussiansHard(ratio);
		Graph<Node, Edge> graph = null;
		for(int i = 0; i < 100; i++) {
			graph = runNGaussiansHard(20, ratio);
			ConvergenceInspector convergenceInspector = new ConvergenceInspector();
			boolean converged = convergenceInspector.haveConverged(graph.getVertices());
			if(!converged) {
				NetworkVisualizer visualizer = new NetworkVisualizer();
				visualizer.showGraph(graph);
				
				FileOutputStream underlyingStream = new FileOutputStream("no_convergence_" + i + ".jung");
			    ObjectOutputStream serializer = new ObjectOutputStream(underlyingStream);
			    serializer.writeObject(graph);
			    serializer.close();
			    underlyingStream.close();   
			}
		}
		NetworkVisualizer visualizer = new NetworkVisualizer();
		visualizer.showGraph(graph);
		
		FileOutputStream underlyingStream = new FileOutputStream("temp.jung");
	    ObjectOutputStream serializer = new ObjectOutputStream(underlyingStream);
	    serializer.writeObject(graph);
	    serializer.close();
	    underlyingStream.close();   

	}
}
