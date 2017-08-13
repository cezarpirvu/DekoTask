import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
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
	// list containing the xml tags
	private List<String> xmlTags = Arrays.asList("userid", "firstname", "surname", "username", "type", "lastlogintime");
	
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
			
			for (int i = 0; i < userList.getLength(); i++){
			 Node node = userList.item(i);
			 String[] xmlEntry = new String[xmlTags.size()];
			 
			 if (node.getNodeType() == Node.ELEMENT_NODE){
			    Element entry = (Element) node;
			    for(int j = 0; j < xmlEntry.length; j++)
			    	xmlEntry[j] = entry.getElementsByTagName(xmlTags.get(j)).item(0).getTextContent();
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
	
	// sort and write the merged list in a xml type document
	public void writeXML() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document document;
		
		try {
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			
			Element usersRoot = document.createElement("users");
			document.appendChild(usersRoot);
			
			for(String[] entry : entriesList) {
				Element user = document.createElement("user");
				usersRoot.appendChild(user);
				
				for(int i = 0; i < xmlTags.size(); i++) {
					Element element = document.createElement(xmlTags.get(i));
					element.appendChild(document.createTextNode(entry[i]));
					user.appendChild(element);
				}
			}
			
			// pretty print the xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
		    StreamResult result = new StreamResult(new File(Constants.XML_OUTPUT_PATH));
			transformer.transform(source, result);
		} catch (ParserConfigurationException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String[]> getEntriesList() {
		return entriesList;
	}

}