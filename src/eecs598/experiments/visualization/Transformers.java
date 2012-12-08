package eecs598.experiments.visualization;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.apache.commons.collections15.Transformer;

import eecs598.DeGrootNode;
import eecs598.Node;
import eecs598.NonBayesianNode;
import eecs598.probability.ProbabilityUtil;

public class Transformers {
	public static class VertexFillPaint implements Transformer<Node, Paint> {

		@Override
		public Paint transform(Node node) {
			if(node instanceof NonBayesianNode) {
				return Color.GREEN;
			} else {
				return Color.GRAY;
			}
		}
		
	}
	
	public static class VertexShape implements Transformer<Node, Shape> {

		@Override
		public Shape transform(Node node) {
			if(node instanceof NonBayesianNode) {
				return new Ellipse2D.Double(-10, -10, 20, 20);
			} else {
				return new Rectangle(-10, -10, 20, 20);
			}
		}
		
	}
	
	public static class VertexLabel implements Transformer<Node, String> {

		@Override
		public String transform(Node node) {
			if(node instanceof NonBayesianNode) {
				return String.format("%.2f", ProbabilityUtil.expectedValue(node.getBeliefs()));
			} else {
				return String.format("%.2f", ((DeGrootNode)node).getParameterEstimate());
			}
		}
	}
}
