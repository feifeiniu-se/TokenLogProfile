/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengineRandom.getlogs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cn.edu.nju.loggenerate.modelengineRandom.logbeans.AlphaLogItem;

/**
 * 
 * @author lichuanyi
 */
public class AlphaLogs {

	public static void main(String[] args) {
		String filePath = "C:\\Users\\Chuanyi Li\\Documents\\学术研究\\parallel process  mining\\20190123实验数据\\";
		File dir = new File(filePath);
		for(File file : dir.listFiles()){
			if(file.getName().contains("trace")){
				String fileName = file.getName().split("\\.")[0];
				fromTraceToXES(filePath + fileName + ".trace", filePath + fileName + ".xes", fileName);
			}
		}
	}
	
	public static void fromTraceToXML(String traceFile, String xmlFile){
		try {
			BufferedReader traceFileReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(traceFile),
							"utf8"));
			WriteCache writeXML = new WriteCache(xmlFile);
			writeXML.addHeader();

			String trace = "";
			while ((trace = traceFileReader.readLine()) != null) {
				writeXML.addItem(AlphaLogItem.getProcessInstance(trace));
			}
			writeXML.addEnd();

			traceFileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void fromTraceToXES(String traceFile, String xesFile, String fileName){
		try {
			BufferedReader traceFileReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(traceFile),
							"utf8"));
			WriteCacheXES writeXES = new WriteCacheXES(xesFile, fileName);
			writeXES.addHeader();

			String trace = "";
			while ((trace = traceFileReader.readLine()) != null) {
				writeXES.addItem(AlphaLogItem.getProcessInstanceXES(trace));
			}
			writeXES.addEnd();

			traceFileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void fromXMLToTrace(String xmlFile, String traceFile){
		try {
			BufferedReader xmlFileReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(xmlFile),
							"utf8"));
			BufferedWriter traceFileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(traceFile), "utf8"));
			
			String line = "";
			while((line = xmlFileReader.readLine()) != null){
				if(!line.contains("<Process id=")){
					continue;
				}else{
					break;
				}
			}
			
			String tmpTrace = "";
			String caseID = "";
			while((line = xmlFileReader.readLine()) != null){
				if(line.contains("<ProcessInstance id=")){
					caseID = line.split("\"")[1];
				}else{
					break;
				}
				while(!(line = xmlFileReader.readLine()).contains("</ProcessInstance>")){
					if(!line.contains("<WorkflowModelElement>")){
						continue;
					}else{
						tmpTrace += line.split("WorkflowModelElement>")[1].split("<")[0] + ",";
					}
				}
				traceFileWriter.append(caseID + "\t" + tmpTrace);
				traceFileWriter.newLine();
				traceFileWriter.flush();
				
				tmpTrace = "";
				caseID = "";
			}
			
			xmlFileReader.close();
			traceFileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
