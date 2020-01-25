package cg.algorithms.rangesearch;

import com.google.gson.JsonObject;

public class PointNode {
	public Point point = null;
	public PointNode parent;
	public PointNode leftChild;
	public PointNode rightChild;

	public DIMENSION dimension;
	public Box box;

	public PointNode(PointNode parent, Point p, DIMENSION d, Box b) {
		this.parent = parent;
		this.point = p;
		this.dimension = d;
		this.box = b;
	}

	public void printTreeNode(int level) {
		if (rightChild != null)
			rightChild.printTreeNode(level + 1);

		for (int i = 0; i < level; i++) {
			System.out.print("\t");
		}
		System.out.println(this.dimension.toString() + "=> (" + String.valueOf((int) this.point.x) + ", "
				+ String.valueOf((int) this.point.y) + ")");

		if (leftChild != null)
			leftChild.printTreeNode(level + 1);
	}

	public String toJsonString() {
		return toJsonObject().toString();
	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		json.addProperty("dimension", this.dimension.toString());
		json.add("point", this.point.toJsonObject());
		json.add("box", this.box.toJsonObject());

		if (rightChild != null)
			json.add("right", rightChild.toJsonObject());

		if (leftChild != null)
			json.add("left", leftChild.toJsonObject());

		return json;
	}
}
