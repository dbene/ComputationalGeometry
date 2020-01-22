package cg.algorithms.blatt5;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cg.algorithms.blatt5.Point.CLASSIFICATION;

public class Polygon {
	LinkedList<Point> points = new LinkedList<Point>();
	LinkedList<Line> triangleLines = new LinkedList<Line>();
	LinkedList<Line> splitLines = new LinkedList<Line>();
	LinkedList<Edge> edges = new LinkedList<Edge>();

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

		for (Point point : this.points) {
			Edge edge = new Edge(point, point.successor);
			point.successorEdge = edge;
			this.edges.add(edge);
		}

		for (int i = 0; i < edges.size(); i++) {
			if (i == 0) {
				// Nothing
			} else if (i == edges.size() - 1) {
				edges.get(i - 1).successor = edges.get(i);
				edges.get(i).predecessor = edges.get(i - 1);

				edges.get(0).predecessor = edges.get(i);
				edges.get(i).successor = edges.get(0);
			} else {
				edges.get(i - 1).successor = edges.get(i);
				edges.get(i).predecessor = edges.get(i - 1);
			}
		}

		for (Point point : this.points) {
			point.predecessorEdge = point.predecessor.successorEdge;
		}
	}

	public ArrayList<Polygon> splitMonoton() {
		ArrayList<Polygon> result = new ArrayList<Polygon>();
//		result.add(this);

		for (Line line : splitLines) {
//			System.out.println(line.p1);
			
			Point p1Pre = line.p1.predecessor;
			Point p1Suc = line.p1.successor;

			Point p2Pre = line.p2.predecessor;
			Point p2Suc = line.p2.successor;

			Point p1A = new Point(line.p1);
			Point p2A = new Point(line.p2);
			
			Point p1B = new Point(line.p1);
			Point p2B = new Point(line.p2);
			
			// Side A
			p1Pre.successor = p1A;
			p1A.predecessor = p1Pre;
			
			p1A.successor = p2A;
			p2A.predecessor = p1A;
			
			p2Suc.predecessor = p2A;
			p2A.successor = p2Suc;
			
			// Side B
			p1Suc.predecessor = p1B;
			p1B.successor = p1Suc;
			
			p1A.predecessor = p2A;
			p2A.successor = p1A;
			
			p2Pre.successor = p2B;
			p2B.predecessor = p2Pre;
			
			
			Point p = p1B;
			while(p != p2B) {
				System.out.println(p);
				p = p.successor;
			}
			System.out.println();
		}

		return result;
	}
	
//	private boolean isOnSplitLine(Point p) {
//		for (Line line : splitLines) {
//			if(line.p1.equals(p) || line.p2.ea)
//		}
//	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		JsonArray pointArr = new JsonArray();
		JsonArray lineArr = new JsonArray();
		for (Point point : points) {
			point.size = 10;
			pointArr.add(point.toJsonObject());
			lineArr.add(new Line(point, point.successor, 0, 0, 0, 5).toJsonObject());
		}
		for (Line line : splitLines) {
			line.color = new DrawColor(0, 0, 255);
			line.width = 4;
			lineArr.add(line.toJsonObject());
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
