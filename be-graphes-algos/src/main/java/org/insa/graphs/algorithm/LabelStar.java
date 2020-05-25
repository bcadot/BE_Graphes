package org.insa.graphs.algorithm;

import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label implements Comparable<Label>{
	
	//attributes
	private double estimatedCost;
	private Node destination;
	private Mode mode;
	private int maxSpeed;

	public LabelStar(Node node, boolean Tag, double Cost, Arc Father, Node destination, Mode mode, int maxSpeed) {
		super(node, Tag, Cost, Father);
		this.destination = destination;
		this.mode = mode;
		this.maxSpeed = maxSpeed;
		this.estimatedCost = getEstimatedCost();
	}
	
	private double getEstimatedCost() {
		if (mode == Mode.LENGTH) {
			return this.getEstimatedDistance();
		} else {
			return this.getEstimatedTime();
		}
	}
	
	private double getEstimatedDistance() {
		if (this.getNode().getPoint() != null && this.destination.getPoint() != null)
			return Point.distance(this.getNode().getPoint(), this.destination.getPoint());
		else 
			return 0;
	}
	
	private double getEstimatedTime() {
		if (this.maxSpeed != -1)
			return this.getEstimatedDistance() / this.maxSpeed;
		else 
			return this.getEstimatedDistance();
	}
	
	@Override
	public double getTotalCost() {
		return (this.cost + this.estimatedCost);
	}
}
