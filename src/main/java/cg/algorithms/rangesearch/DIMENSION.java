package cg.algorithms.rangesearch;

public enum DIMENSION {
	Y("y"), X("x");

	private String value;

	DIMENSION(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
	}
}
