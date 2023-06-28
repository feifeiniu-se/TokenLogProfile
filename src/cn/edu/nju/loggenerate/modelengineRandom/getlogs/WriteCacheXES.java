package cn.edu.nju.loggenerate.modelengineRandom.getlogs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import cn.edu.nju.loggenerate.modelengineRandom.logbeans.AlphaLogItem;

public class WriteCacheXES {

	public boolean write = true;

	public OutputStreamWriter fwinfo = null;
	public OutputStreamWriter fwtag = null;

	public String logTimeString = "";
	public String logName = "";
	public String f_path = "";
	public String header1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n"
			+ "<!-- This file has been generated with the OpenXES library. It conforms -->\r\n"
			+ "<!-- to the XML serialization of the XES standard for log storage and -->\r\n"
			+ "<!-- management. -->\r\n"
			+ "<!-- XES standard version: 1.0 -->\r\n"
			+ "<!-- OpenXES library version: 1.0RC7 -->\r\n"
			+ "<!-- OpenXES is available from http://code.deckfour.org/xes/ -->\r\n"
			+ "<log xes.version=\"1.0\" xes.features=\"arbitrary-depth\" openxes.version=\"1.0RC7\" xmlns=\"http://code.deckfour.org/xes\">\r\n"
			+ "	<extension name=\"Lifecycle\" prefix=\"lifecycle\" uri=\"http://code.fluxicon.com/xes/lifecycle.xesext\"/>\r\n"
			+ "	<extension name=\"Time\" prefix=\"time\" uri=\"http://code.fluxicon.com/xes/time.xesext\"/>\r\n"
			+ "	<extension name=\"Concept\" prefix=\"concept\" uri=\"http://code.fluxicon.com/xes/concept.xesext\"/>\r\n"
			+ "	<extension name=\"Organizational\" prefix=\"org\" uri=\"http://code.fluxicon.com/xes/org.xesext\"/>\r\n"
			+ "	<global scope=\"trace\">\r\n"
			+ "		<string key=\"concept:name\" value=\"UNKNOWN\"/>\r\n"
			+ "	</global>\r\n"
			+ "	<global scope=\"event\">\r\n"
			+ "		<string key=\"concept:name\" value=\"UNKNOWN\"/>\r\n"
			+ "		<string key=\"lifecycle:transition\" value=\"UNKNOWN\"/>\r\n"
			+ "		<string key=\"org:group\" value=\"UNKNOWN\"/>\r\n"
			+ "		<date key=\"time:timestamp\" value=\"1970-01-01T00:00:00.000+01:00\"/>\r\n"
			+ "	</global>\r\n"
			+ "	<classifier name=\"Activity classifier\" keys=\"concept:name lifecycle:transition\"/>\r\n"
			+ "	<string key=\"concept:name\" value=\"";
	private String header2 = "\">\r\n"
			+ "		<date key=\"Generated_On\" value=\"" + logTimeString + "\"/>\r\n"
			+ "		<string key=\"Generated_By\" value=\"Chuanyi Li\"/>\r\n"
			+ "	</string>\r\n";
	private String end = "</log>";

	public WriteCacheXES(String file_path, String logName) {
		f_path = file_path;
		try {
			fwinfo = new OutputStreamWriter(new FileOutputStream(f_path, true),
					"UTF-8");
		} catch (Exception e) {

		}
		Calendar rightNow = Calendar.getInstance();
		this.logTimeString = AlphaLogItem.makeTime(rightNow);
		this.logName = logName;
	}

	public void addHeader(){
		try {
			fwinfo.append(this.header1 + this.logName + this.header2);
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
