package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.LabelStar;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
        
    @Override
    protected LabelStar initLabel (Node node) {
		double inf = Double.POSITIVE_INFINITY;
		ShortestPathData data = getInputData();
		int maximumSpeed = (data.getGraph().getGraphInformation() == null) ? -1 : data.getGraph().getGraphInformation().getMaximumSpeed();
		return new LabelStar(node, false, inf, null, data.getDestination(), data.getMode(), maximumSpeed);
	}
}
