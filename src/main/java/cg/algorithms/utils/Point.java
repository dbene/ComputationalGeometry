package cg.algorithms.utils;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import cg.progress.DrawColor;

public class Point {
	public double x, y;
	public DrawColor color;
	public double size = 6;
	public String text = null;

	public Point(double X, double Y) {
		this.x = X;
		this.y = Y;
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
		return "(" + String.valueOf(this.x) + "," + String.valueOf(this.y) + ")";
	}

	public JsonObject toJsonObject() {
		JsonObjectBuilder json = Json.createObjectBuilder();

		json.add("x", this.x);
		json.add("y", this.y);
		json.add("size", this.size);
		if (this.color == null)
			this.color = new DrawColor(255, 0, 0);
		json.add("color", this.color.toJsonObject());

		if (this.text != null) {
			json.add("text", this.text);
		}

		return json.build();
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
}
