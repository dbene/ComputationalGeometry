package cg.algorithms.artgalleryproblem;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gson.JsonObject;

import cg.algorithms.artgalleryproblem.Point.CLASSIFICATION;

public class Point {
	public double x, y;
	public DrawColor color;
	public double size = 6;
	public String text = null;

	public Polygon polygon;
	public Point successor;
	public Point predecessor;

	public Edge successorEdge;
	public Edge predecessorEdge;

	public boolean left;

	public CLASSIFICATION classification;

	public static Comparator<Point> comparator = new Comparator<Point>() {
		public int compare(Point p1, Point p2) {
			return ((p1.y > p2.y) ? -1 : 1);
		}
	};

	public Point(double X, double Y) {
		this.x = X;
		this.y = Y;
	}

	public Point(double X, double Y, DrawColor color, double size) {
		this.x = X;
		this.y = Y;
		this.color = color;
		this.size = size;
	}

	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
		this.size = p.size;
		this.color = p.color;
		this.text = p.text;
	}

	public Point(String objLine) {
		String[] parts = objLine.split(" ");
		if (parts[0].contentEquals("v") && parts.length == 3) {
			this.x = Double.parseDouble(parts[1]);
			this.y = Double.parseDouble(parts[2]);
		}
	}

	public static ArrayList<Point> parseObj(String obj) {
		ArrayList<Point> result = new ArrayList<Point>();

		String[] lines = obj.split(System.getProperty("line.separator"));
		for (String string : lines) {
			result.add(new Point(string));
		}

		return result;
	}

	public String toString() {
		return "(" + String.valueOf((int) this.x) + ", " + String.valueOf((int) this.y) + ")";
	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		json.addProperty("x", this.x);
		json.addProperty("y", this.y);
		json.addProperty("size", this.size);
		if (this.color == null)
			this.color = new DrawColor(255, 0, 0);
		json.add("color", this.color.toJsonObject());

//		json.addProperty("text", this.classification.getValue() + ", " + this.left);

		return json;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Point p = (Point) o;
		return this.x == p.x && this.y == p.y;
	}

	public static boolean isLeft(Point a, Point b, Point c) {
		return ((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)) > 0;
	}

	public static CLASSIFICATION classifyVertex(Point v, boolean ccw) {
		Point u, w;
		if (ccw) {
			u = v.successor;
			w = v.predecessor;
		} else {
			w = v.successor;
			u = v.predecessor;
		}

		if (u.y < v.y && w.y < v.y && Point.isLeft(u, v, w))
			return CLASSIFICATION.START;

		if (u.y < v.y && w.y < v.y && !Point.isLeft(u, v, w))
			return CLASSIFICATION.SPLIT;

		if (u.y > v.y && w.y > v.y && Point.isLeft(u, v, w))
			return CLASSIFICATION.END;

		if (u.y > v.y && w.y > v.y && !Point.isLeft(u, v, w))
			return CLASSIFICATION.MERGE;

		return CLASSIFICATION.REGULAR;
	}

	public enum CLASSIFICATION {
		START("START"), END("END"), REGULAR("REGULAR"), SPLIT("SPLIT"), MERGE("MERGE");

		private String value;

		CLASSIFICATION(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
