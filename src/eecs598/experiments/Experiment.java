package eecs598.experiments;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import eecs598.Edge;
import eecs598.Node;

/**
 * 
 * @author Pedro
 *
 */
public class Experiment {
	private ExperimentController experimentController;
	
	private int maxConvergenceTimesteps = 10000;
	

	/**
	 * In runUntilConvergence, test for convergence every convergenceTestStep steps.
	 */
	private int convergenceTestStep = 10;

	
	public Experiment(ExperimentalSetup setup) {
		super();
		this.experimentController = new ExperimentController(setup.getTrueDistribution());
	}

	public Graph<Node, Edge> run(GeneratorFactory generatorFactory, Factory<Node> nodeFactory,
			Factory<Edge> edgeFactory, Factory<UndirectedGraph<Node, Edge>> graphFactory, int numTimesteps) {
		GraphGenerator<Node, Edge> generator = generatorFactory.create(nodeFactory, edgeFactory, graphFactory);
		Graph<Node, Edge> graph = generator.create();
		return run(graph, numTimesteps);
	}

	public Graph<Node, Edge> run(Graph<Node, Edge> graph, int numTimesteps) {
		getExperimentController().run(graph, numTimesteps);
		return graph;
	}

	/**
	 * Runs the experiment until the graph converges, or throws an exception if the graph didn't converge before
	 * maxConvergenceTimesteps.
	 * @param generatorFactory
	 * @param controller
	 * @return the number of times steps, approximately.
	 * @throws MaxTimestepsExceededException 
	 */
	public int runUntilConvergence(GeneratorFactory generatorFactory, Factory<Node> nodeFactory,
			Factory<Edge> edgeFactory, Factory<UndirectedGraph<Node, Edge>> graphFactory)
					throws MaxTimestepsExceededException {
		GraphGenerator<Node, Edge> generator = generatorFactory.create(nodeFactory, edgeFactory, graphFactory);
		Graph<Node, Edge> graph = generator.create();
		return runUntilConvergence(graph);
	}

	public int runUntilConvergence(Graph<Node, Edge> graph) throws MaxTimestepsExceededException {
		/**
		 * Run the controller for convergenceTestStep timesteps at a time and check for
		 * convergence. Note the last part of the for loop.
		 */
		ConvergenceInspector inspector = new ConvergenceInspector();
		for(int stepsSoFar = 0; stepsSoFar < maxConvergenceTimesteps; stepsSoFar += convergenceTestStep) {
			if(inspector.haveConverged(graph.getVertices())) {
				return stepsSoFar;
			}
			getExperimentController().run(graph, convergenceTestStep);
		}
		throw new MaxTimestepsExceededException();
	}
	
	public int getMaxConvergenceTimesteps() {
		return maxConvergenceTimesteps;
	}

	public void setMaxConvergenceTimesteps(int maxConvergenceTimesteps) {
		this.maxConvergenceTimesteps = maxConvergenceTimesteps;
	}

	public int getConvergenceTestStep() {
		return convergenceTestStep;
	}

	public void setConvergenceTestStep(int convergenceTestStep) {
		this.convergenceTestStep = convergenceTestStep;
	}
	
	public ExperimentController getExperimentController() {
		return experimentController;
	}
}
