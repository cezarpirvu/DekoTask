import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVFile {
	
	private String csvPath;
	private List<String[]> entriesList;
	
	public CSVFile(String csvPath, List<String[]> entriesList) {
		this.csvPath = csvPath;
		this.entriesList = entriesList;
	}
	
	// read a csv file from a path and store the entries to a list
	public void readCSV() {
		try {
			CSVReader csvReader = new CSVReader(new FileReader(csvPath));
			try {
				entriesList = csvReader.readAll();
				entriesList.remove(0);
				
				// sort the list
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
	public void writeCSV() {
		try {
			CSVWriter csvWriter = new CSVWriter(new FileWriter("./output/users.csv"));
			
			// add the header for the csv file
			List<String[]> list = new ArrayList<>(Main.entriesList);
			String[] header = new String[6];
			header[0] = "User ID";
			header[1] = "First Name";
			header[2] = "Last Name";
			header[3] = "User Name";
			header[4] = "User Type";
			header[5] = "Last Login Time";
			list.add(0, header);
			
			csvWriter.writeAll(list);
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
