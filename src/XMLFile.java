import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
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
			
			for(String[] entry : entriesList) {
				Element user = document.createElement("user");
				usersRoot.appendChild(user);
				
				Element userId = document.createElement("userid");
				user.appendChild(userId);
				userId.appendChild(document.createTextNode(entry[0]));
				
				Element firstName = document.createElement("firstname");
				user.appendChild(firstName);
				firstName.appendChild(document.createTextNode(entry[1]));
				
				Element surName = document.createElement("surname");
				user.appendChild(surName);
				surName.appendChild(document.createTextNode(entry[2]));
				
				Element userName = document.createElement("username");
				user.appendChild(userName);
				userName.appendChild(document.createTextNode(entry[3]));
				
				Element type = document.createElement("type");
				user.appendChild(type);
				type.appendChild(document.createTextNode(entry[4]));
				
				Element lastLoginTime = document.createElement("lastlogintime");
				user.appendChild(lastLoginTime);
				lastLoginTime.appendChild(document.createTextNode(entry[5]));
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File("./output/users.xml"));
			
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
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
