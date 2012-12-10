package eecs598.experiments;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import eecs598.Edge;
import eecs598.Node;
import eecs598.experiments.visualization.NetworkVisualizer;
import eecs598.probability.Gaussian;

public class LoadGraph {
	public static void main(String args[]) throws ClassNotFoundException, IOException {
		FileInputStream underlyingStream2 = new FileInputStream( "no_convergence_1355162296715.jung" ); 
		//FileInputStream underlyingStream2 = new FileInputStream( "no_conv_2.jung" );
	    ObjectInputStream deserializer = new ObjectInputStream( underlyingStream2 );
	    UndirectedSparseGraph<Node, Edge> loadedGraph = (UndirectedSparseGraph<Node, Edge>) deserializer.readObject(); //EXCEPTION THROWN HERE
	    deserializer.close();
	    underlyingStream2.close();
	    
	    NetworkVisualizer visualizer = new NetworkVisualizer();
	    visualizer.showGraph(loadedGraph);
	    
	    ConvergenceInspector inspector = new ConvergenceInspector();
	    System.out.println("Convergence before: " + inspector.haveConverged(loadedGraph.getVertices()));
	    
	    ExperimentController controller = new ExperimentController(new Gaussian(80.0));
	    //controller.run(loadedGraph, 400);
	    visualizer.refresh();
	    controller.run(loadedGraph, 1500);
	    
	    System.out.println("Convergence after: " + inspector.haveConverged(loadedGraph.getVertices()));
	    //visualizer.showGraph(loadedGraph);
	}
}
