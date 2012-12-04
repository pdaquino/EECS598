package eecs598.factories;

import org.apache.commons.collections15.Factory;

import eecs598.Edge;

/**
 * Creates edges.
 * @author Pedro
 *
 */
public class EdgeFactory implements Factory<Edge> {

	private int i = 0;
	@Override
	public Edge create() {
		return new Edge(i++);
	}

}
