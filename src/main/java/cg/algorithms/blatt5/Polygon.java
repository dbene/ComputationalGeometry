package cg.algorithms.blatt5;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Polygon {
	LinkedList<Point> points = new LinkedList<Point>();
	LinkedList<Line> triangleLines = new LinkedList<Line>();

	public Polygon(String obj) {
		String[] objLines = obj.split(System.getProperty("line.separator"));

		ArrayList<Point> cache = new ArrayList<Point>();
		for (String line : objLines) {
			if (line.startsWith("v")) {
				Point point = new Point(line);
				cache.add(point);
			} else if (line.startsWith("f")) {
				String[] parts = line.split(" ");
				if (parts[0].contentEquals("f")) {
					for (int i = 1; i < parts.length; i++) {
						int index = Integer.parseInt(parts[i]) - 1;
						points.add(cache.get(index));
					}
				}
			}
		}

		for (int i = 0; i < points.size(); i++) {
			if (i == 0) {
				// Nothing
			} else if (i == points.size() - 1) {
				points.get(i - 1).successor = points.get(i);
				points.get(i).predecessor = points.get(i - 1);

				points.get(0).predecessor = points.get(i);
				points.get(i).successor = points.get(0);
			} else {
				points.get(i - 1).successor = points.get(i);
				points.get(i).predecessor = points.get(i - 1);
			}
		}
	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		JsonArray pointArr = new JsonArray();
		JsonArray lineArr = new JsonArray();
		for (Point point : points) {
			pointArr.add(point.toJsonObject());
			lineArr.add(new Line(point, point.successor).toJsonObject());
		}
		json.add("points", pointArr);
		json.add("lines", lineArr);		

		JsonArray triLineArr = new JsonArray();
		for (Line line : this.triangleLines) {
			triLineArr.add(line.toJsonObject());
		}
		json.add("triLines", triLineArr);	

		return json;
	}
}
