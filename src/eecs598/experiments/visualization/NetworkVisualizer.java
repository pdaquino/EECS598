package eecs598.experiments.visualization;

import java.awt.Dimension;
import java.awt.Shape;
import java.io.IOException;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.layout.PersistentLayoutImpl;
import eecs598.Node;
import eecs598.Edge;

public class NetworkVisualizer {
	
	private JFrame frame = null;
	
	public void showGraph(Graph<Node, Edge> graph) {
		
		Layout<Node, Edge> layout = new CircleLayout<>(graph);
		layout.setSize(new Dimension(700,700)); // sets the initial size of the layout space

        // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
		
        BasicVisualizationServer<Node,Edge> vv = new BasicVisualizationServer<>(layout);
        vv.setPreferredSize(new Dimension(700,700)); //Sets the viewing area size
        vv.getRenderContext().setVertexLabelTransformer(new Transformers.VertexLabel());
        vv.getRenderContext().setVertexFillPaintTransformer(new Transformers.VertexFillPaint());
        vv.getRenderContext().setVertexShapeTransformer(new Transformers.VertexShape());
        //Shape s = vv.getRenderContext().getVertexShapeTransformer().transform(graph.getVertices().iterator().next());
        
        frame = new JFrame("Kleinberg Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv); 
        frame.pack();
        frame.setVisible(true);       
	}

	public void refresh() {
		frame.repaint();
		
	}
	
}
