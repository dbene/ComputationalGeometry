package cg.algorithms.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import org.apache.commons.io.FileUtils;

public abstract class SceneProgress {
	LinkedList<SceneInterface> progress = null;

	public <T extends SceneInterface> SceneProgress(LinkedList<T> progressList) {
		this.progress = (LinkedList<SceneInterface>) progressList;
	}

	public void addScene(SceneInterface scene) {
		addScene(scene, false);
	}

	public void addScene(SceneInterface scene, boolean force) {
		if (force || progress.isEmpty() || !progress.getLast().equals(scene)) {
			progress.add(scene);
		}
	}

	public void saveContextToJson(File file) throws IOException {
		JsonObjectBuilder json = Json.createObjectBuilder();

		JsonArrayBuilder scenes = Json.createArrayBuilder();
		for (SceneInterface scene : this.progress) {
			scenes.add(scene.toJsonObject());
		}

		json.add("scenes", scenes);

		FileUtils.writeStringToFile(file, json.build().toString(), "UTF-8");
	}
}
