package cg.algorithms.quadtrees;

import java.util.HashSet;

public class IdCounter {
	private static IdCounter instance;
	private int counter;
	private HashSet<QTNode> nodes;

	private IdCounter(int id) {
		this.counter = id;
		this.nodes = new HashSet<QTNode>();
	}

	public static IdCounter getInstance() {
		if (IdCounter.instance == null) {
			IdCounter.instance = new IdCounter(-1);
		}
		return IdCounter.instance;
	}

	public int getNextInt(QTNode node) {
		nodes.add(node);
		this.counter++;
		return this.counter;
	}
	
	public HashSet<QTNode> getNodes(){
		return this.nodes;
	}
}
