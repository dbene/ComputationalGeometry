package cg.algorithms.quadtrees;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class QuadTree {
	ArrayList<Point> points = new ArrayList<Point>();
	QTNode rootNode;

	public QuadTree(String obj) {
		String[] objLines = obj.split(System.getProperty("line.separator"));

		for (int i = 0; i < objLines.length; i++) {
			Point point = new Point(objLines[i]);
			points.add(point);
		}

		rootNode = new QTNode(null, 0, 500, 0, 500);
	}

	public void process() {
		test(rootNode, this.points);

		rootNode.calcNeighbors();
	}

	public void saveToJSON(File file) {
		ArrayList<QTNode> cache = new ArrayList<QTNode>();
		cache.addAll(IdCounter.getInstance().getNodes());		
		Collections.sort(cache);
		
		JsonObject json = new JsonObject();
		JsonArray arrNodes = new JsonArray();
		
		for (QTNode node : cache) {
			arrNodes.add(node.toJsonObject());
		}

		json.add("nodes", arrNodes);
		json.add("map", rootNode.toJsonMapObject());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		try {
			FileUtils.writeStringToFile(file, prettyJson, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void test(QTNode node, ArrayList<Point> points) {
		if (points.size() == 0) {

		} else if (points.size() == 1) {
			node.point = points.get(0);
		} else if (points.size() > 1) {
			node.split();

			ArrayList<Point> nePoints = new ArrayList<Point>();
			ArrayList<Point> nwPoints = new ArrayList<Point>();
			ArrayList<Point> swPoints = new ArrayList<Point>();
			ArrayList<Point> sePoints = new ArrayList<Point>();

			for (Point point : points) {
				if (node.ne.checkPoint(point, DIRECTION.NORTHEAST))
					nePoints.add(point);
				if (node.nw.checkPoint(point, DIRECTION.NORTHWEST))
					nwPoints.add(point);
				if (node.sw.checkPoint(point, DIRECTION.SOUTHWEST))
					swPoints.add(point);
				if (node.se.checkPoint(point, DIRECTION.SOUTHEAST))
					sePoints.add(point);
			}

			test(node.ne, nePoints);
			test(node.nw, nwPoints);
			test(node.sw, swPoints);
			test(node.se, sePoints);
		}
	}
}
