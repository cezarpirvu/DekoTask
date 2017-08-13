import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JSONFile {
	
	private String jsonPath;
	private List<String[]> entriesList;
	// preserve the correct order for the json values
	private static List<String> jsonKeys = Arrays.asList("user_id", "first_name", "last_name", "username", "user_type", "last_login_time");
	
	public JSONFile(String jsonPath, List<String[]> entriesList) {
		this.jsonPath = jsonPath;
		this.entriesList = entriesList;
	}
	
	// read the json file from a path and update the list of entries
	public void readJSON() {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(jsonPath));
			JSONArray jsonArray = (JSONArray) obj;
			
			for(int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonAttr = (JSONObject) jsonArray.get(i);
				String[] jsonEntry = new String[jsonAttr.size()];
				
				for(int j = 0; j < jsonEntry.length; j++) 
					jsonEntry[j] = String.valueOf(jsonAttr.get(jsonKeys.get(j)));
				entriesList.add(jsonEntry);
			}	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// sort the list
		Collections.sort(entriesList, new SortComparator());
	}
	
	// write the json file
	public static void writeJSON() {
		JSONArray jsonWriter = new JSONArray();
		
		for(String[] entry : Main.entriesList) {
			LinkedHashMap<String, Object> obj = new LinkedHashMap<>();
			for(int i = 0; i < entry.length; i++) {
				if (i == 0)
					obj.put(jsonKeys.get(i), Integer.parseInt(entry[i]));
				else obj.put(jsonKeys.get(i), entry[i]);
			}
			jsonWriter.add(obj);
		}
		
		try (FileWriter file = new FileWriter(Constants.JSON_OUTPUT_PATH)) {
			try {
				// pretty print the json file
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonParser jp = new JsonParser();
				JsonElement je = jp.parse(JSONArray.toJSONString(jsonWriter));
				file.write(gson.toJson(je));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public List<String[]> getEntriesList() {
		return entriesList;
	}
}
