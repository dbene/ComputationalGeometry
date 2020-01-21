package cg.algorithms.blatt5;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class ArtGallery {
	Polygon polygon;

	public ArtGallery(String obj) {
		polygon = new Polygon(obj);

		for (Point point : polygon.points) {
			System.out.println(point);
		}
	}

	public void process() {

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
