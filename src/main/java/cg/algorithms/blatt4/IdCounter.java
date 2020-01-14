package cg.algorithms.blatt4;

import java.util.HashSet;

public class IdCounter {
	private static IdCounter instance;
	private int counter;
	private HashSet<PointNode> nodes;

	private IdCounter(int id) {
		this.counter = id;
		this.nodes = new HashSet<PointNode>();
	}

	public static IdCounter getInstance() {
		if (IdCounter.instance == null) {
			IdCounter.instance = new IdCounter(-1);
		}
		return IdCounter.instance;
	}

	public int getNextInt(PointNode node) {
		nodes.add(node);
		this.counter++;
		return this.counter;
	}
	
	public HashSet<PointNode> getNodes(){
		return this.nodes;
	}
}
