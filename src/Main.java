import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// custom comparator class to sort the list ascending by user id
class SortComparator implements Comparator<String[]>{

	@Override
	public int compare(String[] o1, String[] o2) {
		// TODO Auto-generated method stub
		return Integer.parseInt(o1[0]) - Integer.parseInt(o2[0]);
	}
}

// custom exceptions
class CustomException extends Exception{
	private static final long serialVersionUID = 1L;
	public CustomException() {
		
	}
	public CustomException(String message) {
		super(message);
	}
}

public class Main {
	
	public static List<String[]> entriesList = new ArrayList<>();
	
	// extract the extension of the file
	public static String getFileType(File file) throws CustomException {
		
		if (file != null) {
			String fileName = file.getName();
			int extensionStart = fileName.lastIndexOf('.');
			
			if (extensionStart != 0 && extensionStart != -1)
				return fileName.substring(extensionStart + 1);
			else throw new CustomException("The files do not have an extension!");
		} else
			throw new CustomException("The file is null!");
	}
	
	// read csv, json or xml files from the data folder
	public static void readFiles(String...extensions) throws CustomException {
		
		if (extensions.length != 0) {
			// retrieve all the files from the data folder
			File f = new File(Constants.INPUT_PATH);
			File[] files = f.listFiles();
			
			for(File file : files) {
				// path of the file
				String path = Constants.INPUT_PATH + file.getName();
				
				for(String extension : extensions) {
					// read the files according to the specified extensions
					if (getFileType(file).equals(extension)) {
						if (getFileType(file).equals(Constants.CSV_EXTENSION)) {
							CSVFile csv = new CSVFile(path, entriesList);
							csv.readCSV();
							// update the list
							entriesList = new ArrayList<>(csv.getEntriesList());
						} else {
							if (getFileType(file).equals(Constants.JSON_EXTENSION)) {
								JSONFile json = new JSONFile(path, entriesList);
								json.readJSON();
								// update the list
								entriesList = new ArrayList<>(json.getEntriesList());
							} else {
								if (getFileType(file).equals(Constants.XML_EXTENSION)) {
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
		} else throw new CustomException("Files cannot be read because no extensions have been provided!");
	}
	
	// write csv, json or xml files to the output folder
	public static void writeFiles(String...extensions) throws CustomException {
		
		if (extensions.length != 0) {
			for(String extension : extensions) {
				if (extension.equals(Constants.CSV_EXTENSION))
					CSVFile.writeCSV();
				else
					if (extension.equals(Constants.JSON_EXTENSION))
						JSONFile.writeJSON();
					else
						if (extension.equals(Constants.XML_EXTENSION))
							XMLFile.writeXML();
			}
		} else
			throw new CustomException("No file types will be written because no extension have been provided!");
	}
	
	public static void main(String[] args) throws CustomException {
		// TODO Auto-generated method stub
		
		if (entriesList != null) {
			// read the specified types of files from the data folder
			// currently supported formats are csv, json, xml
			readFiles("csv", "xml", "json");
			// write the specified types of files in the output folder
			// currently supported formats are csv, json, xml
			writeFiles("csv", "xml", "json");
		} else throw new CustomException("The list is null!");
	}
}
