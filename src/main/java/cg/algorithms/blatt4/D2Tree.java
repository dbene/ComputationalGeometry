package cg.algorithms.blatt4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cg.algorithms.quadtrees.IdCounter;
import cg.algorithms.quadtrees.QTNode;

public class D2Tree {
	ArrayList<Point> points = new ArrayList<Point>();
	PointNode rootNode;

	public D2Tree(String obj, String range) {
		String[] objLines = obj.split(System.getProperty("line.separator"));

		for (int i = 0; i < objLines.length; i++) {
			Point point = new Point(objLines[i]);
			points.add(point);
		}
	}

	public void process() {
		this.rootNode = build(null, DIMENSION.Y, this.points, new Box(0, 500, 0, 500));

		this.rootNode.printTreeNode(0);
	}

	private PointNode build(PointNode parent, DIMENSION d, ArrayList<Point> points, Box box) {
		if (points.isEmpty())
			return null;
		else if (points.size() == 1)
			return new PointNode(parent, points.get(0), d, box);

		Collections.sort(points, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				if (d == DIMENSION.X)
					return (int) (o1.x - o2.x);
				if (d == DIMENSION.Y)
					return (int) (o1.y - o2.y);
				return 0;
			}
		});

		int median = points.size() / 2;
		Point point = points.get(median);
		PointNode pn = new PointNode(parent, point, d, box);

		ArrayList<Point> leftPoints = new ArrayList<Point>();
		for (int i = 0; i < median; i++) {
			leftPoints.add(points.get(i));
		}

		ArrayList<Point> rightPoints = new ArrayList<Point>();
		for (int i = median + 1; i < points.size(); i++) {
			rightPoints.add(points.get(i));
		}

		pn.leftChild = build(pn, getNextDIMENSION(d), leftPoints, box.splitLeftAt(point, d));
		pn.rightChild = build(pn, getNextDIMENSION(d), rightPoints, box.splitRightAt(point, d));

		return pn;
	}

	private DIMENSION getNextDIMENSION(DIMENSION d) {
		if (d == DIMENSION.Y) {
			return DIMENSION.X;
		} else if (d == DIMENSION.X) {
			return DIMENSION.Y;
		}
		return null;
	}

	public void saveToJSON(File file) {
		JsonObject json = new JsonObject();

		json.add("map", rootNode.toJsonObject());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		try {
			FileUtils.writeStringToFile(file, prettyJson, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
