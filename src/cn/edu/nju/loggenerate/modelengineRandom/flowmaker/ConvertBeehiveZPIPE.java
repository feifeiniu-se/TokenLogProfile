package cn.edu.nju.loggenerate.modelengineRandom.flowmaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConvertBeehiveZPIPE {

	static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

	public static void fromPIPEtoBeehiveZ(String sourceFolder, String desFolder) {
		File desFolderFile = new File(desFolder);
		if (!desFolderFile.exists()) {
			desFolderFile.mkdir();
		}

		File sourceFolderFile = new File(sourceFolder);
		File[] sourceFiles = sourceFolderFile.listFiles();
		try {
			for (File inputFile : sourceFiles) {
				String fileName = inputFile.getName().split("\\.")[0];
				BufferedWriter writer = new BufferedWriter(new FileWriter(desFolder + "\\" + fileName + ".pnml"));
				
				Document document = parse(inputFile);
		        Element rootElement = document.getDocumentElement();
		        
		        writer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		        writer.newLine();
		        writer.append("<pnml>");
		        writer.newLine();
		        writer.append("<net id=\"workflownet\" type=\"http://www.informatik.hu-berlin.de/top/pnml/basicPNML.rng\">");
		        writer.newLine();
		        
		        NodeList placeList = rootElement.getElementsByTagName("place");
		        for(int i = 0 ; i < placeList.getLength(); i++) {
		        	String placeBeehiveZ = constructPlaceBeehiveZFromPIPE((Element) placeList.item(i));
		        	writer.append(placeBeehiveZ);
		        	writer.newLine();
		        }
		        
		        NodeList transitionList = rootElement.getElementsByTagName("transition");
		        for(int i = 0 ; i < transitionList.getLength(); i++) {
		        	String transitionBeehiveZ = constructTransitionBeehiveZFromPIPE((Element) transitionList.item(i));
		        	writer.append(transitionBeehiveZ);
		        	writer.newLine();
		        }
		        
		        NodeList arcList = rootElement.getElementsByTagName("arc");
		        for(int i = 0 ; i < arcList.getLength(); i++) {
		        	String arcBeehiveZ = constructArcBeehiveZFromPIPE((Element) arcList.item(i));
		        	writer.append(arcBeehiveZ);
		        	writer.newLine();
		        }
		        
		        writer.append("</net>\r\n" + "</pnml>");
		        writer.newLine();
		        writer.flush();
		        
		        writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String constructArcBeehiveZFromPIPE(Element arc) {
		// TODO Auto-generated method stub
		String arcID = arc.getAttribute("id");
    	String arcSource = arc.getAttribute("source");
    	String arcTarget = arc.getAttribute("target");
    	
    	Element point = (Element) arc.getElementsByTagName("arcpath").item(0);
    	String startX = point.getAttribute("x");
    	String startY = point.getAttribute("y");
    	
    	Element end = (Element) arc.getElementsByTagName("arcpath").item(1);
    	String endX = point.getAttribute("x");
    	String endY = point.getAttribute("y");
    	
		String arcBeehiveZ = "    <arc id=\"" + arcID + "\" source=\"" + arcSource + "\" target=\"" + arcTarget + "\">\r\n" + 
				"        <toolspecific tool=\"ProM\" version=\"5.2\">\r\n" + 
				"            <spline>\r\n" + 
				"                <point x=\"" + startX + "\" y=\"" + startY + "\" />\r\n" + 
				"                <end x=\"" + endX + "\" y=\"" + endY + "\" />\r\n" + 
				"            </spline>\r\n" + 
				"        </toolspecific>\r\n" + 
				"    </arc>";
		return arcBeehiveZ;
	}

	private static String constructTransitionBeehiveZFromPIPE(Element transition) {
		// TODO Auto-generated method stub
		String transitionID = transition.getAttribute("id");
    	
    	Element position = (Element) transition.getElementsByTagName("position").item(0);
    	String transitionX = position.getAttribute("x");
    	String transitionY = position.getAttribute("y");
    	
    	String transitionBeehiveZ = "\t<transition id=\"" + transitionID + "\">\r\n" + 
    			"        <graphics>\r\n" + 
    			"            <position  x=\"" + transitionX + "\" y=\"" + transitionY + "\" />\r\n" + 
    			"            <dimension x=\"20\" y=\"17\" />\r\n" + 
    			"        </graphics>\r\n" + 
    			"        <name>\r\n" + 
    			"            <text>" + transitionID + "</text>\r\n" + 
    			"         </name>\r\n" + 
    			"        <toolspecific tool=\"ProM\" version=\"5.2\">\r\n" + 
    			"            <logevent>\r\n" + 
    			"                <name>" + transitionID + "</name>\r\n" + 
    			"                <type>auto</type>\r\n" + 
    			"            </logevent>\r\n" + 
    			"        </toolspecific>\r\n" + 
    			"    </transition>";
    	return transitionBeehiveZ;
	}

	private static String constructPlaceBeehiveZFromPIPE(Element place) {
		// TODO Auto-generated method stub
		String placeID = place.getAttribute("id");
    	Element position = (Element) place.getElementsByTagName("position").item(0);
    	String placeX = position.getAttribute("x");
    	String placeY = position.getAttribute("y");
    	
    	String placeBeehiveZ = "    <place id=\"" + placeID + "\">\r\n" + 
    			"        <graphics>\r\n" + 
    			"            <position x=\"" + placeX + "\" y=\"" + placeY + "\" />\r\n" + 
    			"            <dimension x=\"13\" y=\"13\" />\r\n" + 
    			"        </graphics>\r\n" + 
    			"        <name>\r\n" + 
    			"            <text>" + placeID + "</text>\r\n" + 
    			"         </name>\r\n" + 
    			"    </place>";
    	return placeBeehiveZ;
	}

	public static void fromBeehiveZtoPIPE(String sourceFolder, String desFolder) {
		File desFolderFile = new File(desFolder);
		if (!desFolderFile.exists()) {
			desFolderFile.mkdir();
		}

		File sourceFolderFile = new File(sourceFolder);
		File[] sourceFiles = sourceFolderFile.listFiles();
		try {
			for (File inputFile : sourceFiles) {
				String fileName = inputFile.getName().split("\\.")[0];
				BufferedWriter writer = new BufferedWriter(new FileWriter(desFolder + "\\" + fileName + ".xml"));
				
				Document document = parse(inputFile);
		        Element rootElement = document.getDocumentElement();
		        
		        writer.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
		        writer.newLine();
		        writer.append("<pnml>");
		        writer.newLine();
		        writer.append("<net id=\"Net-One\" type=\"P/T net\">");
		        writer.newLine();
		        writer.append("	<token id=\"Default\" enabled=\"true\" red=\"0\" green=\"0\" blue=\"0\"/>");
		        writer.newLine();
		        
		        NodeList placeList = rootElement.getElementsByTagName("place");
		        for(int i = 0 ; i < placeList.getLength(); i++) {
		        	String placePIPE = constructPlacePIPEFromBeehiveZ((Element) placeList.item(i));
		        	writer.append(placePIPE);
		        	writer.newLine();
		        }
		        
		        NodeList transitionList = rootElement.getElementsByTagName("transition");
		        for(int i = 0 ; i < transitionList.getLength(); i++) {
		        	String transitionPIPE = constructTransitionPIPEFromBeehiveZ((Element) transitionList.item(i));
		        	writer.append(transitionPIPE);
		        	writer.newLine();
		        }
		        
		        NodeList arcList = rootElement.getElementsByTagName("arc");
		        for(int i = 0 ; i < arcList.getLength(); i++) {
		        	String arcPIPE = constructArcPIPEFromBeehiveZ((Element) arcList.item(i));
		        	writer.append(arcPIPE);
		        	writer.newLine();
		        }
		        
		        writer.append("</net>\r\n" + "</pnml>");
		        writer.newLine();
		        writer.flush();
		        
		        writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String constructArcPIPEFromBeehiveZ(Element arc) {
		// TODO Auto-generated method stub
		String arcID = arc.getAttribute("id");
    	String arcSource = arc.getAttribute("source");
    	String arcTarget = arc.getAttribute("target");
    	
    	Element point = (Element) arc.getElementsByTagName("point").item(0);
    	String startX = point.getAttribute("x");
    	String startY = point.getAttribute("y");
    	
    	Element end = (Element) arc.getElementsByTagName("end").item(0);
    	String endX = point.getAttribute("x");
    	String endY = point.getAttribute("y");
    	
    	String arcPIPE = "\t<arc id=\"" + arcSource + " to " + arcTarget + "\" source=\""+ arcSource +"\" target=\"" + arcTarget + "\">\r\n" + 
    			"		<graphics/>\r\n" + 
    			"		<inscription>\r\n" + 
    			"			<value>Default,1</value>\r\n" + 
    			"			<graphics/>\r\n" + 
    			"		</inscription>\r\n" + 
    			"		<tagged>\r\n" + 
    			"			<value>false</value>\r\n" + 
    			"		</tagged>\r\n" + 
    			"		<arcpath id=\"000\" x=\"" + startX + "\" y=\"" + startY + "\" curvePoint=\"false\"/>\r\n" + 
    			"		<arcpath id=\"001\" x=\"" + endX + "\" y=\"" + endY + "\" curvePoint=\"false\"/>\r\n" + 
    			"		<type value=\"normal\"/>\r\n" + 
    			"	</arc>";
    	return arcPIPE;
	}

	private static String constructTransitionPIPEFromBeehiveZ(Element transition) {
		// TODO Auto-generated method stub
		String transitionID = transition.getAttribute("id");
    	
    	Element position = (Element) transition.getElementsByTagName("position").item(0);
    	String transitionX = position.getAttribute("x");
    	String transitionY = position.getAttribute("y");
    	
    	Element name = (Element) transition.getElementsByTagName("text").item(0);
    	String transitionName = name.getTextContent();
    	
    	String transitionPIPE = "\t<transition id=\"" + transitionID + "\">\r\n" + 
    			"		<graphics>\r\n" + 
    			"			<position x=\"" + transitionX + "\" y=\"" + transitionY + "\"/>\r\n" + 
    			"		</graphics>\r\n" + 
    			"		<name>\r\n" + 
    			"			<value>" + transitionID + "</value>\r\n" + 
    			"			<graphics>\r\n" + 
    			"				<offset x=\"-5.0\" y=\"35.0\"/>\r\n" + 
    			"			</graphics>\r\n" + 
    			"		</name>\r\n" + 
    			"		<orientation>\r\n" + 
    			"			<value>0</value>\r\n" + 
    			"		</orientation>\r\n" + 
    			"		<rate>\r\n" + 
    			"			<value>1.0</value>\r\n" + 
    			"		</rate>\r\n" + 
    			"		<timed>\r\n" + 
    			"			<value>true</value>\r\n" + 
    			"		</timed>\r\n" + 
    			"		<infiniteServer>\r\n" + 
    			"			<value>false</value>\r\n" + 
    			"		</infiniteServer>\r\n" + 
    			"		<priority>\r\n" + 
    			"			<value>1</value>\r\n" + 
    			"		</priority>\r\n" + 
    			"	</transition>";
    	return transitionPIPE;
	}

	private static String constructPlacePIPEFromBeehiveZ(Element place) {
		// TODO Auto-generated method stub
		String placeID = place.getAttribute("id");
    	Element position = (Element) place.getElementsByTagName("position").item(0);
    	String placeX = position.getAttribute("x");
    	String placeY = position.getAttribute("y");
    	
    	Element name = (Element) place.getElementsByTagName("text").item(0);
    	String placeName = name.getTextContent();
    	
    	String placePIPE = "\t<place id=\"" + placeID + "\">\r\n" + 
    			"		<graphics>\r\n" + 
    			"			<position x=\"" + placeX + "\" y=\"" + placeY + "\"/>\r\n" + 
    			"		</graphics>\r\n" + 
    			"		<name>\r\n" + 
    			"			<value>" + placeID + "</value>\r\n" + 
    			"			<graphics>\r\n" + 
    			"				<offset x=\"0.0\" y=\"0.0\"/>\r\n" + 
    			"			</graphics>\r\n" + 
    			"		</name>\r\n" + 
    			"		<initialMarking>\r\n" + 
    			"			<value>Default,0</value>\r\n" + 
    			"			<graphics>\r\n" + 
    			"				<offset x=\"0.0\" y=\"0.0\"/>\r\n" + 
    			"			</graphics>\r\n" + 
    			"		</initialMarking>\r\n" + 
    			"		<capacity>\r\n" + 
    			"			<value>0</value>\r\n" + 
    			"		</capacity>\r\n" + 
    			"	</place>";
    	return placePIPE;
	}

	public static Document parse(File file) {
		Document document = null;
		try {
			// DOM parser instance
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			// parse an XML file into a DOM tree
			document = builder.parse(file);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}
}
