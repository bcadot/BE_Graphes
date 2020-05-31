package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.Label;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    protected Label initLabel (Node node) {
		double inf = Double.POSITIVE_INFINITY;
		return new Label(node, false, inf, null);
	}

    @Override
    protected ShortestPathSolution doRun() {

	    // Retrieve the graph.
	    ShortestPathData data = getInputData();
	    Graph graph = data.getGraph();
	
	    final int nbNodes = graph.size();

	    if (nbNodes == 0)
	    	return new ShortestPathSolution(data, Status.INFEASIBLE);
	    
	    if (data.getOrigin().compareTo(data.getDestination()) == 0)
	    	return new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, data.getDestination()));
    
	    //Array of "Label" 
        Label[] labels = new Label[nbNodes];
        for (Node node : graph.getNodes()) {
        	labels[node.getId()] = initLabel(node);
        }
        labels[data.getOrigin().getId()].setCost(0);
        
        //Heap
        BinaryHeap<Label> heap = new BinaryHeap<Label>();
        heap.insert(labels[data.getOrigin().getId()]);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
        while (!heap.isEmpty()) {
        	Node x = heap.deleteMin().getNode();
        	if (x.compareTo(data.getDestination()) == 0)
        		break;
        	labels[x.getId()].setTag(true);
        	notifyNodeMarked(x);
        	for (Arc y : x.getSuccessors()) {
        		if (data.isAllowed(y)) {
	        		if (!labels[y.getDestination().getId()].getTag()) {
	        			double oldcost = labels[y.getDestination().getId()].getCost();
	        			double newcost = 0;
	        			if (data.getMode() == Mode.LENGTH)
	        				newcost = labels[x.getId()].getCost() + y.getLength();
	        			else if (data.getMode() == Mode.TIME)
	        				newcost = labels[x.getId()].getCost() + y.getMinimumTravelTime();
	        			if (oldcost >= newcost) {
	        				try {
	        					heap.remove(labels[y.getDestination().getId()]);
	        				}
	        				catch(Exception e) {
	        				}
	        				finally {
		        				labels[y.getDestination().getId()].setCost(newcost);
	        					heap.insert(labels[y.getDestination().getId()]);
	        					labels[y.getDestination().getId()].setFather(y);
	        					notifyNodeReached(y.getDestination());
	        				}
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
