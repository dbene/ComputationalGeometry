import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import cg.algorithms.blatt4.D2Tree;
import cg.algorithms.blatt5.ArtGallery;
import cg.algorithms.grahamsscan.GrahamsScan;
import cg.algorithms.grahamsscan.Point;
import cg.algorithms.polygonsintersect.IntersectConvexPolygon;
import cg.algorithms.polygonsintersect.Polygon;
import cg.algorithms.quadtrees.QuadTree;
import cg.algorithms.utils.SceneProgress;

public class Main {

	public static void main(String[] args) {
		Path drawerPath = Paths.get("C:\\Users\\dasbene\\Desktop\\drawer_dev\\cg_drawer");

//		blatt1(drawerPath);
//		blatt2(drawerPath);
//		blatt3(drawerPath);
//		blatt4(drawerPath);
		blatt5(drawerPath);
	}

	public static void blatt1(Path drawerPath) {
		// Read Object
		File objFile = new File(ClassLoader.getSystemClassLoader().getResource("test.obj").getFile());

		try {
			String[] objLines = FileUtils.readFileToString(objFile, "UTF-8").split(System.getProperty("line.separator"));

			ArrayList<Point> points = new ArrayList<Point>();
			for (int i = 0; i < objLines.length; i++) {
				Point point = new Point(objLines[i]);
				points.add(point);
			}

			GrahamsScan gs = new GrahamsScan(points);
			cg.algorithms.grahamsscan.SceneProgress sp = gs.process();

			try {
				sp.saveContextToJson(Paths.get(drawerPath.toString(), "algorithms", "blatt2", "data", "output.json").toFile());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void blatt2(Path drawerPath) {
		// Read Object
		File poly1File = new File(ClassLoader.getSystemClassLoader().getResource("poly1.obj").getFile());
		File poly2File = new File(ClassLoader.getSystemClassLoader().getResource("poly2_v2.obj").getFile());

		Polygon polygon1 = null, polygon2 = null;
		try {
			String poly1String = FileUtils.readFileToString(poly1File, "UTF-8");
			String poly2String = FileUtils.readFileToString(poly2File, "UTF-8");

			polygon1 = new Polygon(poly1String);
			polygon2 = new Polygon(poly2String);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		IntersectConvexPolygon icp = new IntersectConvexPolygon(polygon1, polygon2);
		SceneProgress sp = icp.process();

		try {
			sp.saveContextToJson(Paths.get(drawerPath.toString(), "algorithms", "blatt2", "data", "output.json").toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void blatt3(Path drawerPath) {
		// Read Object
		File quad = new File(ClassLoader.getSystemClassLoader().getResource("quad_2.obj").getFile());

		QuadTree qt = null;
		try {
			String quadString = FileUtils.readFileToString(quad, "UTF-8");
			qt = new QuadTree(quadString);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		qt.process();

		qt.saveToJSON(Paths.get(drawerPath.toString(), "algorithms", "blatt3", "data", "blatt3.json").toFile());
	}

	public static void blatt4(Path drawerPath) {
		// Read Object
		File points = new File(ClassLoader.getSystemClassLoader().getResource("blatt4_1.obj").getFile());
		File range = new File(ClassLoader.getSystemClassLoader().getResource("blatt4_range.obj").getFile());

		D2Tree d2t = null;
		try {
			String pointsString = FileUtils.readFileToString(points, "UTF-8");
			String rangeString = FileUtils.readFileToString(range, "UTF-8");
			d2t = new D2Tree(pointsString, rangeString);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		d2t.process();

		d2t.saveToJSON(Paths.get(drawerPath.toString(), "algorithms", "blatt4", "data", "test_blatt4.json").toFile());
	}

	public static void blatt5(Path drawerPath) {
		// Read Object
		File polygon = new File(ClassLoader.getSystemClassLoader().getResource("blatt5_2.obj").getFile());	// "blatt5/UB5_T1.obj"

		ArtGallery ag = null;
		try {
			String pointsString = FileUtils.readFileToString(polygon, "UTF-8");
			ag = new ArtGallery(pointsString, false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		ag.process();

		ag.saveToJSON(Paths.get(drawerPath.toString(), "algorithms", "blatt5", "data", "blatt5.json").toFile());
	}

}
