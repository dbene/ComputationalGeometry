package cg.progress;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.apache.commons.io.FileUtils;

public class SceneProgress {
	LinkedList<Scene> progress = new LinkedList<Scene>();

	public void addScene(Scene scene) {
		addScene(scene, false);
	}

	public void addScene(Scene scene, boolean force) {
		if (force || progress.isEmpty() || !progress.getLast().equals(scene)) {
			progress.add(scene);
		}
	}

	public void saveContextToJson(File file) throws IOException {
		JsonObjectBuilder json = Json.createObjectBuilder();

		JsonArrayBuilder scenes = Json.createArrayBuilder();
		for (Scene scene : this.progress) {
			scenes.add(scene.toJsonObject());
		}

		json.add("scenes", scenes);

		FileUtils.writeStringToFile(file, json.build().toString(), "UTF-8");
	}
}
