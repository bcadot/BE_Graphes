package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.GraphStatistics;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.*;

public class DijkstraTest {
	
	// Small graph use for tests
    private static Graph graph;
    private static Graph emptygraph = new Graph("ID", "", new ArrayList<Node>(), null);

    // List of nodes
    private static Node[] nodes;

    // List of arcs in the graph, a2b is the arc from node A (0) to B (1).
    @SuppressWarnings("unused")
    private static Arc a2b, a2c, a2e, b2c, c2d_1, c2d_2, c2d_3, c2a, d2a, d2e, e2d;

    @BeforeClass
    public static void initAll() throws IOException {

        // 10 and 20 meters per seconds
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, ""),
                speed20 = new RoadInformation(RoadType.MOTORWAY, null, true, 72, "");

        // Create nodes
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        // Add arcs...
        a2b = Node.linkNodes(nodes[0], nodes[1], 10, speed10, null);
        a2c = Node.linkNodes(nodes[0], nodes[2], 15, speed10, null);
        a2e = Node.linkNodes(nodes[0], nodes[4], 15, speed20, null);
        b2c = Node.linkNodes(nodes[1], nodes[2], 10, speed10, null);
        c2d_1 = Node.linkNodes(nodes[2], nodes[3], 20, speed10, null);
        c2d_2 = Node.linkNodes(nodes[2], nodes[3], 10, speed10, null);
        c2d_3 = Node.linkNodes(nodes[2], nodes[3], 15, speed20, null);
        d2a = Node.linkNodes(nodes[3], nodes[0], 15, speed10, null);
        d2e = Node.linkNodes(nodes[3], nodes[4], 22.8f, speed20, null);
        e2d = Node.linkNodes(nodes[4], nodes[0], 10, speed10, null);

        graph = new Graph("ID", "", Arrays.asList(nodes), new GraphStatistics(null, 0, 0, -1, 0));

    }
    //ArcInspectors
	ArcInspector arc0 = ArcInspectorFactory.getAllFilters().get(0);
	ArcInspector arc2 = ArcInspectorFactory.getAllFilters().get(2);
	
	@Test
	public void testValidPath() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				ShortestPathSolution solution = new DijkstraAlgorithm(new ShortestPathData(graph, nodes[i], nodes[j], arc0)).run();
				if (solution.isFeasible())
					assertTrue(solution.getPath().isValid());
				else
					fail("Solution non faisable");
			}
		}
	}
	
	@Test
	public void testEmptyGraph() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				ShortestPathSolution solution = new DijkstraAlgorithm(new ShortestPathData(emptygraph, nodes[i], nodes[j], arc0)).run();
				assertEquals(Status.INFEASIBLE, solution.getStatus());
			}
		}
	}
	
	@Test
	public void testIslandPath() {
		for (int i = 0; i < 5; i++) {
			ShortestPathSolution solution = new DijkstraAlgorithm(new ShortestPathData(graph, nodes[i], nodes[5], arc0)).run();
			assertEquals(Status.INFEASIBLE, solution.getStatus());
		}
	}
	
	@Test
	public void testShortestPath_arc0() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (i == j)
					break;
				ShortestPathSolution solutionD = new DijkstraAlgorithm(new ShortestPathData(graph, nodes[i], nodes[j], arc0)).run();
				ShortestPathSolution solutionB = new BellmanFordAlgorithm(new ShortestPathData(graph, nodes[i], nodes[j], arc0)).run();
				assertEquals(solutionB.toString(), solutionD.toString());
			}
		}
	}
	
	@Test
	public void testShortestPath_arc2() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (i == j)
					//Selon Bellman-Ford, si départ = arrivée alors il n'y a pas de solution (c'est faux)
					break;
				ShortestPathSolution solutionD = new DijkstraAlgorithm(new ShortestPathData(graph, nodes[i], nodes[j], arc2)).run();
				ShortestPathSolution solutionB = new BellmanFordAlgorithm(new ShortestPathData(graph, nodes[i], nodes[j], arc2)).run();
				assertEquals(solutionB.toString(), solutionD.toString());
			}
		}
	}
	

}
