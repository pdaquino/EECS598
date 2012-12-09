package eecs598.experiments;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import eecs598.Edge;
import eecs598.ManyRandomDrawsBeliefEstimator;
import eecs598.Node;
import eecs598.NopParameterEstimator;
import eecs598.experiments.visualization.NetworkVisualizer;
import eecs598.factories.EdgeFactory;
import eecs598.factories.GraphFactory;
import eecs598.factories.RatioNodeFactory;
import eecs598.util.MeanAndStdDev;

public class ConvergenceTimeTest {
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

	/**
	 * Returns the average time to convergence.
	 * @param nRuns
	 * @param setupFactory
	 * @return
	 * @throws IOException
	 */
	public MeanAndStdDev run(int nRuns, Factory<ExperimentalSetup> setupFactory) throws IOException {
		List<Double> convergenceTimes = new ArrayList<Double>();
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
		
		for(int i = 0; i < nRuns; i++) {
			Graph<Node, Edge> graph = generator.create();
			try {				
				int timesteps = experiment.runUntilConvergence(
						generatorFactory, nodeFactory, edgeFactory, graphFactory);
				//System.out.println(timesteps);
				convergenceTimes.add((double)timesteps);
			} catch(MaxTimestepsExceededException e) {
				String filename =  "no_convergence_" + new Date().getTime() + ".jung";
				saveGraph(graph, filename); 
				System.out.println("\tNo convergence: saved to " + filename);
				System.out.println("\tTrue mean: " + setup.getTrueDistribution().getParameter());
			}
		}
		return MeanAndStdDev.fromSample(convergenceTimes);
	}

	private void saveGraph(Graph<Node, Edge> graph, String filename)
			throws FileNotFoundException, IOException {
		FileOutputStream underlyingStream = new FileOutputStream(filename);
		ObjectOutputStream serializer = new ObjectOutputStream(underlyingStream);
		serializer.writeObject(graph);
		serializer.close();
		underlyingStream.close();
	}
	
	public static void main(String[] args) throws IOException {
		int minLatticeSize = 2;
		int maxLatticeSize = 20;
		System.out.println("Size\tMean\tStdDev");
		for(int latticeSize = minLatticeSize; latticeSize < maxLatticeSize; latticeSize++) {
			ConvergenceTimeTest test = new ConvergenceTimeTest();
			test.setGeneratorFactory(new GeneratorFactories.KleinbergGenerator(latticeSize, 2));
			test.setConvergenceTestGranularity(Math.max(1, latticeSize * latticeSize / 100 / 2));
			test.setMaxTimesteps(1000);
			MeanAndStdDev convergenceTime = test.run(60, ExperimentalSetup.nGaussiansHardFactory(2));
			System.out.println(latticeSize*latticeSize + "\t" + convergenceTime.getMean() + "\t" + convergenceTime.getStdDev());
		}
	}
}
