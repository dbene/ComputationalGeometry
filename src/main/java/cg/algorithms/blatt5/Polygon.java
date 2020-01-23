package cg.algorithms.blatt5;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Polygon {
	boolean CCW = false;
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
			point.classification = ArtGallery.classifyVertex(point, this.CCW);
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
	
	public Polygon(List<Point> other, boolean ccw) {	
		this.CCW = ccw;
		
		for (Point point : other) {
			this.points.add(new Point(point));
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
			point.classification = ArtGallery.classifyVertex(point, this.CCW);
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
		result.add(this);

		for (Line line : splitLines) {
			System.out.println("Split@ " + line);
			LinkedList<Point> A = new LinkedList<Point>();
			LinkedList<Point> B = new LinkedList<Point>();

			Point active = line.p1;
			boolean aSide = true;
			for (int i = 0; i <= this.points.size(); i++) {				
				if (aSide) {
					if (active.equals(line.p2)) {
						aSide = false;

						i--;
						A.add(active);
					} else {
						A.add(active);
						active = active.successor;
					}
				} else {

					B.add(active);
					active = active.successor;
				}
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

			// A Side
			{

			}

			// B Side
			{

			}
		}

//		for (Line line : splitLines) {
////			System.out.println(line.p1);
//			
//			Point p1Pre = line.p1.predecessor;
//			Point p1Suc = line.p1.successor;
//
//			Point p2Pre = line.p2.predecessor;
//			Point p2Suc = line.p2.successor;
//
//			Point p1A = new Point(line.p1);
//			Point p2A = new Point(line.p2);
//			
//			Point p1B = new Point(line.p1);
//			Point p2B = new Point(line.p2);
//			
//			// Side A
//			p1Pre.successor = p1A;
//			p1A.predecessor = p1Pre;
//			
//			p1A.successor = p2A;
//			p2A.predecessor = p1A;
//			
//			p2Suc.predecessor = p2A;
//			p2A.successor = p2Suc;
//			
//			// Side B
//			p1Suc.predecessor = p1B;
//			p1B.successor = p1Suc;
//			
//			p1A.predecessor = p2A;
//			p2A.successor = p1A;
//			
//			p2Pre.successor = p2B;
//			p2B.predecessor = p2Pre;
//			
//			
//			Point p = p1B;
//			while(p != p2B) {
//				System.out.println(p);
//				p = p.successor;
//			}
//			System.out.println();
//		}

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

	public static ArrayList<Polygon> splitOne(Polygon polygon, LinkedList<Line> rootSplitLines) {
		ArrayList<Polygon> result = new ArrayList<Polygon>();
		if(polygon.isMonoton()) {
			result.add(polygon);
			return result;
		}
		
		LinkedList<Line> parentSplitLines = new LinkedList<Line>(rootSplitLines);
		Line splitLine = parentSplitLines.pop();
		while (!polygon.containsSplitLine(polygon, splitLine)) {
			if(parentSplitLines.isEmpty())
				System.out.println("wat?");
				
			splitLine = parentSplitLines.pop();
		}
		
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

		result.addAll(Polygon.splitOne(aSidePolygon, rootSplitLines));
		result.addAll(Polygon.splitOne(bSidePolygon, rootSplitLines));

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

	private boolean containsSplitLine(Polygon polygon, Line spliLine) {
		Point left = null, right = null;
		for (Point point : this.points) {
			if (left == null && spliLine.p1.equals(point))
				left = point;
			if (right == null && spliLine.p2.equals(point))
				right = point;
		}

//		boolean neigbours = left.predecessor.equals(right) || right.predecessor.equals(left);
		return left != null && right != null && !(left.predecessor.equals(right) || right.predecessor.equals(left));
	}
}
