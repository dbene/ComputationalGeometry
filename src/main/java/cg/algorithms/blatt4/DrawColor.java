package cg.algorithms.blatt4;

import com.google.gson.JsonObject;

public class DrawColor {
	int r, g, b;

	public DrawColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();

		json.addProperty("r", this.r);
		json.addProperty("g", this.g);
		json.addProperty("b", this.b);

		return json;
	}
}
