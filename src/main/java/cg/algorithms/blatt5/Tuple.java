package cg.algorithms.blatt5;

public class Tuple<X, Y> {
	public final X x;
	public final Y y;

	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	public int hashCode() {
		int A = this.x.hashCode();
		int B = this.y.hashCode();

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

	public boolean equals(Tuple t) {
		if(this.x.equals(t.x) && this.y.equals(t.y))
			return true;
		if(this.x.equals(t.y) && this.y.equals(t.x))
			return true;
		
		return false;
	}
}