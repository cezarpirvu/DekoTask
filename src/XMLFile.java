import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLFile {
	
	private String xmlPath;
	private List<String[]> entriesList;
	
	public XMLFile(String xmlPath, List<String[]> entriesList) {
		this.xmlPath = xmlPath;
		this.entriesList = entriesList;
	}
	
	// read the contents of an xml file and save them to the list
	public void readXML() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File(xmlPath));
			document.getDocumentElement().normalize();
			NodeList userList = document.getElementsByTagName("user");
			
			for (int i = 0; i < userList.getLength(); i++)
			{
			 Node node = userList.item(i);
			 String[] xmlEntry = new String[6];
			 if (node.getNodeType() == Node.ELEMENT_NODE)
			 {
			    Element entry = (Element) node;
			    xmlEntry[0] = entry.getElementsByTagName("userid").item(0).getTextContent();
			    xmlEntry[1] = entry.getElementsByTagName("firstname").item(0).getTextContent();
			    xmlEntry[2] = entry.getElementsByTagName("surname").item(0).getTextContent();
			    xmlEntry[3] = entry.getElementsByTagName("username").item(0).getTextContent();
			    xmlEntry[4] = entry.getElementsByTagName("type").item(0).getTextContent();
			    xmlEntry[5] = entry.getElementsByTagName("lastlogintime").item(0).getTextContent();
			 }
			 entriesList.add(xmlEntry);
			}
			
			// sort the list
			Collections.sort(entriesList, new SortComparator());
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	// write the sorted list in a xml type document
	public void writeXML() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document;
		
		try {
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			
			Element usersRoot = document.createElement("users");
			document.appendChild(usersRoot);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result =
			         new StreamResult(new File("./output/users.xml"));
			transformer.transform(source, result);
			StreamResult consoleResult =
			         new StreamResult(System.out);
			transformer.transform(source, consoleResult);
		} catch (ParserConfigurationException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public List<String[]> getEntriesList() {
		return entriesList;
	}

}
