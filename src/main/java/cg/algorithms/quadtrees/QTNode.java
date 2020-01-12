package cg.algorithms.quadtrees;

import com.google.gson.JsonObject;

public class QTNode implements Comparable<QTNode> {
	public int id = -1;

	public double minX, maxX, minY, maxY;

	public QTNode parent;

	public QTNode ne;
	public QTNode nw;
	public QTNode sw;
	public QTNode se;

	public QTNode north;
	public QTNode west;
	public QTNode south;
	public QTNode east;

	public Point point = null;

	public QTNode(QTNode parent, double minX, double maxX, double minY, double maxY) {
		this.id = IdCounter.getInstance().getNextInt(this);

		this.parent = parent;

		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public void split() {
		double diffX = (this.maxX - this.minX) / 2;
		double diffY = (this.maxY - this.minY) / 2;

		this.ne = new QTNode(this, this.minX + diffX, this.maxX, this.minY, this.maxY - diffY);
		this.nw = new QTNode(this, this.minX, this.maxX - diffX, this.minY, this.maxY - diffY);
		this.sw = new QTNode(this, this.minX, this.maxX - diffX, this.minY + diffY, this.maxY);
		this.se = new QTNode(this, this.minX + diffX, this.maxX, this.minY + diffY, this.maxY);
	}

	public void calcNeighbors() {
		this.north = findNorth(this);
		this.south = findSouth(this);
		this.west = findWest(this);
		this.east = findEast(this);

		if (ne != null)
			ne.calcNeighbors();
		if (nw != null)
			nw.calcNeighbors();
		if (sw != null)
			sw.calcNeighbors();
		if (se != null)
			se.calcNeighbors();
	}

	public static QTNode findNorth(QTNode node) {
		if (node.parent == null) {
			return null;
		}
		if (node == node.parent.sw) {
			return node.parent.nw;
		}
		if (node == node.parent.se) {
			return node.parent.ne;
		}

		QTNode µ = findNorth(node.parent);
		if (µ == null || µ.ne == null) {
			return µ;
		} else {
			if (node == node.parent.nw) {
				return µ.sw;
			} else {
				return µ.se;
			}
		}
	}

	public static QTNode findSouth(QTNode node) {
		if (node.parent == null) {
			return null;
		}
		if (node == node.parent.nw) {
			return node.parent.sw;
		}
		if (node == node.parent.ne) {
			return node.parent.se;
		}

		QTNode µ = findSouth(node.parent);
		if (µ == null || µ.ne == null) {
			return µ;
		} else {
			if (node == node.parent.sw) {
				return µ.nw;
			} else {
				return µ.ne;
			}
		}
	}

	public static QTNode findWest(QTNode node) {
		if (node.parent == null) {
			return null;
		}
		if (node == node.parent.ne) {
			return node.parent.nw;
		}
		if (node == node.parent.se) {
			return node.parent.sw;
		}

		QTNode µ = findWest(node.parent);
		if (µ == null || µ.ne == null) {
			return µ;
		} else {
			if (node == node.parent.nw) {
				return µ.ne;
			} else {
				return µ.se;
			}
		}
	}

	public static QTNode findEast(QTNode node) {
		if (node.parent == null) {
			return null;
		}
		if (node == node.parent.nw) {
			return node.parent.ne;
		}
		if (node == node.parent.sw) {
			return node.parent.se;
		}

		QTNode µ = findEast(node.parent);
		if (µ == null || µ.ne == null) {
			return µ;
		} else {
			if (node == node.parent.ne) {
				return µ.nw;
			} else {
				return µ.sw;
			}
		}
	}

	public boolean checkPoint(Point point, DIRECTION direction) {
		if (direction == DIRECTION.NORTHEAST)
			return point.x > this.minX && point.x <= this.maxX && point.y > this.minY && point.y <= this.maxY;
		if (direction == DIRECTION.NORTHWEST)
			return point.x >= this.minX && point.x <= this.maxX && point.y >= this.minY && point.y < this.maxY;
		if (direction == DIRECTION.SOUTHWEST)
			return point.x >= this.minX && point.x <= this.maxX && point.y >= this.minY && point.y <= this.maxY;
		if (direction == DIRECTION.SOUTHEAST)
			return point.x > this.minX && point.x <= this.maxX && point.y >= this.minY && point.y <= this.maxY;

		return false;
	}

	public String toJsonString() {
		return toJsonObject().toString();
	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		json.addProperty("_id", this.id);

		json.addProperty("minX", this.minX);
		json.addProperty("maxX", this.maxX);
		json.addProperty("minY", this.minY);
		json.addProperty("maxY", this.maxY);

		if (this.north != null)
			json.addProperty("NORTH", this.north.id);
		if (this.west != null)
			json.addProperty("WEST", this.west.id);
		if (this.south != null)
			json.addProperty("SOUTH", this.south.id);
		if (this.east != null)
			json.addProperty("EAST", this.east.id);

		if (point != null)
			json.add("point", this.point.toJsonObject());

		return json;
	}

	public JsonObject toJsonMapObject() {
		JsonObject json = new JsonObject();
		json.addProperty("_refID", this.id);

		if (ne != null)
			json.add("NE", this.ne.toJsonMapObject());
		if (nw != null)
			json.add("NW", this.nw.toJsonMapObject());
		if (sw != null)
			json.add("SW", this.sw.toJsonMapObject());
		if (se != null)
			json.add("SE", this.se.toJsonMapObject());

		return json;
	}

	@Override
	public int compareTo(QTNode other) {
		return this.id - other.id;
	}
}
