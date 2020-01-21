package cg.algorithms.blatt5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import cg.algorithms.blatt5.Point.CLASSIFICATION;

public class ArtGallery {
	Polygon polygon;
	boolean CCW;

	public ArtGallery(String obj, boolean CCW) {
		this.CCW = CCW;
		this.polygon = new Polygon(obj);
	}

	public void process() {
		// Classification
		for (Point vertex : polygon.points) {
			vertex.classification = classifyVertex(vertex);
//			System.out.println(vertex + " == " + vertex.classification.getValue());
		}

		// Split to y-Monotone polygons
		ArrayList<Polygon> monotonePolygons = yMonotisePolygon(polygon);

		// Triangulation of y-Monotone polygons
		for (Polygon polygon : monotonePolygons) {
			triangulatePolygon(polygon);
		}
	}

	private CLASSIFICATION classifyVertex(Point v) {
		Point u, w;
		if (this.CCW) {
			u = v.successor;
			w = v.predecessor;
		} else {
			w = v.successor;
			u = v.predecessor;
		}

		if (u.y < v.y && w.y < v.y && isLeft(u, v, w))
			return CLASSIFICATION.START;

		if (u.y < v.y && w.y < v.y && !isLeft(u, v, w))
			return CLASSIFICATION.SPLIT;

		if (u.y > v.y && w.y > v.y && isLeft(u, v, w))
			return CLASSIFICATION.END;

		if (u.y > v.y && w.y > v.y && !isLeft(u, v, w))
			return CLASSIFICATION.MERGE;

		return CLASSIFICATION.REGULAR;
	}

	public boolean isLeft(Point a, Point b, Point c) {
		return ((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)) > 0;
	}

	public ArrayList<Polygon> yMonotisePolygon(Polygon polygon) {
		ArrayList<Polygon> result = new ArrayList<Polygon>();

		result.add(polygon);

		return result;
	}

	public void triangulatePolygon(Polygon polygon) {
		LinkedList<Point> S = new LinkedList<Point>();
		ArrayList<Point> Q = new ArrayList<Point>();

		Q.addAll(polygon.points);
		Collections.sort(Q, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return (int) (o2.y - o1.y);
			}
		});

		Point highest = Q.get(0);
		Point lowest = Q.get(Q.size() - 1);

		Point active = highest;
		while (active != lowest) {
			active.left = false;
			active = active.predecessor;
		}

		active = highest;
		while (active != lowest) {
			active.left = true;
			active = active.successor;
		}

		S.push(Q.get(0));
		S.push(Q.get(1));
		for (int j = 2; j < Q.size(); j++) {
			System.out.print(Q.get(j) + "\t\t");
			printStack(S);

			if (Q.get(j).left != S.peek().left) {
//				System.out.println(Q.get(j) + " != " + S.peek());

				while (!S.isEmpty()) {
					Point p = S.pop();

					if (!S.isEmpty()) {
						Line l = new Line(Q.get(j), p);
						polygon.triangleLines.add(l);
					}
				}

				S.push(Q.get(j - 1));
				S.push(Q.get(j));
			} else {
//				System.out.println(Q.get(j) + " == " + S.peek());
				Point p1 = S.pop();
				Point p2 = S.pop();

				Line l2 = new Line(p2, Q.get(j));
				if (insidePolygon(polygon, l2)) {
					polygon.triangleLines.add(l2);
				} else {
//					S.push(p2);
				}

				Line l1 = new Line(p1, Q.get(j));
				if (insidePolygon(polygon, l1)) {
					polygon.triangleLines.add(l1);
				} else {
//					S.push(p1);
				}

				S.push(p2);
				S.push(Q.get(j));
			}
		}
	}

	private boolean insidePolygon(Polygon polygon, Line line) {
		if (neighbours(line))
 			return false;

		Point cache = polygon.points.getFirst();
		do {
			Line testLine = new Line(cache, cache.successor);
			if (intersect(testLine, line) != null)
				return false;

			cache = cache.successor;
		} while (!cache.equals(polygon.points.getFirst()));

		return true;
	}

	private boolean neighbours(Line line) {
		return line.p1.predecessor.equals(line.p2) || line.p2.predecessor.equals(line.p1);
	}

	/**
	 * Return the intersection of two line segments
	 * {@link http://www.cs.swan.ac.uk/~cssimon/line_intersection.html}
	 * 
	 * @param line1
	 * @param line2
	 * @return Point of intersection or null if there is none
	 */
	public Point intersect(Line line1, Line line2) {
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
			if (result.equals(line1.p1) || result.equals(line1.p2) || result.equals(line2.p1)
					|| result.equals(line2.p2)) {
				if (insideHelper(line2)) {
					return null;
				}
				return result;
			} else {
				return result;
			}
		}

		return null;
	}
	
	private boolean insideHelper(Line line) {
		if (line.p1.predecessor.predecessor.equals(line.p2)) {
			if (isLeft(line.p1.predecessor.predecessor, line.p1.predecessor, line.p1) && this.CCW)
				return false;
			else if (!isLeft(line.p1.predecessor.predecessor, line.p1.predecessor, line.p1) && !this.CCW)
				return false;
		} else if (line.p1.successor.successor.equals(line.p2)) {
			if (isLeft(line.p1, line.p1.successor, line.p1.successor.successor) && this.CCW)
				return false;
			else if (!isLeft(line.p1, line.p1.successor, line.p1.successor.successor) && !this.CCW)
				return false;
		}
		
		return true;
	}

	private void printStack(List<Point> stack) {
		StringBuilder sb = new StringBuilder();
		for (Point point : stack) {
			sb.append(point + ", ");
		}
		System.out.println(sb.toString());
	}

	public void saveToJSON(File file) {
		JsonObject json = new JsonObject();

		json.add("polygon", this.polygon.toJsonObject());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		try {
			FileUtils.writeStringToFile(file, prettyJson, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
