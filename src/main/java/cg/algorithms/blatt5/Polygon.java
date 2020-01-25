package cg.algorithms.blatt5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cg.algorithms.blatt5.Point.CLASSIFICATION;

public class Polygon {
	boolean CCW = false;
	LinkedList<Point> points = new LinkedList<Point>();
	LinkedList<Line> triangleLines = new LinkedList<Line>();
	LinkedList<Line> splitLines = new LinkedList<Line>();
	LinkedList<Edge> edges = new LinkedList<Edge>();

	public Polygon(String obj, boolean ccw) {
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

		buildPolygon();
	}

	public Polygon(List<Point> other, boolean ccw) {
		this.CCW = ccw;

		for (Point point : other) {
			this.points.add(new Point(point));
		}

		buildPolygon();
	}

	private void buildPolygon() {
		ArrayList<Point> summits = new ArrayList<Point>();
		
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
			point.classification = Point.classifyVertex(point, this.CCW);
			if (point.classification == CLASSIFICATION.START || point.classification == CLASSIFICATION.SPLIT) {
				summits.add(point);
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
		
		for (Point point : summits) {
			Point active = point;
			while (active.classification != CLASSIFICATION.END && active.classification != CLASSIFICATION.MERGE) {
				active.left = false;
				active = active.predecessor;
			}

			active = point;
			while (active.classification != CLASSIFICATION.END && active.classification != CLASSIFICATION.MERGE) {
				active.left = true;
				active = active.successor;
			}
		}
	}

	public void generateSplitLines() {
		LinkedList<Point> Q = new LinkedList<Point>();

		HashMap<Edge, Point> tree = new HashMap<Edge, Point>();

		Q.addAll(this.points);
		Collections.sort(Q, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return (int) (o2.y - o1.y);
			}
		});

		while (!Q.isEmpty()) {
			Point p = Q.pop();
			Edge ej = null;
			Line l = null;
			
			System.out.println(p);

			switch (p.classification) {
			case START:
				Edge e = p.successorEdge;
				tree.put(e, p);

				break;
			case END:
				if (tree.get(p.predecessorEdge).classification == CLASSIFICATION.MERGE) {
					l = new Line(p, tree.get(p.predecessorEdge));
					this.splitLines.add(l);
				}
				tree.remove(p.predecessorEdge);

				break;
			case SPLIT:
				ej = eJ(this, p);

				l = new Line(p, tree.get(ej));
				this.splitLines.add(l);

				tree.put(ej, p);
				tree.put(p.successorEdge, p);

				break;
			case MERGE:
				if (tree.get(p.predecessorEdge).classification == CLASSIFICATION.MERGE) {
					l = new Line(p, tree.get(p.predecessorEdge));
					this.splitLines.add(l);
				}
				tree.remove(p.predecessorEdge);
				ej = eJ(this, p);

				if (tree.get(ej).classification == CLASSIFICATION.MERGE) {
					l = new Line(p, tree.get(ej));
					this.splitLines.add(l);
				}

				tree.put(ej, p);

				break;
			case REGULAR:
				if (p.left) { // interiorIsRight(p) && p.x != 500.0
					if (tree.get(p.predecessorEdge).classification == CLASSIFICATION.MERGE) {
						l = new Line(p, tree.get(p.predecessorEdge));
						this.splitLines.add(l);
					}
					tree.remove(p.predecessorEdge);
					tree.put(p.successorEdge, p);
				} else {
					ej = eJ(this, p);
					if (tree.get(ej).classification == CLASSIFICATION.MERGE) {
						l = new Line(p, tree.get(ej));
						this.splitLines.add(l);
					}
					tree.put(ej, p);
				}

				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + p.classification);
			}
		}
	}

	public static ArrayList<Polygon> splitRecursivly(Polygon polygon, LinkedList<Line> rootSplitLines) {
		ArrayList<Polygon> result = new ArrayList<Polygon>();
		if (polygon.isMonoton()) {
			result.add(polygon);
			return result;
		}

		LinkedList<Line> parentSplitLines = new LinkedList<Line>(rootSplitLines);
		Line splitLine = parentSplitLines.pop();
		while (!polygon.containsSplitLine(polygon, splitLine)) {
			if (parentSplitLines.isEmpty())
				System.out.println("wat?");

			splitLine = parentSplitLines.pop();
		}

		System.out.println("##########################################################");
		System.out.println("Split@ " + splitLine);
		LinkedList<Point> A = new LinkedList<Point>();
		LinkedList<Point> B = new LinkedList<Point>();

		Point active = polygon.points.get(polygon.points.indexOf(splitLine.p1));
		boolean aSide = true;
		for (int i = 0; i < polygon.points.size() + 1; i++) {
			if (aSide) {
				A.add(new Point(active));

				if (active.equals(splitLine.p2)) {
					aSide = false;
					B.add(active);
				}
			} else {
				B.add(new Point(active));
			}
			active = active.successor;
		}

		for (Point p : A) {
			System.out.println("A: " + p);
		}
		System.out.println();
		for (Point p : B) {
			System.out.println("B: " + p);
		}
		System.out.println();
		System.out.println();

		Polygon aSidePolygon = new Polygon(A, polygon.CCW);
		Polygon bSidePolygon = new Polygon(B, polygon.CCW);

		result.addAll(Polygon.splitRecursivly(aSidePolygon, rootSplitLines));
		result.addAll(Polygon.splitRecursivly(bSidePolygon, rootSplitLines));

		return result;
	}

	public void triangulate() {
		System.out.println("##########################################################");
		System.out.println("Triangulation");
		
		LinkedList<Point> S = new LinkedList<Point>();
		ArrayList<Point> Q = new ArrayList<Point>();

		Q.addAll(this.points);
		Collections.sort(Q, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return (int) (o2.y - o1.y);
			}
		});

		S.push(Q.get(0));
		S.push(Q.get(1));
		for (int j = 2; j < Q.size(); j++) {
			System.out.print(Q.get(j) + "\t\t");
			printStack(S);

			if (Q.get(j).left != S.peek().left) {
				while (!S.isEmpty()) {
					Point p = S.pop();

					if (!S.isEmpty()) {
						Line l = new Line(Q.get(j), p);
						if (this.insidePolygon(l))
							this.triangleLines.add(l);
					}
				}

				S.push(Q.get(j - 1));
				S.push(Q.get(j));
			} else {
				Line testLine = new Line(S.get(1), Q.get(j));
				while (this.insidePolygon(testLine)) {
					S.pop();
					this.triangleLines.add(testLine);

					if (S.size() > 1)
						testLine = new Line(S.get(1), Q.get(j));
					else
						break;
				}

				S.push(Q.get(j));
			}
		}
	}

	private void printStack(List<Point> stack) {
		StringBuilder sb = new StringBuilder();
		for (Point point : stack) {
			sb.append(point + ", ");
		}
		System.out.println(sb.toString());
	}

	private static Edge eJ(Polygon polygon, Point v) {
		Line testLine = new Line(v, new Point(0, v.y));

		ArrayList<Tuple<Point, Edge>> edges = new ArrayList<Tuple<Point, Edge>>();
		for (Edge edge : polygon.edges) {
			Point intersection = Line.intersect(testLine, new Line(edge.p1, edge.p2), polygon.CCW, false);
			if (intersection != null) {
				edges.add(new Tuple<Point, Edge>(intersection, edge));
			}
		}

		double minDistance = Double.MAX_VALUE;
		Edge result = null;
		for (Tuple<Point, Edge> tuple : edges) {
			double test = v.x - tuple.x.x;

			if (minDistance > test) {
				minDistance = test;
				result = tuple.y;
			}
		}
		
		System.out.println(v + " => " + result);

		return result;
	}

	public boolean isMonoton() {
		int start = 0, end = 0, split = 0, merge = 0, regular = 0;

		for (Point point : this.points) {
			switch (point.classification) {
			case START:
				start++;
				break;
			case END:
				end++;
				break;
			case SPLIT:
				split++;
				break;
			case MERGE:
				merge++;

				break;
			case REGULAR:
				regular++;

				break;
			}
		}

		return start == 1 && end == 1 && split == 0 && merge == 0 && (regular + 2) == this.points.size();
	}

	public boolean insidePolygon(Line line) {
		if (Line.neighbours(line))
			return false;

		Point cache = this.points.getFirst();
		do {
			Line testLine = new Line(cache, cache.successor);
			if (Line.intersect(testLine, line, this.CCW, false) != null)
				return false;

			cache = cache.successor;
		} while (!cache.equals(this.points.getFirst()));

		return true;
	}

	private boolean containsSplitLine(Polygon polygon, Line spliLine) {
		Point left = null, right = null;
		for (Point point : this.points) {
			if (left == null && spliLine.p1.equals(point))
				left = point;
			if (right == null && spliLine.p2.equals(point))
				right = point;
		}

		return left != null && right != null && !(left.predecessor.equals(right) || right.predecessor.equals(left));
	}

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
