import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import cg.algorithms.GrahamsScan;
import cg.algorithms.utils.Point;
import cg.progress.SceneProgress;

public class Main {

	public static void main(String[] args) {
		// Read Object
		File file = new File(ClassLoader.getSystemClassLoader().getResource("test.obj").getFile());

		ArrayList<Point> coordinates = new ArrayList<Point>();

		try {
			String object = FileUtils.readFileToString(file, "UTF-8");
			coordinates = Point.parseObj(object);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

//		for (int i = 0; i < 25; i++) {
//			int x = (int) (Math.random() * (450 - 50) + 50);
//			int y = (int) (Math.random() * (450 - 50) + 50);
//			coordinates.add(new Point(x, y));
//		}
//		for (int i = 0; i < 25; i++) {
//			int x = (int) (Math.random() * (350 - 150) + 150);
//			int y = (int) (Math.random() * (350 - 150) + 150);
//			coordinates.add(new Point(x, y));
//		}

//		for (Point point : coordinates) {
//			System.out.println("v " + point.x + " " + point.y);
//		}

		GrahamsScan gs = new GrahamsScan(coordinates);
		SceneProgress sp = gs.process();

		try {
			sp.saveContextToJson(new File("C:\\Users\\dasbene\\Desktop\\drawer_dev\\cg_drawer\\data\\output.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
