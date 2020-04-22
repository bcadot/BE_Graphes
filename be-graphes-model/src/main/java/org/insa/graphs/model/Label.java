package org.insa.graphs.model;

public class Label implements Comparable<Label>{
	//attributes
	private Node currentNode;
	//a tagged Node means it has been visited
	private boolean tag;
	private double cost;
	private Arc father;
	
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
	public double getCost() {
		return this.cost;
	}
	public boolean getTag() {
		return this.tag;
	}
	public Arc getFather() {
		return this.father;
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
	
	@Override
    public int compareTo(Label other) {
        return Double.compare(getCost(), other.getCost());
    }
}
