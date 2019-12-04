package cg.algorithms.polygonsintersect;

public class Polygon {
	public Point[] points;

	public Polygon(String obj) {
		String[] objLines = obj.split(System.getProperty("line.separator"));
		this.points = new Point[objLines.length];

		for (int i = 0; i < objLines.length; i++) {
			Point point = new Point(objLines[i]);
			point.polygon = this;
			points[i] = point;

			if (i == 0) {
				// Nothing
			} else if (i == objLines.length - 1) {
				points[i - 1].successor = points[i];
				points[i].predecessor = points[i - 1];

				points[0].predecessor = points[i];
				points[i].successor = points[0];
			} else {
				points[i - 1].successor = points[i];
				points[i].predecessor = points[i - 1];
			}
		}

		for (Point p : this.points) {
			if (p.predecessor == null) {
				System.out.println("halt");
			} else if (p.successor == null) {
				System.out.println("halt");
			}
		}
	}
}
