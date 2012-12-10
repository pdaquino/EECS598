package eecs598.experiments;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import eecs598.Edge;
import eecs598.Node;

public class GeneratorFactories {
	public static class KleinbergGenerator implements GeneratorFactory {
		private int latticeSize;
		private double clusteringExponent;
		
		public KleinbergGenerator(int latticeSize, double clusteringExponent) {
			super();
			this.latticeSize = latticeSize;
			this.clusteringExponent = clusteringExponent;
		}



		@Override
		public GraphGenerator<Node, Edge> create(Factory<Node> nodeFactory,
				Factory<Edge> edgeFactory,
				Factory<UndirectedGraph<Node, Edge>> graphFactory) {
			return new KleinbergSmallWorldGenerator<>(graphFactory, nodeFactory, edgeFactory, latticeSize, clusteringExponent);
		}
	}
	
	public static class BarabasiAlbertFactory implements GeneratorFactory {

		private int numInitialVertices;
		private int numTimeSteps;
		private int edgesPerNewVertex;
		
		/**
		 * Creates a factory of Barabasi-Albert (preferential attachment) generators.
		 * @param numInitialVertices the number of initial vertices.
		 * @param edgesPerNewVertex how many edges are connected to every vertex added to the graph.
		 * @param numTimeSteps for how many timesteps the generator should evolve the graph. At every timestep a new
		 * vertex is added. 
		 */
		public BarabasiAlbertFactory(int numInitialVertices, int edgesPerNewVertex, int numTimeSteps) {
			super();
			this.numInitialVertices = numInitialVertices;
			this.edgesPerNewVertex = edgesPerNewVertex;
			this.numTimeSteps = numTimeSteps;
		}



		@Override
		public GraphGenerator<Node, Edge> create(Factory<Node> nodeFactory,
				Factory<Edge> edgeFactory,
				Factory<UndirectedGraph<Node, Edge>> notUsed) {
			// XXX hack! not using the graphFactory.
			Factory<Graph<Node, Edge>> graphFactory = new Factory<Graph<Node, Edge>>() {

				@Override
				public Graph<Node, Edge> create() {
					return new UndirectedSparseGraph<>();
				}
			};
			Set<Node> seedNodes = new HashSet<Node>();
			BarabasiAlbertGenerator<Node, Edge> generator = new BarabasiAlbertGenerator<Node, Edge>(
					graphFactory, nodeFactory, edgeFactory,
					this.numInitialVertices, this.edgesPerNewVertex, seedNodes );
			generator.evolveGraph(numTimeSteps);
			return generator;
		}
	}
	
	public static class EppsteinFactory implements GeneratorFactory {

		private int numTimesteps;
		private int numEdges;
		private int numNodes;
		
		public EppsteinFactory(int numNodes, int numEdges,int numTimesteps)  {
			super();
			this.numTimesteps = numTimesteps;
			this.numEdges = numEdges;
			this.numNodes = numNodes;
		}



		@Override
		public GraphGenerator<Node, Edge> create(Factory<Node> nodeFactory,
				Factory<Edge> edgeFactory,
				Factory<UndirectedGraph<Node, Edge>> notUsed) {
			// XXX hack! not using the graphFactory.
			Factory<Graph<Node, Edge>> graphFactory = new Factory<Graph<Node, Edge>>() {

				@Override
				public Graph<Node, Edge> create() {
					return new UndirectedSparseGraph<>();
				}
			};
			EppsteinPowerLawGenerator<Node, Edge> gen = new EppsteinPowerLawGenerator<Node,Edge>(
					graphFactory, nodeFactory, edgeFactory, numNodes, numEdges, numTimesteps);
			return gen;
		}
	}
	
	public static class ErdosRenyiFactory implements GeneratorFactory {
		private int nodeCount;
		private double p;
		
		public ErdosRenyiFactory(int nodeCount, double p) {
			super();
			this.nodeCount = nodeCount;
			this.p = p;
		}



		@Override
		public GraphGenerator<Node, Edge> create(Factory<Node> nodeFactory,
				Factory<Edge> edgeFactory,
				Factory<UndirectedGraph<Node, Edge>> graphFactory) {
			return new ErdosRenyiGenerator<>(graphFactory, nodeFactory, edgeFactory, nodeCount, p);
		}
	}
}
