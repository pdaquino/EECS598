package eecs598.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.Graph;

/**
 * Makes a graph fully connected.
 * @author Pedro
 *
 */
public class GraphConnector {
	public  <V, E> void makeConnected(Graph<V, E> graph, Factory<E> edgeFactory) {
		WeakComponentClusterer<V, E> clusterer = new WeakComponentClusterer<>();
		Set<Set<V>> components = clusterer.transform(graph);
		Set<V> firstComponent = components.iterator().next();
		V bridgeNode = firstComponent.iterator().next();
		
		for(Set<V> component : components) {
			if(component == firstComponent) {
				continue;
			}
			V nodeInOtherComponent = component.iterator().next();
			boolean succeeded = graph.addEdge(edgeFactory.create(), bridgeNode, nodeInOtherComponent);
			if(!succeeded) {
				throw new RuntimeException("Error connecting components");
			}
		}
	}
	
	public <V, E> boolean isConnected(Graph<V, E> graph) {
		WeakComponentClusterer<V, E> clusterer = new WeakComponentClusterer<>();
		Set<Set<V>> components = clusterer.transform(graph);
		return components.size() == 1;
	}
}
