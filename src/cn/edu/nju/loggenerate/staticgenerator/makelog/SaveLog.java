package cn.edu.nju.loggenerate.staticgenerator.makelog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import cn.edu.nju.loggenerate.staticgenerator.model.TransitionList;

public class SaveLog {

	public static void saveLog(String content, String path){
		File file = new File(path);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "utf-8"));
			writer.append(content);
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void saveLog(List<List<String>> traces, File outputFile, TransitionList tList){
		try {
			outputFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFile), "utf-8"));
			for(List<String> trace : traces) {
				for(String name : trace){
					writer.append(tList.getTransitionByName(name).getID() +",");
				}
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
