package cg.algorithms.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import cg.progress.DrawColor;

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
		JsonObjectBuilder line = Json.createObjectBuilder();
		line.add("p1", p1.toJsonObject());
		line.add("p2", p2.toJsonObject());
		line.add("color", color.toJsonObject());
		line.add("width", width);

		return line.build();
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
}
