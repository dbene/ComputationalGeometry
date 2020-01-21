package cg.algorithms.blatt5;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import cg.algorithms.blatt5.Point.CLASSIFICATION;

public class ArtGallery {
	Polygon polygon;
	boolean CCW;

	public ArtGallery(String obj, boolean CCW) {
		this.CCW = CCW;
		polygon = new Polygon(obj);

		for (Point point : polygon.points) {
			System.out.println(point);
		}
	}

	public void process() {
		// Classification
		for (Point vertex : polygon.points) {
			vertex.classification = classifyVertex(vertex);
			System.out.println(vertex + " == " + vertex.classification.getValue());
		}
	}

	private CLASSIFICATION classifyVertex(Point v) {
		Point u, w;
		if(this.CCW) {
			u = v.successor;
			w = v.predecessor;
		} else {
			w = v.successor;
			u = v.predecessor;
		}


		if (u.y < v.y && w.y < v.y && isLeft(u, v, w))
			return CLASSIFICATION.START;

		if (u.y < v.y && w.y < v.y && !isLeft(u, v, w))
			return CLASSIFICATION.SPLIT;

		if (u.y > v.y && w.y > v.y && isLeft(u, v, w))
			return CLASSIFICATION.END;

		if (u.y > v.y && w.y > v.y && !isLeft(u, v, w))
			return CLASSIFICATION.MERGE;

		return CLASSIFICATION.REGULAR;
	}

	public boolean isLeft(Point a, Point b, Point c) {
		return ((b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)) > 0;
	}

	public void saveToJSON(File file) {
		JsonObject json = new JsonObject();

		json.add("polygon", this.polygon.toJsonObject());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		try {
			FileUtils.writeStringToFile(file, prettyJson, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
