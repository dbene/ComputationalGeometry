package cg.algorithms.quadtrees;

import java.util.ArrayList;

import cg.algorithms.utils.DrawColor;

public class QuadTree {
	ArrayList<Point> points = new ArrayList<Point>();

	public QuadTree(String obj) {
		String[] objLines = obj.split(System.getProperty("line.separator"));

		for (int i = 0; i < objLines.length; i++) {
			Point point = new Point(objLines[i]);
			points.add(point);
		}
	}

	public SceneProgress process() {
		SceneProgress sp = new SceneProgress();
		addScene(sp, points, null);
		
		

		return sp;
	}

	private void addScene(SceneProgress sp, ArrayList<Point> points, ArrayList<Line> lines) {
		ArrayList<Point> drawPoints = new ArrayList<Point>();
		ArrayList<Line> drawLines = new ArrayList<Line>();

		if (points != null) {
			drawPoints.addAll(points);
		}

		if (lines != null) {
			drawLines.addAll(lines);
		}

		Point edge1 = new Point(0, 0, new DrawColor(0, 0, 0), 0);
		Point edge2 = new Point(0, 500, new DrawColor(0, 0, 0), 0);
		Point edge3 = new Point(500, 500, new DrawColor(0, 0, 0), 0);
		Point edge4 = new Point(500, 0, new DrawColor(0, 0, 0), 0);

		drawPoints.add(edge1);
		drawPoints.add(edge2);
		drawPoints.add(edge3);
		drawPoints.add(edge4);

		drawLines.add(new Line(edge1, edge2, 0, 0, 0, 3));
		drawLines.add(new Line(edge2, edge3, 0, 0, 0, 3));
		drawLines.add(new Line(edge3, edge4, 0, 0, 0, 3));
		drawLines.add(new Line(edge4, edge1, 0, 0, 0, 3));

		Scene scene = new Scene(drawPoints, drawLines);
		sp.addScene(scene);
	}
}
