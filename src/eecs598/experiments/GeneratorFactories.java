package eecs598.experiments;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.graph.UndirectedGraph;
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
}
