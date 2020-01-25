package cg.algorithms.blatt5;

import com.google.gson.JsonObject;

public class Line {
	public Point p1, p2;
	public DrawColor color;
	public int width;

	public Line(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;

		this.color = new DrawColor(0, 0, 255);
		this.width = 3;
	}

	public Line(Point p1, Point p2, int r, int g, int b, int width) {
		this.p1 = p1;
		this.p2 = p2;

		this.color = new DrawColor(r, g, b);
		this.width = width;
	}

	public JsonObject toJsonObject() {
		JsonObject line = new JsonObject();
		line.add("p1", p1.toJsonObject());
		line.add("p2", p2.toJsonObject());
		line.add("color", color.toJsonObject());
		line.addProperty("width", width);

		return line;
	}
	
	/**
	 * Return the intersection of two line segments
	 * {@link http://www.cs.swan.ac.uk/~cssimon/line_intersection.html}
	 * 
	 * @param line1
	 * @param line2
	 * @return Point of intersection or null if there is none
	 */
	public static Point intersect(Line line1, Line line2, boolean ccw, boolean onPoint) {
		if (line1 == null || line2 == null)
			return null;
		
		double x1 = line1.p1.x;
		double x2 = line1.p2.x;
		double x3 = line2.p1.x;
		double x4 = line2.p2.x;

		double y1 = line1.p1.y;
		double y2 = line1.p2.y;
		double y3 = line2.p1.y;
		double y4 = line2.p2.y;

		float tA = (float) ((
					(y3 - y4) * (x1 - x3) + (x4 - x3) * (y1 - y3)
				) / (
					(x4 - x3) * (y1 - y2) - (x1 - x2) * (y4 - y3))
				);

		float tB = (float) ((
					(y1 - y2) * (x1 - x3) + (x2 - x1) * (y1 - y3)
				) / (
					(x4 - x3) * (y1 - y2) - (x1 - x2) * (y4 - y3)
				));

		if (0 <= tA && tA <= 1 && 0 <= tB && tB <= 1) {
			double pX = x1 + tA * (x2 - x1);
			double pY = y1 + tA * (y2 - y1);

			Point result = new Point(pX, pY);
			if (result.equals(line1.p1) || result.equals(line1.p2) || result.equals(line2.p1) || result.equals(line2.p2)) {
				if (!onPoint && insideHelper(line2, ccw)) {
					return null;
				}
				return result;
			} else {
				return result;
			}
		}

		return null;
	}

	private static boolean insideHelper(Line line, boolean ccw) {
		if (line.p1.predecessor.predecessor.equals(line.p2)) {
			if (Point.isLeft(line.p1.predecessor.predecessor, line.p1.predecessor, line.p1) && ccw)
				return false;
			else if (!Point.isLeft(line.p1.predecessor.predecessor, line.p1.predecessor, line.p1) && !ccw)
				return false;
		} else if (line.p1.successor.successor.equals(line.p2)) {
			if (Point.isLeft(line.p1, line.p1.successor, line.p1.successor.successor) && ccw)
				return false;
			else if (!Point.isLeft(line.p1, line.p1.successor, line.p1.successor.successor) && !ccw)
				return false;
		}

		return true;
	}
	
	public static boolean neighbours(Line line) {
		return line.p1.predecessor.equals(line.p2) || line.p2.predecessor.equals(line.p1);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Line l = (Line) o;
		return this.p1.equals(l.p1) && this.p2.equals(l.p2);
	}

	public String toString() {
		return "[" + this.p1 + ", " + this.p2 + "]";
	}
}
