package cg.algorithms.rangesearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class D2Tree {
	ArrayList<Point> points = new ArrayList<Point>();
	Box searchRange = null;
	PointNode rootNode;

	public D2Tree(String obj, String range) {
		String[] objLines = obj.split(System.getProperty("line.separator"));

		for (int i = 0; i < objLines.length; i++) {
			Point point = new Point(objLines[i]);
			points.add(point);
		}

//		Random r = new Random();
//		for (int i = 0; i < 32; i++) {
//			points.add(new Point(r.nextInt((490 - 10) + 1) + 10, r.nextInt((490 - 10) + 1) + 10));
//		}

		String[] objRange = range.split(System.getProperty("line.separator"));
		double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE, minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

		for (int i = 0; i < objRange.length; i++) {
			String[] parts = objRange[i].split(" ");
			if (parts[0].contentEquals("v") && parts.length == 3) {
				if (minX > Double.parseDouble(parts[1]))
					minX = Double.parseDouble(parts[1]);
				if (maxX < Double.parseDouble(parts[1]))
					maxX = Double.parseDouble(parts[1]);

				if (minY > Double.parseDouble(parts[2]))
					minY = Double.parseDouble(parts[2]);
				if (maxY < Double.parseDouble(parts[2]))
					maxY = Double.parseDouble(parts[2]);
			}
		}

		this.searchRange = new Box(minX, maxX, minY, maxY);
	}

	public void process() {
		this.rootNode = build(null, DIMENSION.Y, this.points, new Box(0, 500, 0, 500));
		
		rangeSearch(this.rootNode, this.rootNode.dimension, this.searchRange);

		this.rootNode.printTreeNode(0);
	}

	public void rangeSearch(PointNode node, DIMENSION d, Box range) {
		if (node != null) {
			double l, r, coord;

			if (d == DIMENSION.Y) {
				r = range.maxY;
				l = range.minY;
				coord = node.point.y;
			} else {
				r = range.maxX;
				l = range.minX;
				coord = node.point.x;
			}

			node.point.color = new DrawColor(255, 0, 0);

			if (l < coord) {
				node.point.color = new DrawColor(255, 255, 0);
				rangeSearch(node.leftChild, getNextDIMENSION(d), range);
			}

			if (coord < r) {
				node.point.color = new DrawColor(255, 255, 0);
				rangeSearch(node.rightChild, getNextDIMENSION(d), range);
			}

			if (range.contains(node.point)) {
				node.point.color = new DrawColor(0, 255, 0);
			}
		}
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

		json.add("map", this.rootNode.toJsonObject());
		json.add("range", this.searchRange.toJsonObject());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		try {
			FileUtils.writeStringToFile(file, prettyJson, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
