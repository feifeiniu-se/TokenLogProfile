package cn.edu.nju.tlpsimilar.constructMatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Matrix {

	//将任务发生关系转化为Matrix
	public static String[][] getMatrix(HashMap<HashMap<String, String>, String> relations){
		Set<String> task = getTask(relations);
		String[][] m = new String[task.size()+1][task.size()+1];
		int index = 1;

		for(int i=0; i<m.length; i++){
			for(int j=0; j<m[i].length; j++){
				m[i][j]="";
			}
		}//初始化矩阵,必须得初始化，必然contain函数不可以用
		for(String s:task){
			m[index][0] = s;
			m[0][index] = s;
			index++;
		}
		for(int i=1; i<m.length; i++){
			for(int j=1; j<m[i].length; j++){
				HashMap<String, String> hsIndex = new HashMap<String, String>();
				hsIndex.put(m[i][0], m[j][0]);
				if(relations.containsKey(hsIndex)){
					m[i][j]=relations.get(hsIndex);
				}
			}
		}

		return m;
	}
	
	
	public static Set<String> getTask(HashMap<HashMap<String, String>, String> relations){
		Set<String> a = new TreeSet<>();
		for(Map.Entry<HashMap<String, String>, String> entry : relations.entrySet()){
			HashMap<String, String> key = entry.getKey();
			a.addAll(key.keySet());
			a.addAll(key.values());
		}
		return a;
	}
}
