package cn.edu.nju.loggenerate.modelengineRandom.enginemain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(path, true), "UTF-8");
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
	
}
