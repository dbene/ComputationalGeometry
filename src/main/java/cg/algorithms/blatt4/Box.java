package cg.algorithms.blatt4;

public class Box {
	public double minX, maxX, minY, maxY;

	public Box(double minX, double maxX, double minY, double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public Box splitLeftAt(Point p, DIMENSION d) {
		if (!contains(p))
			return null;

		if (d == DIMENSION.X) {
			return new Box(minX, p.x, minY, maxY);
		} else if (d == DIMENSION.Y) {
			return new Box(minX, maxX, minY, p.y);
		}

		return null;
	}

	public Box splitRightAt(Point p, DIMENSION d) {
		if (!contains(p))
			return null;

		if (d == DIMENSION.X) {
			return new Box(p.x, maxX, minY, maxY);
		} else if (d == DIMENSION.Y) {
			return new Box(minX, maxX, p.y, maxY);
		}

		return null;
	}

	public boolean contains(Point p) {
		return this.minX <= p.x && this.maxX >= p.x && this.minY <= p.y && this.maxY >= p.y;
	}
}
