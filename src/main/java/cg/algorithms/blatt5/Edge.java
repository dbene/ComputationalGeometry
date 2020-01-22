package cg.algorithms.blatt5;

public class Edge {
	public final Point p1;
	public final Point p2;
	
	public Edge successor;
	public Edge predecessor;

	public Edge(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public int hashCode() {
		int A = this.p1.hashCode();
		int B = this.p2.hashCode();

		if (A > B) {
			int prime = 31;
			int result = 1;
			result = prime * result + A;
			result = prime * result + B;
			return result;
		} else {
			int prime = 31;
			int result = 1;
			result = prime * result + B;
			result = prime * result + A;
			return result;
		}
	}

	public boolean equals(Edge e) {
		if(this.p1.equals(e.p1) && this.p2.equals(e.p2))
			return true;
		if(this.p1.equals(e.p2) && this.p2.equals(e.p1))
			return true;
		
		return false;
	}
	
	public String toString() {
		return "[" + this.p1 + ", " + this.p2 + "]";
	}
}
