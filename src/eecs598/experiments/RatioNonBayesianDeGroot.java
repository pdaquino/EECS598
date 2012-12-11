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
import eecs598.DistanceBeliefEstimator;
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

        static private int stepsToConvergence;
	
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
				new NopParameterEstimator(), new DistanceBeliefEstimator()/*new ManyRandomDrawsBeliefEstimator(distributionFactory)*/);
		this.edgeFactory = new EdgeFactory();
		this.graphFactory = new GraphFactory();
	}

	public Graph<Node, Edge> runExperiment(ExperimentController controller, int numVertices, double p) {
            //		ErdosRenyiGenerator<Node, Edge> erdosRenyiGenerator = new ErdosRenyiGenerator<Node, Edge>(
            //				graphFactory, nodeFactory, edgeFactory, numVertices, p);
		
                //		Graph<Node, Edge> graph = erdosRenyiGenerator.create();
                //Graph<Node, Edge> graph = new GeneratorFactories.ErdosRenyiFactory(15, 1.0).create(nodeFactory, edgeFactory, graphFactory).create();
            Graph<Node, Edge> graph = new GeneratorFactories.KleinbergGenerator(5, 2).create(nodeFactory, edgeFactory, graphFactory).create();
                GraphConnector connector = new GraphConnector();
                connector.makeConnected(graph, edgeFactory);
		
                final int numTimesteps = 2000;
		//controller.run(graph, numTimesteps);
                RatioNonBayesianDeGroot.stepsToConvergence = controller.runUntilConvergence(graph, numTimesteps);
		
		ConvergenceInspector convergenceInspector = new ConvergenceInspector();
		boolean converged = convergenceInspector.haveConverged(graph.getVertices());
//		System.out.println("\tConvergence: " + converged);
		if(converged) {
                    //System.out.println("\tParameter: " + convergenceInspector.getConvergedParameter(graph.getVertices()));
                    System.out.println("Converged in " + stepsToConvergence + " steps");
		} else {
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
		
		return onlyNb.runExperiment(new ExperimentController(trueDistribution), 10, 0.2);
	}
	
	public static Graph<Node, Edge> run2GaussiansEasy(double ratio) {
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getTwoGaussiansEasy(possibleDistributions);
		System.out.println("Two Gaussians Easy: True mean = " + trueDistribution.getParameter());
		
		RatioNonBayesianDeGroot onlyNb = new RatioNonBayesianDeGroot(ratio, possibleDistributions, new GaussianFactory());
		
		ExperimentController controller = new ExperimentController(trueDistribution);
		//controller.attachAnalyzer(new AverageBeliefTracker(System.out));
		controller.attachAnalyzer(new AverageParameterEstimateTracker(System.out));
		
		return onlyNb.runExperiment(controller, 10, 0.2);
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
		
		return onlyNb.runExperiment(controller, 20, 0.1);
	}
	
	public static Graph<Node, Edge> runNGaussiansHard(int n, double ratio) {
		List<Distribution> possibleDistributions = new ArrayList<>();
		Distribution trueDistribution = DistributionRepository.getNGaussiansMedium(2, possibleDistributions);
		//System.out.println(n + " Gaussians Hard: True mean = " + trueDistribution.getParameter());
		
		RatioNonBayesianDeGroot mixedNodes = new RatioNonBayesianDeGroot(ratio, possibleDistributions, new GaussianFactory());
		
		ExperimentController controller = new ExperimentController(trueDistribution);
		//controller.attachAnalyzer(new AverageBeliefTracker(System.out, NodeType.NonBayesian));
		//controller.attachAnalyzer(new AverageBeliefTracker(System.err, NodeType.DeGroot));
		//controller.attachAnalyzer(new ParameterEstimateTracker(System.err));
                //controller.attachAnalyzer(new AverageParameterEstimateTracker(System.err));
		
                int numNodes = 30;
		return mixedNodes.runExperiment(controller, numNodes, 0.1);
	}
	
	public static void main(String[] args) throws IOException {
            //double ratio = 0.8;
		//runSanityCheck(ratio);
		//run2GaussiansEasy(ratio);
		//Graph graph = run2GaussiansHard(ratio);
            final int numTrialsPerRatio = 10;
            Graph<Node, Edge> graph = null;
            System.out.println("Ratio\tConvergence/"
                    + numTrialsPerRatio + "\tMeanStepsToConvergence\tStdDevStepsToConvergence");
                for(double ratio = 1.0; ratio >= 0.0; ratio -= 0.05) {
                    List<Integer> stepsToConvergence = new ArrayList<>();
                    for(int i = 0; i < numTrialsPerRatio; i++) {
                            graph = runNGaussiansHard(20, ratio);
                            ConvergenceInspector convergenceInspector = new ConvergenceInspector();
                            boolean converged = convergenceInspector.haveConverged(graph.getVertices());
                            if(converged) {
                                /* System.out.println("Ratio " + ratio + " converged in "
                                   + RatioNonBayesianDeGroot.stepsToConvergence + " steps");*/
                                stepsToConvergence.add(RatioNonBayesianDeGroot.stepsToConvergence);
                            }/* else {
                                NetworkVisualizer visualizer = new NetworkVisualizer();
                                visualizer.showGraph(graph);

                                FileOutputStream underlyingStream = new FileOutputStream("no_convergence_" + i + ".jung");
                                ObjectOutputStream serializer = new ObjectOutputStream(underlyingStream);
                                serializer.writeObject(graph);
                                serializer.close();
                                underlyingStream.close();   
                            }*/
                    }
                    int totalStepsToConvergence = 0;
                    for(int convergenceSteps : stepsToConvergence) {
                        totalStepsToConvergence += convergenceSteps;
                    }
                    double meanStepsToConvergence = (double)totalStepsToConvergence / stepsToConvergence.size();

                    // Calculate std deviation
                    double sumOfSquaredDiffs = 0;
                    for(int convergenceSteps : stepsToConvergence) {
                        sumOfSquaredDiffs += Math.pow(convergenceSteps - meanStepsToConvergence, 2);
                    }
                    double stdDevStepsToConvergence = Math.sqrt(sumOfSquaredDiffs / stepsToConvergence.size());
                    System.out.printf("*****\t%.2f\t%d\t%f\t%f%n", ratio, stepsToConvergence.size(),
                            meanStepsToConvergence, stdDevStepsToConvergence);
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
