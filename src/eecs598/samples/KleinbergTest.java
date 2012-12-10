package eecs598.samples;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

/**
 * Kleinber's small world graph test.
 * @author Pedro
 *
 */
public class KleinbergTest {
	private Graph<Integer, String> g;
	public KleinbergTest() {
		Factory<Graph<Integer, String>> graphFactory = new Factory<Graph<Integer,String>>() {
			@Override
			public Graph<Integer, String> create() {
				SparseGraph<Integer, String> g = new SparseGraph<>();
				return g;
			}
		};
		Factory<UndirectedGraph<Integer, String>> otherGraphFactory = new Factory<UndirectedGraph<Integer,String>>() {
			@Override
			public UndirectedGraph<Integer, String> create() {
				UndirectedSparseGraph<Integer, String> g = new UndirectedSparseGraph<>();
				return g;
			}
		};
		Factory<Integer> vertexFactory = new Factory<Integer>() {
			int i = 0;
			@Override
			public Integer create() {
				return i++;
			}
		};
		Factory<String> edgeFactory = new Factory<String>() {
			int i = 0;
			@Override
			public String create() {
				return "Edge-" + i++;
			}
			
		};
		
//		KleinbergSmallWorldGenerator<Integer, String> kleinbergGenerator = new KleinbergSmallWorldGenerator<>(
//				graphFactory, vertexFactory, edgeFactory, 10, 5);
//		
//		this.g = kleinbergGenerator.create();
		Set<Integer> seeds = new HashSet<Integer>();
		int num_seeds = 10;
		EppsteinPowerLawGenerator<Integer, String> gen = new EppsteinPowerLawGenerator<Integer, String>(
				graphFactory, vertexFactory, edgeFactory, 15, 45, 50);
		this.g = gen.create();
		System.out.println(g.getVertexCount());
		
	}
	
	public static void main(String[] args) {
        KleinbergTest sgv = new KleinbergTest(); //We create our graph in here
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Integer, String> layout = new CircleLayout<>(sgv.g);
        layout.setSize(new Dimension(700,700)); // sets the initial size of the layout space
        // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
        BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(700,700)); //Sets the viewing area size
        
        JFrame frame = new JFrame("Kleinberg Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv); 
        frame.pack();
        frame.setVisible(true);       
    }
}
