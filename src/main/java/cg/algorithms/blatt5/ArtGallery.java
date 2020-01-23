package cg.algorithms.blatt5;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cg.algorithms.blatt5.Point.CLASSIFICATION;

public class ArtGallery {
	Polygon polygon;
	boolean CCW;
	ArrayList<Polygon> result = new ArrayList<Polygon>();

	public ArtGallery(String obj, boolean CCW) {
		this.CCW = CCW;
		this.polygon = new Polygon(obj);
	}

	public void process() {		
		// Monotonisieren
		polygon.generateSplitLines();
		
		result.addAll(Polygon.splitRecursivly(polygon, polygon.splitLines));
		
		// Zwischenstand
		System.out.println("##########################################################");
		System.out.println("Zwischenstand");
		for (Polygon polygon : result) {
			for (Point point : polygon.points) {
				System.out.println(point);
			}
			System.out.println();
		}
		
		// Triangulieren
		for (Polygon polygon : result) {
			polygon.triangulate();
		}
	}

	public void saveToJSON(File file) {
		JsonObject json = new JsonObject();
		
		JsonArray polyArr = new JsonArray();
		for (Polygon polygon : this.result) {
			polyArr.add(polygon.toJsonObject());
		}
		json.add("polygone", polyArr);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		try {
			FileUtils.writeStringToFile(file, prettyJson, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
