package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Label;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // Retrieve the graph.
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();
        
        //Array of "Label" 
        Label[] labels = new Label[nbNodes];
        for (Node node : graph.getNodes()) {
        	if (node.equals(data.getOrigin()))
        		labels[node.getId()] = new Label(node, false, 0, null);
        	else
        		labels[node.getId()] = new Label(node, false, Double.POSITIVE_INFINITY, null);
        }
        
        //Heap
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        heap.insert(labels[data.getOrigin().getId()]);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
        while (!heap.isEmpty()) {
        	Node x = heap.deleteMin().getNode();
        	labels[x.getId()].setTag(true);
        	for (Arc y : x.getSuccessors()) {
        		if (!labels[y.getDestination().getId()].getTag()) {
        			double oldcost = labels[y.getDestination().getId()].getCost();
        			double newcost = labels[x.getId()].getCost() + y.getLength();
        			if (oldcost > newcost) {
        				labels[y.getDestination().getId()].setCost(newcost);
        				try {
        					heap.remove(labels[y.getDestination().getId()]);
        					heap.insert(labels[y.getDestination().getId()]);
        				}
        				catch(Exception e) {
        					heap.insert(labels[y.getDestination().getId()]);
        				}
        				finally {
        					labels[y.getDestination().getId()].setFather(y);
        				}
        			}
        		}
        	}
        }
        
        ShortestPathSolution solution = null;
        if (labels[data.getDestination().getId()].getFather() == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
            
        	ArrayList<Arc> arcs = new ArrayList<>();
        	Arc arc = labels[data.getDestination().getId()].getFather();
        	while (arc != null) {
        		arcs.add(arc);
        		arc = labels[arc.getOrigin().getId()].getFather();
        	}
                
        	// Reverse the path...
        	Collections.reverse(arcs);
        	
        	// Create the final solution.
        	solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
        
        return solution;
    }

}
