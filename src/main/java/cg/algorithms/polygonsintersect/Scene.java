package cg.algorithms.polygonsintersect;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import cg.algorithms.utils.SceneInterface;

public class Scene implements SceneInterface {
	public ArrayList<Point> points;
	public ArrayList<Line> lines;

	public Scene(ArrayList<Point> points, ArrayList<Line> lines) {
		this.points = new ArrayList<Point>();
		this.points.addAll(points);

		this.lines = new ArrayList<Line>();
		this.lines.addAll(lines);
	}

	public JsonObject toJsonObject() {
		JsonObjectBuilder json = Json.createObjectBuilder();

		// Points
		JsonArrayBuilder points = Json.createArrayBuilder();
		for (Point point : this.points) {
			points.add(point.toJsonObject());
		}
		json.add("points", points);

		// Lines
		JsonArrayBuilder lines = Json.createArrayBuilder();
		for (Line line : this.lines) {
			lines.add(line.toJsonObject());
		}
		json.add("lines", lines);

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

		Scene scene = (Scene) o;

		if (!this.points.equals(scene.points)) {
			return false;
		}
		if (!this.lines.equals(scene.lines)) {
			return false;
		}

		return true;
	}
}