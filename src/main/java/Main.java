import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import cg.algorithms.blatt4.D2Tree;
import cg.algorithms.polygonsintersect.IntersectConvexPolygon;
import cg.algorithms.polygonsintersect.Polygon;
import cg.algorithms.quadtrees.QuadTree;
import cg.algorithms.utils.SceneProgress;

public class Main {

	public static void main(String[] args) {
		blatt3();
	}

	public static void blatt4() {
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

		d2t.saveToJSON(new File("C:\\Users\\dasbene\\Desktop\\drawer_dev\\ComputationalGeometryDrawer\\data\\test_blatt4.json"));
	}

	public static void blatt3() {
		// Read Object
		File quad = new File(ClassLoader.getSystemClassLoader().getResource("UB2_T3.obj").getFile());

		QuadTree qt = null;
		try {
			String quadString = FileUtils.readFileToString(quad, "UTF-8");
			qt = new QuadTree(quadString);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		qt.process();

		qt.saveToJSON(new File("C:\\Users\\dasbene\\Desktop\\drawer_dev\\ComputationalGeometryDrawer\\data\\blatt3.json"));
	}

	public static void blatt2() {
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
			sp.saveContextToJson(new File("C:\\Users\\dasbene\\Desktop\\drawer_dev\\ComputationalGeometryDrawer\\data\\blatt2.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
