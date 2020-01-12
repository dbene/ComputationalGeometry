package cg.algorithms.bsptrees;

import com.google.gson.JsonObject;

public class BSPNode implements Comparable<BSPNode> {
	public int id = -1;

	public double minX, maxX, minY, maxY;

	public BSPNode parent;

	public Point point = null;

	public BSPNode(BSPNode parent, double minX, double maxX, double minY, double maxY) {
		this.id = IdCounter.getInstance().getNextInt(this);
		this.parent = parent;

		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public void split() {

	}

	public String toJsonString() {
		return toJsonObject().toString();
	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		if (point != null)
			json.add("point", this.point.toJsonObject());

		return json;
	}

	public JsonObject toJsonMapObject() {
		JsonObject json = new JsonObject();
		json.addProperty("_refID", this.id);


		return json;
	}

	@Override
	public int compareTo(BSPNode other) {
		return this.id - other.id;
	}
}
