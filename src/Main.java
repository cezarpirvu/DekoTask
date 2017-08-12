import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

// custom comparator class to sort the list ascending by user id
class SortComparator implements Comparator<String[]>{

	@Override
	public int compare(String[] o1, String[] o2) {
		// TODO Auto-generated method stub
		return Integer.parseInt(o1[0]) - Integer.parseInt(o2[0]);
	}
}

public class Main {
	
	// extract the extension of the file
	public static String getFileType(File file) {
		String fileName = file.getName();
		int extensionStart = fileName.lastIndexOf('.');
		
		if (extensionStart != 0 && extensionStart != -1)
			return fileName.substring(extensionStart + 1);
		else return "No extension found!";
	}
	
	// read each file
	public static void readFiles() {
		File f = new File(Constants.INPUT_PATH);
		File[] files = f.listFiles();
		
		for(File file : files) {
			// find and read csv files
			if (getFileType(file).equals("csv")) {
				String csvPath = Constants.INPUT_PATH + file.getName();
				java.util.List<String[]> csvEntriesList = new ArrayList<>();
				
				try {
					CSVReader csvReader = new CSVReader(new FileReader(csvPath));
					try {
						csvEntriesList = csvReader.readAll();
						csvEntriesList.remove(0);
						for(String[] entry : csvEntriesList) {
							for(String e : entry) {
								System.out.print(e + " ");
							}
							System.out.println();
						}
						
						Collections.sort(csvEntriesList, new SortComparator());	
						
						csvReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					CSVWriter csvWriter = new CSVWriter(new FileWriter("./output/users.csv"));
					csvWriter.writeAll(csvEntriesList);
					csvWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// find and read json files
				if (getFileType(file).equals("json")) {
					String jsonPath = Constants.INPUT_PATH + file.getName();
					java.util.List<String[]> jsonEntriesList = new ArrayList<>();
					
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
							
							jsonEntriesList.add(jsonEntry);
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
					
					JSONArray jsonWriter = new JSONArray();
					for(String[] entry : jsonEntriesList) {
						LinkedHashMap<String, String> obj = new LinkedHashMap<>();
						obj.put("user_id", entry[0]);
						obj.put("first_name", entry[1]);
						obj.put("last_name", entry[2]);
						obj.put("username", entry[3]);
						obj.put("user_type", entry[4]);
						obj.put("last_login_time", entry[5]);
						jsonWriter.add(obj);
					}
					
					// try-with-resources statement based on post comment below :)
					try (FileWriter file1 = new FileWriter("./output/users.json")) {
						try {
							
							Gson gson = new GsonBuilder().setPrettyPrinting().create();
							JsonParser jp = new JsonParser();
							JsonElement je = jp.parse(JSONArray.toJSONString(jsonWriter));
							String prettyJsonString = gson.toJson(je);
							file1.write(prettyJsonString);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				} else {
					// find and read xml files
					if (getFileType(file).equals("xml")) {
						//TODO
					} else {
						//TODO ERROR
					}
				}
				
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		File file = new File("./data");
//		File[] files = file.listFiles();
//		
//		for(File f : files) {
//			System.out.println(f.getName());
//			getFileType(f);
//		}
		
		readFiles();
	}
}
