package invaders.engine;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigReader {
	private JSONObject jsonGame;
	private JSONArray jsonEnemies;
	private JSONArray jsonBunkers;
	private JSONObject jsonPlayer;
	/**
	 * You will probably not want to use a static method/class for this.
	 * 
	 * This is just an example of how to access different parts of the json
	 * 
	 * @param path The path of the json file to read
	 */

	public ConfigReader(String path) {

		JSONParser parser = new JSONParser();
		try {
			Object object = parser.parse(new FileReader(path));
			JSONObject jsonObject = (JSONObject) object;
			this.jsonGame = (JSONObject) jsonObject.get("Game");
			this.jsonEnemies = (JSONArray) jsonObject.get("Enemies");
			for (Object obj : this.jsonEnemies) {
				JSONObject jsonEnemy = (JSONObject) obj;
			}
			this.jsonBunkers = (JSONArray) jsonObject.get("Bunkers");
			for (Object obj : this.jsonBunkers) {
				JSONObject jsonBunker = (JSONObject) obj;
			}
			this.jsonPlayer = (JSONObject) jsonObject.get("Player");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public JSONArray getEnemies() {
		return this.jsonEnemies;
	}

	public JSONObject getGame() {
		return this.jsonGame;
	}
	public JSONArray getBunkers() {
		return this.jsonBunkers;
	}
	public JSONObject getPlayer() {
		return this.jsonPlayer;
	}

	/**
	 * Your main method will probably be in another file!
	 * 
	 * @param args First argument is the path to the config file
	 */
	public static void main(String[] args) {
		String configPath;
		if (args.length > 0) {
			configPath = args[0];
		} else {
			configPath = "src/main/resources/config.json";
		}
		// parse the file:
		ConfigReader reader = new ConfigReader(configPath);
		JSONObject jsonPlayer = reader.getPlayer();
		System.out.println(jsonPlayer);
	}

}
