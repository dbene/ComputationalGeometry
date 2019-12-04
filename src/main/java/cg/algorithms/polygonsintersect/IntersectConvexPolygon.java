package cg.algorithms.polygonsintersect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import cg.algorithms.utils.DrawColor;

public class IntersectConvexPolygon {
	Polygon p1, p2;

	public IntersectConvexPolygon(Polygon p1, Polygon p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public SceneProgress process() {
		SceneProgress sp = new SceneProgress();
		addScene(sp, null, null, null, null, new HashSet<Point>());

		// Sort points of polgons by Y
		LinkedList<Point> p1List = sortPolygonPoints(new LinkedList<Point>(Arrays.asList(p1.points)));
		LinkedList<Point> p2List = sortPolygonPoints(new LinkedList<Point>(Arrays.asList(p2.points)));

		// Build height list
		LinkedList<Point> Q = new LinkedList<Point>();
		Point p1p = null;
		Point p2p = null;
		while (!p1List.isEmpty() || !p2List.isEmpty()) {
			if (p1List.isEmpty()) {
				p2p = p2List.getFirst();
				Q.add(p2p);
				p2List.remove(p2p);
				continue;
			} else if (p2List.isEmpty()) {
				p1p = p1List.getFirst();
				Q.add(p1p);
				p1List.remove(p1p);
				continue;
			}

			p1p = p1List.getFirst();
			p2p = p2List.getFirst();

			if (p1p.y > p2p.y) {
				Q.add(p1p);
				p1List.removeFirst();
			} else {
				Q.add(p2p);
				p2List.removeFirst();
			}
		}

		// Initialize S
		Point highest = Q.getFirst();
		Line leftP1 = new Line(Q.getFirst(), Q.getFirst().predecessor);
		Line rightP1 = new Line(Q.getFirst(), Q.getFirst().successor);
		Line leftP2 = null;
		Line rightP2 = null;

		HashSet<Point> result = new HashSet<Point>();
		while (!Q.isEmpty()) {
			// Add lower polygon
			if (leftP2 == null && !Q.getFirst().polygon.equals(highest.polygon)) {
				leftP2 = new Line(Q.getFirst(), Q.getFirst().predecessor);
				rightP2 = new Line(Q.getFirst(), Q.getFirst().successor);
			}

			// Finished polygon?
			if (leftP1 != null && leftP1.p2.equals(Q.getFirst()) && leftP1.p2.equals(rightP1.p2)) {
				leftP1 = null;
				rightP1 = null;
			}
			if (leftP2 != null && leftP2.p2.equals(Q.getFirst()) && leftP2.p2.equals(rightP2.p2)) {
				leftP2 = null;
				rightP2 = null;
			}

			// Advance active lines in S
			if (leftP1 != null && Q.getFirst().equals(leftP1.p2)) {
				leftP1 = new Line(leftP1.p2, leftP1.p2.predecessor);
			} else if (rightP1 != null && Q.getFirst().equals(rightP1.p2)) {
				rightP1 = new Line(rightP1.p2, rightP1.p2.successor);
			} else if (leftP2 != null && Q.getFirst().equals(leftP2.p2)) {
				leftP2 = new Line(leftP2.p2, leftP2.p2.predecessor);
			} else if (rightP2 != null && Q.getFirst().equals(rightP2.p2)) {
				rightP2 = new Line(rightP2.p2, rightP2.p2.successor);
			}

			// Check for intersection
			result.addAll(intersectAll(leftP1, rightP1, leftP2, rightP2));

			// Save scene
			addScene(sp, leftP1, rightP1, leftP2, rightP2, result);

			// Remove first element
			Q.removeFirst();
		}

		return sp;
	}

	public ArrayList<Point> intersectAll(Line line1, Line line2, Line line3, Line line4) {
		ArrayList<Point> result = new ArrayList<Point>();

		Point test1 = intersect(line1, line3);
		Point test2 = intersect(line1, line4);
		Point test3 = intersect(line2, line3);
		Point test4 = intersect(line2, line4);

		if (test1 != null)
			result.add(test1);
		if (test2 != null)
			result.add(test2);
		if (test3 != null)
			result.add(test3);
		if (test4 != null)
			result.add(test4);

		return result;
	}

	public Point intersect(Line line1, Line line2) {
		if (line1 == null || line2 == null)
			return null;

		double m1 = (line1.p2.y - line1.p1.y) / (line1.p2.x - line1.p1.x);
		double b1 = line1.p1.y - (m1 * line1.p1.x);

		double m2 = (line2.p2.y - line2.p1.y) / (line2.p2.x - line2.p1.x);
		double b2 = line2.p1.y - (m2 * line2.p1.x);

		// Parallel
		if (m1 - m2 == 0.0)
			return null;

		double x = (b2 - b1) / (m1 - m2);
		double y = m1 * x + b1;

		if (x > line1.p1.x && x > line1.p2.x || x > line2.p1.x && x > line2.p2.x || x < line1.p1.x && x < line1.p2.x
				|| x < line2.p1.x && x < line2.p2.x)
			return null;

		return new Point(x, y);
	}

	private void addScene(SceneProgress sp, Line p1L, Line p1R, Line p2L, Line p2R, HashSet<Point> interPoints) {
		ArrayList<Line> lines = new ArrayList<Line>();
		ArrayList<Point> points = new ArrayList<Point>();

		for (Point point : this.p1.points) {
			Point p = new Point(point.x, point.y, new DrawColor(255, 0, 0), 10);
			p.text = point.toString();
			points.add(p);
		}

		for (Point point : this.p2.points) {
			Point p = new Point(point.x, point.y, new DrawColor(255, 0, 0), 10);
			p.text = point.toString();
			points.add(p);
		}

		for (Point point : interPoints) {
			Point p = new Point(point.x, point.y, new DrawColor(0, 0, 255), 10);
			p.text = point.toString();
			points.add(p);
		}

		Point first = this.p1.points[0];
		Point last = null;
		for (Point point : this.p1.points) {
			if (last != null) {
				lines.add(new Line(last, point));
			}
			last = point;
		}
		lines.add(new Line(last, first));

		Point first2 = this.p2.points[0];
		Point last2 = null;
		for (Point point : this.p2.points) {
			if (last2 != null) {
				lines.add(new Line(last2, point));
			}
			last2 = point;
		}
		lines.add(new Line(last2, first2));

		if (p1L != null) {
			lines.add(new Line(p1L.p1, p1L.p2, 0, 255, 0, 3));
			lines.add(new Line(p1R.p1, p1R.p2, 0, 255, 0, 3));
		}

		if (p2L != null) {
			lines.add(new Line(p2L.p1, p2L.p2, 255, 255, 0, 3));
			lines.add(new Line(p2R.p1, p2R.p2, 255, 255, 0, 3));
		}

		Scene scene = new Scene(points, lines);
		sp.addScene(scene, true);
	}

	private LinkedList<Point> sortPolygonPoints(LinkedList<Point> polygon) {
		LinkedList<Point> result = new LinkedList<Point>();

		Point highest = polygon.getFirst();
		while (!polygon.isEmpty()) {
			if (polygon.getFirst().y > highest.y) {
				highest = polygon.getFirst();
			}
			polygon.removeFirst();
		}
		result.add(highest);

		Point left = highest.successor;
		Point right = highest.predecessor;

		while (left != null && right != null) {
			if (left.y > right.y) {
				result.add(left);
				if (!result.contains(left.successor))
					left = left.successor;
				else {
					left = null;
				}
			} else {
				result.add(right);
				if (!result.contains(right.predecessor))
					right = right.predecessor;
				else {
					right = null;
				}
			}
		}

		return result;
	}
}
