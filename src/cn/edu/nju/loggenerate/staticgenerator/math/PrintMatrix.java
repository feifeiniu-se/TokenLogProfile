package cn.edu.nju.loggenerate.staticgenerator.math;

import java.util.List;

import cn.edu.nju.loggenerate.staticgenerator.model.Place;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;
import cn.edu.nju.loggenerate.staticgenerator.model.Transition;

public class PrintMatrix {

	public static void printRelationMatrix(int[][] matrix, ProcessModel pm){
		List<Place> places = pm.getPlaces().getPlaces();
		List<Transition> transtionsList = pm.getTransitions().getTransitions();
		
		int pNumber = places.size();
		int tNumber = transtionsList.size();
		
		System.out.println("***************************************************");
		
		System.out.print("      ");
		for(int j = 0; j < tNumber; j ++){
			System.out.print(transtionsList.get(j).getName() + "    ");
		}
		System.out.println();
		
		for(int i = 0; i < pNumber; i ++){
			System.out.print(places.get(i).getName() + "    ");
			for(int j = 0; j < tNumber; j ++){
				System.out.print(matrix[i][j]);
				if(matrix[i][j] == -1){
					System.out.print("    ");
				}else{
					System.out.print("    ");
				}
			}
			System.out.println();
		}
		System.out.println("***************************************************");
	}
	
	public static void printTMatrix(int[][] matix, ProcessModel pm){
		
	}
	
	public static void printPMatrix(int[][] matix, ProcessModel pm){
		
	}
	
	public static void printListListList(List<List<List<String>>> rtn){
		for(List<List<String>> list1 : rtn){
			System.out.println("This One:");
			for(List<String> list2 : list1){
				for(String item : list2){
					System.out.print(item + " ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
}
