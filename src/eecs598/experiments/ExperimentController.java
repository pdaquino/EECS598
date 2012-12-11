package eecs598.experiments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;
import eecs598.Edge;
import eecs598.Node;
import eecs598.experiments.analyzer.ExperimentAnalyzer;
import eecs598.experiments.visualization.NetworkVisualizer;
import eecs598.probability.Distribution;

/**
 * Runs an experiment, drawing values from the true distribution and feeding them
 * to every node in the graph.
 * @author Pedro
 *
 */
public class ExperimentController {
	private Distribution trueDistribution;
	List<ExperimentAnalyzer> analyzers = new ArrayList<>();
	
	public ExperimentController(Distribution trueDistribution) {
		super();
		this.trueDistribution = trueDistribution;
	}
	
	public void attachAnalyzer(ExperimentAnalyzer analyzer) {
		this.analyzers.add(analyzer);
	}

	public void run(Graph<Node, Edge> graph, int numTimesteps) {
		notifyAllAnalyzers(0, graph);
		for(int i = 0; i < numTimesteps; i++) {
			Collection<Node> nodes = graph.getVertices();
			for(Node node : nodes) {
				node.newSignal(graph.getNeighbors(node), trueDistribution.draw());
			}
			notifyAllAnalyzers(i+1, graph);
		}
	}
	
        /**
         * Run the experiment, but stop early if experiment convergence.  Returns number
         * of steps taken to converge, or -1 if convergence was not reached.
         */
        public int runUntilConvergence(Graph<Node, Edge> graph, int maxTimesteps) {
            notifyAllAnalyzers(0, graph);
            //NetworkVisualizer visualizer = new NetworkVisualizer();
		    //visualizer.showGraph(graph);
            for(int i=0; i < maxTimesteps; i++) {
                //showAndStop(visualizer, graph, i);
                Collection<Node> nodes = graph.getVertices();
                for(Node node : nodes) {
                    node.newSignal(graph.getNeighbors(node), trueDistribution.draw());
                }
                notifyAllAnalyzers(i+1, graph);

                ConvergenceInspector convergenceInspector = new ConvergenceInspector();
                boolean converged = convergenceInspector.haveConverged(graph.getVertices());
                if(converged) { return (i+1); }
            }
            return -1;
        }

	private void showAndStop(NetworkVisualizer visualizer, Graph<Node, Edge> graph, int i) {
		 visualizer.refresh();
		 for(Node n : graph.getVertices()) {
				System.out.println("\t" + n);
			}
		    System.out.println(i + " timesteps. Press enter to continue:");
		    try {
				System.in.read(new byte[2]);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	public void notifyAllAnalyzers(int timestep, Graph<Node, Edge> graph) {
		for(ExperimentAnalyzer analyzer : analyzers) {
			analyzer.notifyTimestep(graph, timestep);
		}
	}
}
