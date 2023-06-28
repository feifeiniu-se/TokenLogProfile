package cn.edu.nju.loggenerate.modelengineRandom.getlogs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class WriteCache {
	public boolean write = true;

	public OutputStreamWriter fwinfo = null;
	public OutputStreamWriter fwtag = null;

	public String f_path = "";
	public String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
			+ "<!-- MXML version 1.1 -->\r\n"
			+ "<!-- Created by ProM Import Framework, Version 7.0 (Propeller) -->\r\n"
			+ "<!-- via MXMLib Version 1.9 (http://promimport.sf.net/) -->\r\n"
			+ "<!-- (c) 2004-2007 C.W. Guenther (christian@deckfour.org); Eindhoven Technical University -->\r\n"
			+ "<!-- This event log is formatted in MXML, for use by BPI and Process Mining Tools. -->\r\n"
			+ "<!-- You can load this file e.g. in the ProM Framework for Process Mining. -->\r\n"
			+ "<!-- More information about MXML, Process Mining, and ProM: http://www.processmining.org/. -->\r\n"
			+ "<WorkflowLog xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://is.tm.tue.nl/research/processmining/WorkflowLog.xsd\">\r\n"
			+ "	<Data>\r\n"
			+ "		<Attribute name=\"app.name\">ProM Import Framework</Attribute>\r\n"
			+ "		<Attribute name=\"app.version\">7.0 (Propeller)</Attribute>\r\n"
			+ "		<Attribute name=\"java.vendor\">Sun Microsystems Inc.</Attribute>\r\n"
			+ "		<Attribute name=\"java.version\">1.6.0_19</Attribute>\r\n"
			+ "		<Attribute name=\"mxml.creator\">MXMLib (http://promimport.sf.net/)</Attribute>\r\n"
			+ "		<Attribute name=\"mxml.version\">1.1</Attribute>\r\n"
			+ "		<Attribute name=\"os.arch\">x86</Attribute>\r\n"
			+ "		<Attribute name=\"os.name\">Windows 7</Attribute>\r\n"
			+ "		<Attribute name=\"os.version\">6.1</Attribute>\r\n"
			+ "		<Attribute name=\"user.name\">WVDAALST</Attribute>\r\n" + "	</Data>\r\n"
			+ "	<Source program=\"Rapid Synthesizer\"/>\r\n"
			+"	<Process id=\"PipeLCY\">\r\n";
	
	private String end = "	</Process>\r\n</WorkflowLog>\r\n";

	public WriteCache(String file_path) {
		f_path = file_path;
		try {
			fwinfo = new OutputStreamWriter(new FileOutputStream(f_path, true),
					"UTF-8");
		} catch (Exception e) {

		}
	}

	public void addHeader(){
		try {
			fwinfo.append(header);
			fwinfo.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addEnd(){
		try {
			fwinfo.append(end);
			fwinfo.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean addItem(String item) {
		try {
			fwinfo.append(item);
			//System.out.print(item);
			fwinfo.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				fwinfo.close();
				fwinfo = new OutputStreamWriter(new FileOutputStream(f_path,
						true), "UTF-8");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return true;
	}

}