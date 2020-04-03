package org.insa.graphs.model;

public class Label {
	//attributes
	public Node currentNode;
	public boolean tag;
	public float cost;
	public Arc father;
	
	//constructor
	Label(Node node, boolean Tag, float Cost, Arc Father) {
		this.currentNode = node;
		tag = Tag;
		cost = Cost;
		father = Father;
	}
	
	//functions
	float getCost() {
		return this.cost;
	}
}
