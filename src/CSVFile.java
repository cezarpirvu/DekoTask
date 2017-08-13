import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVFile {
	
	private String csvPath;
	private List<String[]> entriesList;
	private static String[] header;
	
	public CSVFile(String csvPath, List<String[]> entriesList) {
		this.csvPath = csvPath;
		this.entriesList = entriesList;
	}
	
	// removes and retrieves the header of the csv file (User Id, First Name...)
	public String[] retrieveHeader(List<String[]> entriesList) {
		// save the header
		int length = entriesList.get(0).length;
		String[] header = new String[length];
		
		for(int i = 0; i < length; i++)
			header[i] = entriesList.get(0)[i];
		// remove it from the list
		entriesList.remove(0);
		
		return header;
	}
	
	// read a csv file from a path and merge the entries to a list
	public void readCSV() {
		try {
			CSVReader csvReader = new CSVReader(new FileReader(csvPath));
			try {
				entriesList = csvReader.readAll();
				// removes and retrieves the header
				header = retrieveHeader(entriesList);
				// sort the list ascending by user id
				Collections.sort(entriesList, new SortComparator());	
				csvReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// write the csv file
	public static void writeCSV() {
		try {
			CSVWriter csvWriter = new CSVWriter(new FileWriter(Constants.CSV_OUTPUT_PATH));
			// add the header for the csv file
			Main.entriesList.add(0, header);
			csvWriter.writeAll(Main.entriesList);
			Main.entriesList.remove(0);
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String[]> getEntriesList() {
		return entriesList;
	}
	
}
