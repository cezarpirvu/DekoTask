import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
				
				jsonEntry[0] = String.valueOf(jsonAttr.get("user_id"));
				jsonEntry[1] = (String) jsonAttr.get("first_name");
				jsonEntry[2] = (String) jsonAttr.get("last_name");
				jsonEntry[3] = (String) jsonAttr.get("username");
				jsonEntry[4] = (String) jsonAttr.get("user_type");
				jsonEntry[5] = (String) jsonAttr.get("last_login_time");
				
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
	public void writeJSON() {
		JSONArray jsonWriter = new JSONArray();
		for(String[] entry : Main.entriesList) {
			LinkedHashMap<String, String> obj = new LinkedHashMap<>();
			obj.put("user_id", entry[0]);
			obj.put("first_name", entry[1]);
			obj.put("last_name", entry[2]);
			obj.put("username", entry[3]);
			obj.put("user_type", entry[4]);
			obj.put("last_login_time", entry[5]);
			jsonWriter.add(obj);
		}
		
		try (FileWriter file = new FileWriter("./output/users.json")) {
			try {
				// pretty print
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
