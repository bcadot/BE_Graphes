package org.insa.graphs.algorithm;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{
	//attributes
	protected Node currentNode;
	//a tagged Node means it has been visited
	protected boolean tag;
	protected double cost;
	protected Arc father;
	
	//constructor
	public Label(Node node, boolean Tag, double Cost, Arc Father) {
		this.currentNode = node;
		tag = Tag;
		cost = Cost;
		father = Father;
	}
	
	//functions
	public Node getNode() {
		return this.currentNode;
	}
	public boolean getTag() {
		return this.tag;
	}
	public Arc getFather() {
		return this.father;
	}
	public double getCost() {
		return this.cost;
	}
	public void setCost(double Cost) {
		this.cost = Cost;
	}
	public void setTag(boolean Tag) {
		this.tag = Tag;
	}
	public void setFather(Arc Father) {
		this.father = Father;
	}
	
	public double getTotalCost() {
		return this.cost;
	}
	
	@Override
    public int compareTo(Label other) {
		int cmp = Double.compare(this.getTotalCost(), other.getTotalCost());
        if (cmp == 0) {
            cmp = Double.compare(this.cost , other.cost);
        }
        return cmp;
    }
}
