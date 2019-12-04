import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import cg.algorithms.polygonsintersect.IntersectConvexPolygon;
import cg.algorithms.polygonsintersect.Polygon;
import cg.algorithms.utils.SceneProgress;

public class Main {

	public static void main(String[] args) {
		// Read Object
		File poly1File = new File(ClassLoader.getSystemClassLoader().getResource("poly1.obj").getFile());
		File poly2File = new File(ClassLoader.getSystemClassLoader().getResource("poly2.obj").getFile());

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
			sp.saveContextToJson(new File("C:\\Users\\dasbene\\Desktop\\drawer_dev\\cg_drawer\\data\\output.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
