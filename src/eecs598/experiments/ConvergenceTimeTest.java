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
				if(graph.getVertexCount() == 375) {
					throw new MaxTimestepsExceededException();
				}
			} catch(MaxTimestepsExceededException e) {
				String filename =  "no_convergence_" + new Date().getTime() + ".jung";
				saveGraph(graph, filename); 
				System.out.println("\tNo convergence: saved to " + filename);
				System.out.println("\tTrue mean: " + setup.getTrueDistribution().getParameter());
				if(graph.getVertexCount() == 375)  break;
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
		String arg = args[0];
		//String arg = "complete"; 
		if(arg.equals("kleinberg")) {
			runKleinberg();
		} else if(arg.equals("barabasi")) {
			runBarabasiAlbert();
		} else if(arg.equals("eppstein")) {
			runEppstein();
		} else if(arg.equals("complete")) {
			runComplete();
		}
	}
	
	public static void runKleinberg() throws IOException {
		int minLatticeSize = 2;
		int maxLatticeSize = 30;
		System.out.format("Kleinberg Small World\n");
		System.out.println("Size\tMean\tStdDev");
		for(int latticeSize = minLatticeSize; latticeSize < maxLatticeSize; latticeSize++) {
			ConvergenceTimeTest test = new ConvergenceTimeTest();
			test.setGeneratorFactory(new GeneratorFactories.KleinbergGenerator(latticeSize, 2));
			test.setConvergenceTestGranularity(Math.max(1, latticeSize * latticeSize / 100 / 2));
			test.setMaxTimesteps(2000);
			MeanAndStdDev convergenceTime = test.run(200, ExperimentalSetup.nGaussiansMediumFactory(2));
			System.out.println(latticeSize*latticeSize + "\t" + convergenceTime.getMean() + "\t" + convergenceTime.getStdDev());
		}
	}
	
	public static void runBarabasiAlbert() throws IOException {
		int initialNodeCount = 10;
		int edgesPerTimestep = 5;
		int minNumTimesteps = 5; // min total size is 15
		int maxNumTimesteps = 490; // max total size is 500

		System.out.format("Barbasi-Albert Preferential Attachment (initial node count = %d, edges per timestep = %d,"+
		"minNumTimesteps = %d, maxNumTimesteps = %d\n", initialNodeCount, edgesPerTimestep, minNumTimesteps, maxNumTimesteps);
		
		System.out.println("Size\tMean\tStdDev");
		for(int numTimesteps = minNumTimesteps; numTimesteps < maxNumTimesteps; numTimesteps+=20) {
			int totalNodeCount = initialNodeCount + numTimesteps;
			ConvergenceTimeTest test = new ConvergenceTimeTest();
			test.setGeneratorFactory(new GeneratorFactories.BarabasiAlbertFactory(initialNodeCount, edgesPerTimestep, numTimesteps));
			test.setConvergenceTestGranularity(Math.max(1, (totalNodeCount) / 100 / 2));
			test.setMaxTimesteps(2000);
			MeanAndStdDev convergenceTime = test.run(200, ExperimentalSetup.nGaussiansMediumFactory(2));
			System.out.println(totalNodeCount + "\t" + convergenceTime.getMean() + "\t" + convergenceTime.getStdDev());
		}
	}
	
	public static void runEppstein() throws IOException {
		int generationTimesteps = 50;
		int edgeToNodeRatio = 3;
		int minNodeCount = 15; // min total size is 15
		int maxNodeCount = 500; // max total size is 500

		System.out.format("Eppstein Power Law Attachment (minNodeCount = %d, maxNodeCount = %d,"+
		"edgeToNodeRatio = %d, generationTimesteps = %d\n", minNodeCount, maxNodeCount, edgeToNodeRatio, generationTimesteps);
		
		System.out.println("Size\tMean\tStdDev");
		for(int nodeCount = minNodeCount; nodeCount < maxNodeCount; nodeCount+=20) {
			ConvergenceTimeTest test = new ConvergenceTimeTest();
			test.setGeneratorFactory(new GeneratorFactories.EppsteinFactory(nodeCount, edgeToNodeRatio * nodeCount, generationTimesteps));
			test.setConvergenceTestGranularity(Math.max(1, (nodeCount) / 100 / 2));
			test.setMaxTimesteps(2000);
			MeanAndStdDev convergenceTime = test.run(200, ExperimentalSetup.nGaussiansMediumFactory(2));
			System.out.println(nodeCount + "\t" + convergenceTime.getMean() + "\t" + convergenceTime.getStdDev());
		}
	}
	
	/**
	 * Compelte graph is an Erdos-Renyi with p=1.
	 * @throws IOException
	 */
	public static void runComplete() throws IOException {
		int minNodeCount = 15; // min total size is 15
		int maxNodeCount = 500; // max total size is 500

		System.out.format("Complete Graph (minNodeCount = %d, maxNodeCount = %d\n", minNodeCount, maxNodeCount);
		
		System.out.println("Size\tMean\tStdDev");
		for(int nodeCount = minNodeCount; nodeCount < maxNodeCount; nodeCount+=20) {
			ConvergenceTimeTest test = new ConvergenceTimeTest();
			test.setGeneratorFactory(new GeneratorFactories.ErdosRenyiFactory(nodeCount, 1.0));
			test.setConvergenceTestGranularity(Math.max(1, (nodeCount) / 100 / 2));
			test.setMaxTimesteps(2000);
			MeanAndStdDev convergenceTime = test.run(200, ExperimentalSetup.nGaussiansMediumFactory(2));
			System.out.println(nodeCount + "\t" + convergenceTime.getMean() + "\t" + convergenceTime.getStdDev());
		}
	}
}
