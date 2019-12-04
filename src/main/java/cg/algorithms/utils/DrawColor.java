package cg.algorithms.utils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class DrawColor {
	int r, g, b;

	public DrawColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public JsonObject toJsonObject() {
		JsonObjectBuilder json = Json.createObjectBuilder();

		json.add("r", this.r);
		json.add("g", this.g);
		json.add("b", this.b);

		return json.build();
	}
}
