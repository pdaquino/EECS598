package eecs598.experiments;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import eecs598.Edge;
import eecs598.ManyRandomDrawsBeliefEstimator;
import eecs598.Node;
import eecs598.NonBayesianNode;
import eecs598.NopParameterEstimator;
import eecs598.experiments.visualization.NetworkVisualizer;
import eecs598.factories.EdgeFactory;
import eecs598.factories.GraphFactory;
import eecs598.factories.RatioNodeFactory;
import eecs598.test.helpers.TestGraphs.NonBayesianFactory;

public class ConvergenceStressTest {
	
	private double ratioNonBayesian = 0.8;
	private GeneratorFactory generatorFactory;
	private int convergenceTestGranularity = 20;
	private int maxTimesteps = 500;
	
	public double getRatioNonBayesian() {
		return ratioNonBayesian;
	}

	public void setRatioNonBayesian(double ratioNonBayesian) {
		this.ratioNonBayesian = ratioNonBayesian;
	}

	public GeneratorFactory getGeneratorFactory() {
		return generatorFactory;
	}

	public void setGeneratorFactory(GeneratorFactory generatorFactory) {
		this.generatorFactory = generatorFactory;
	}

	public int getConvergenceTestGranularity() {
		return convergenceTestGranularity;
	}

	public void setConvergenceTestGranularity(int convergenceTestGranularity) {
		this.convergenceTestGranularity = convergenceTestGranularity;
	}

	public int getMaxTimesteps() {
		return maxTimesteps;
	}

	public void setMaxTimesteps(int maxTimesteps) {
		this.maxTimesteps = maxTimesteps;
	}

	public void run(int nRuns, Factory<ExperimentalSetup> setupFactory) throws IOException {
		for(int i = 0; i < nRuns; i++) {
			ExperimentalSetup setup = setupFactory.create();
			Experiment experiment = new Experiment(setup);
			experiment.setConvergenceTestStep(convergenceTestGranularity);
			experiment.setMaxConvergenceTimesteps(maxTimesteps);
			
			Factory<Node> nodeFactory = new RatioNodeFactory(
					ratioNonBayesian,
					setup.getPossibleDistributions(),
					new NopParameterEstimator(),
					new ManyRandomDrawsBeliefEstimator(setup.getDistributionFactory()));
			
			Factory<Edge> edgeFactory = new EdgeFactory();
			
			Factory<UndirectedGraph<Node, Edge>> graphFactory = new GraphFactory();
			
			GraphGenerator<Node, Edge> generator = generatorFactory.create(nodeFactory, edgeFactory, graphFactory);
			Graph<Node, Edge> graph = generator.create();
			try {
				
				int timesteps = experiment.runUntilConvergence(
						generatorFactory, nodeFactory, edgeFactory, graphFactory);
				System.out.println(timesteps);
				if(timesteps > 50) {
					String filename =  "slow_convergence_" + new Date().getTime() + ".jung";
					showAndSaveGraph(graph, filename);
					System.out.println("\tSlow convergence: saved to " + filename);
					System.out.println("\tTrue mean: " + setup.getTrueDistribution().getParameter());
				}
			} catch(MaxTimestepsExceededException e) {
				String filename =  "no_convergence_" + new Date().getTime() + ".jung";
				showAndSaveGraph(graph, filename); 
				System.out.println("\tNo convergence: saved to " + filename);
				System.out.println("\tTrue mean: " + setup.getTrueDistribution().getParameter());
				break;
			}
		}
	}

	private void showAndSaveGraph(Graph<Node, Edge> graph, String filename)
			throws FileNotFoundException, IOException {
		NetworkVisualizer visualizer = new NetworkVisualizer();
		visualizer.showGraph(graph);

		FileOutputStream underlyingStream = new FileOutputStream(filename);
		ObjectOutputStream serializer = new ObjectOutputStream(underlyingStream);
		serializer.writeObject(graph);
		serializer.close();
		underlyingStream.close();
	}
	
	public static void main(String[] args) throws IOException {
		ConvergenceStressTest stressTest = new ConvergenceStressTest();
		stressTest.setGeneratorFactory(new GeneratorFactories.KleinbergGenerator(10, 2));
		stressTest.setConvergenceTestGranularity(1);
		stressTest.setMaxTimesteps(100);
		stressTest.run(50, ExperimentalSetup.nGaussiansHardFactory(80));
	}
}
