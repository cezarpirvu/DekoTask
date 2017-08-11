import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Main {
	
	private static String inputPath = "./data/";
	private static String outputPath = "./output/";
	
	// extract the extension of the file
	public static String getFileType(File file) {
		String fileName = file.getName();
		int extensionIndex = fileName.lastIndexOf('.');
		if (extensionIndex != 0 && extensionIndex != -1)
			return fileName.substring(extensionIndex + 1);
		else return "No extension found!";
	}
	
	// read each file
	public static void readFiles() {
		File f = new File(inputPath);
		File[] files = f.listFiles();
		
		for(File file : files) {
			// find and read csv files
			if (getFileType(file).equals("csv")) {
				String csvPath = inputPath + file.getName();
				java.util.List<String[]> entriesList = new ArrayList<>();
				
				try {
					CSVReader csvReader = new CSVReader(new FileReader(csvPath));
					try {
						entriesList = csvReader.readAll();
						for(String[] entry : entriesList) {
							for(String e : entry) {
								System.out.print(e + " ");
							}
							System.out.println();
						}
						csvReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				CSVWriter csvWriter;
				try {
					csvWriter = new CSVWriter(new FileWriter("./data/example.csv"));
					csvWriter.writeAll(entriesList);
					csvWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			} else {
				
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
