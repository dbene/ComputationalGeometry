package cg.algorithms.bsptrees;

import java.util.ArrayList;

public class BSPTree {
	ArrayList<Point> points = new ArrayList<Point>();
	BSPNode rootNode;

	public BSPTree(String obj) {
		String[] objLines = obj.split(System.getProperty("line.separator"));

		for (int i = 0; i < objLines.length; i++) {
			Point point = new Point(objLines[i]);
			points.add(point);
		}

		rootNode = new BSPNode(null, 0, 500, 0, 500);
	}

	public void process() {

	}
}
