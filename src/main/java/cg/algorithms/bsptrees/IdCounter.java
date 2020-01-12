package cg.algorithms.bsptrees;

import java.util.HashSet;

public class IdCounter {
	private static IdCounter instance;
	private int counter;
	private HashSet<BSPNode> nodes;

	private IdCounter(int id) {
		this.counter = id;
		this.nodes = new HashSet<BSPNode>();
	}

	public static IdCounter getInstance() {
		if (IdCounter.instance == null) {
			IdCounter.instance = new IdCounter(-1);
		}
		return IdCounter.instance;
	}

	public int getNextInt(BSPNode node) {
		nodes.add(node);
		this.counter++;
		return this.counter;
	}
	
	public HashSet<BSPNode> getNodes(){
		return this.nodes;
	}
}
