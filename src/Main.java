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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

// custom comparator class to sort the list ascending by user id
class SortComparator implements Comparator<String[]>{

	@Override
	public int compare(String[] o1, String[] o2) {
		// TODO Auto-generated method stub
		return Integer.parseInt(o1[0]) - Integer.parseInt(o2[0]);
	}
}

public class Main {
	
	public static List<String[]> entriesList = new ArrayList<>();
	
	// extract the extension of the file
	public static String getFileType(File file) {
		String fileName = file.getName();
		int extensionStart = fileName.lastIndexOf('.');
		
		if (extensionStart != 0 && extensionStart != -1)
			return fileName.substring(extensionStart + 1);
		else return "No extension found!";
	}
	
	// read csv, json or xml files from the data folder
	public static void readFiles(String...extensions) {
		// retrieve all the files from the data folder
		File f = new File(Constants.INPUT_PATH);
		File[] files = f.listFiles();
		
		for(File file : files) {
			// path of the file
			String path = Constants.INPUT_PATH + file.getName();
			
			for(String extension : extensions) {
				// read the files according to the specified extensions
				if (getFileType(file).equals(extension)) {
					if (getFileType(file).equals("csv")) {
						CSVFile csv = new CSVFile(path, entriesList);
						csv.readCSV();
						// update the list
						entriesList = new ArrayList<>(csv.getEntriesList());
					} else {
						if (getFileType(file).equals("json")) {
							JSONFile json = new JSONFile(path, entriesList);
							json.readJSON();
							// update the list
							entriesList = new ArrayList<>(json.getEntriesList());
						} else {
							if (getFileType(file).equals("xml")) {
								XMLFile xml = new XMLFile(path, entriesList);
								xml.readXML();
								// update the list
								entriesList = new ArrayList<>(xml.getEntriesList());
							}
						}
					}
				}
			}
		}
	}
	
	// write csv, json or xml files to the output folder
	public static void writeFiles(String...extensions) {
		for(String extension : extensions) {
			if (extension.equals("csv"))
				CSVFile.writeCSV();
			else
				if (extension.equals("json"))
					JSONFile.writeJSON();
				else
					if (extension.equals("xml"))
						XMLFile.writeXML();
		}
	}
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		
		// read the specified types of files from the data folder
		readFiles("csv", "xml", "json");
		// write the specified types of files in the output folder
		writeFiles("csv", "xml", "json");
	}
}
