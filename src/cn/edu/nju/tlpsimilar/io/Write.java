package cn.edu.nju.tlpsimilar.io;

import cn.edu.nju.loggenerate.modelengine.enginebeans.TransitionList;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


public class Write {

	public static void print_hashMap(HashMap<List<Token>, String> hashMap){
		Iterator<Entry<List<Token>, String>> entryKeyIterator = hashMap.entrySet().iterator();
		System.out.println("ʵ����ĿΪ��" + hashMap.size());
	    while (entryKeyIterator.hasNext() ) {
	    	Entry<List<Token>, String> e = entryKeyIterator.next();
	        String value = e.getValue();
	        List<Token> key = e.getKey();
	        System.out.print("��ǰcase�� ");
	        for(int i=0; i<key.size(); i++){
	          	System.out.print(key.get(i).getProVisibleName() + key.get(i).getConVisibleName() + " ");
	        }
	        System.out.println();
	     }
	}

	
	public static void print_matrix(String[][] m){
		String s = "";
		for(int i=0; i<m.length; i++){
			for(int j=0; j<m[i].length; j++){
				if(i==0 || j==0){
					if(i==0&&j==0){
						s = String.format("%8s", "");
					}else{
						s = TransitionList.getByID(TransitionList.getIDByName(m[i][j])).getVisibleName()==null ? m[i][j].split("_")[0] : TransitionList.getByID(TransitionList.getIDByName(m[i][j])).getVisibleName();
						s = String.format("%8s", s);
					}
					System.out.print(s);
				}else{
					if(m[i][j] == ""){
						s = String.format("%8s", "");
						System.out.print(s);
					}
					else{
						s = String.format("%8s", m[i][j]);
						System.out.print(s);
					}
				}


			}
			System.out.println();
		}
	}
}
