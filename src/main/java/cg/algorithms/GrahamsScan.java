package cg.algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

import cg.algorithms.utils.Line;
import cg.algorithms.utils.Point;
import cg.progress.DrawColor;
import cg.progress.Scene;
import cg.progress.SceneProgress;

public class GrahamsScan {
	private ArrayList<Point> points;
	private Comparator<Point> comparator = new Comparator<Point>() {
		public int compare(Point p1, Point p2) {
			if (p1.x == p2.x) {
				// Sort by Y
				return ((p1.y < p2.y) ? -1 : 1);
			}
			return ((p1.x < p2.x) ? -1 : 1);
		}
	};

	public GrahamsScan(ArrayList<Point> points) {
		this.points = points;
	}

	public SceneProgress process() {
		SceneProgress result = new SceneProgress();

		// GrahamsScan
		points.sort(comparator);
		addScene(result, points, new LinkedList<Point>(), new LinkedList<Point>(), new LinkedList<Point>());

		// Upper
		LinkedList<Point> lUpper = new LinkedList<Point>();
		lUpper.add(points.get(0));
		lUpper.add(points.get(1));
		addScene(result, points, lUpper, new LinkedList<Point>(), new LinkedList<Point>());

		for (int i = 2; i < points.size(); i++) {
			Point p = points.get(i);
			lUpper.add(p);

			addScene(result, points, lUpper, new LinkedList<Point>(), lastThreePoints(lUpper));
			while (lUpper.size() > 2 && !lastThreeTurnRight(lUpper)) {
				lUpper.remove(lUpper.size() - 2);
				addScene(result, points, lUpper, new LinkedList<Point>(), lastThreePoints(lUpper));
			}
			addScene(result, points, lUpper, new LinkedList<Point>(), new LinkedList<Point>());
		}

		// Lower
		LinkedList<Point> lLower = new LinkedList<Point>();
		lLower.add(points.get(points.size() - 1));
		lLower.add(points.get(points.size() - 2));
		addScene(result, points, lUpper, lLower, new LinkedList<Point>());

		for (int i = points.size() - 3; i >= 0; i--) {
			Point p = points.get(i);
			lLower.add(p);

			addScene(result, points, lUpper, lLower, lastThreePoints(lLower));
			while (lLower.size() > 2 && !lastThreeTurnRight(lLower)) {
				lLower.remove(lLower.size() - 2);
				addScene(result, points, lUpper, lLower, lastThreePoints(lLower));
			}
			addScene(result, points, lUpper, lLower, new LinkedList<Point>());
		}

		addScene(result, points, lUpper, lLower, new LinkedList<Point>());

		LinkedList<Point> convexHull = new LinkedList<Point>();
		convexHull.addAll(lUpper);
		lLower.removeFirst();
		lLower.removeLast();
		convexHull.addAll(lLower);

		addFinalScene(result, points, convexHull);

		for (Point point : convexHull) {
			System.out.println(point);
		}

		return result;
	}

	private static boolean lastThreeTurnRight(LinkedList<Point> list) {
		Point a = list.get(list.size() - 3);
		Point b = list.get(list.size() - 2);
		Point c = list.get(list.size() - 1);

		double result = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);

		if (result > 0)
			return true;
		if (result == 0)
			return true;
		return false;
	}

	private static LinkedList<Point> lastThreePoints(LinkedList<Point> list) {
		if (list.size() < 3) {
			return new LinkedList<Point>();
		}

		LinkedList<Point> result = new LinkedList<Point>();

		result.add(list.get(list.size() - 3));
		result.add(list.get(list.size() - 1));

		return result;
	}

	private void addScene(SceneProgress sp, ArrayList<Point> points, LinkedList<Point> lUpper, LinkedList<Point> lLower,
			LinkedList<Point> lastThree) {

		ArrayList<Line> lines = new ArrayList<Line>();

		Point lastPoint = null;
		for (Point point : lUpper) {
			if (lastPoint == null) {
				lastPoint = point;
			} else {
				Line line = new Line(lastPoint, point);
				lines.add(line);
			}
			lastPoint = point;
		}

		lastPoint = null;
		for (Point point : lLower) {
			if (lastPoint == null) {
				lastPoint = point;
			} else {
				Line line = new Line(lastPoint, point);
				lines.add(line);
			}
			lastPoint = point;
		}

		if (lastThree.size() == 2) {
			Line testLine = new Line(lastThree.getFirst(), lastThree.getLast(), 0, 255, 0, 2);
			lines.add(testLine);
		}

		Scene scene = new Scene(points, lines);
		sp.addScene(scene);
	}

	private void addFinalScene(SceneProgress sp, ArrayList<Point> allPoints, LinkedList<Point> convexHull) {
		ArrayList<Point> cache = new ArrayList<Point>();
		cache.addAll(allPoints);
		cache.removeAll(convexHull);

		ArrayList<Point> points = new ArrayList<Point>();
		for (Point point : cache) {
			Point p = new Point(point);
			p.color = new DrawColor(0, 0, 0);
			points.add(p);
		}

		ArrayList<Line> hullLines = new ArrayList<Line>();

		Point firstPoint = null;
		Point lastPoint = null;
		for (int i = 0; i < convexHull.size(); i++) {
			Point point = new Point(convexHull.get(i));
			point.color = new DrawColor(255, 0, 0);
			point.size = 16;
			point.text = "P" + i + " - " + point.toString();
			points.add(point);

			if (convexHull.get(i).equals(convexHull.getFirst())) {
				firstPoint = point;
			} else {
				Line line = new Line(lastPoint, point);
				hullLines.add(line);
			}

			if (convexHull.get(i).equals(convexHull.getLast())) {
				Line line = new Line(point, firstPoint);
				hullLines.add(line);
			}

			lastPoint = point;
		}

		Scene scene = new Scene(points, hullLines);
		sp.addScene(scene);
	}
}
