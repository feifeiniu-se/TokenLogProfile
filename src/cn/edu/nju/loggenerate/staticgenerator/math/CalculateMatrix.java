package cn.edu.nju.loggenerate.staticgenerator.math;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.staticgenerator.model.Place;
import cn.edu.nju.loggenerate.staticgenerator.model.PlaceList;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;
import cn.edu.nju.loggenerate.staticgenerator.model.Transition;
import cn.edu.nju.loggenerate.staticgenerator.model.TransitionList;

public class CalculateMatrix {

	public static int[][] calcMatrix(ProcessModel pm) {
		int pNumber = pm.getPlaces().getPlaces().size();
		int tNumber = pm.getTransitions().getTransitions().size();
		int[][] matrix = new int[pNumber][tNumber];
		for (int i = 0; i < pNumber; i++) {
			for (int j = 0; j < tNumber; j++) {
				if (pm.getPlaces()
						.getPlaces()
						.get(i)
						.getInputs()
						.contains(
								pm.getTransitions().getTransitions().get(j)
										.getName())) {
					matrix[i][j] = 1;
					continue;
				}
				if (pm.getPlaces()
						.getPlaces()
						.get(i)
						.getOutputs()
						.contains(
								pm.getTransitions().getTransitions().get(j)
										.getName())) {
					matrix[i][j] = -1;
					continue;
				}
				matrix[i][j] = 0;
			}
		}
		return matrix;
	}

	public static List<ProcessModel> getModelsByIntArray(
			ArrayList<int[]> trans, ProcessModel pm) {
		List<ProcessModel> pms = new ArrayList<ProcessModel>();

		for (int[] tmpOne : trans) {
			TransitionList tsOne = new TransitionList();
			PlaceList psOne = new PlaceList();
			//System.out.println();
			for (int i = 0; i < tmpOne.length; i++) {
				//System.out.print(tmpOne[i]);
				if (tmpOne[i] == 1) {
					Transition tTmp = pm.getTransitions().getTransitions()
							.get(i);
					//if (!tTmp.getName().equals("-")) {
						tsOne.addTransition(tTmp);
						for (String pName : tTmp.getInputs()) {
							psOne.addPlace(pm.getPlaceByName(pName));
						}
						for (String pName : tTmp.getOutputs()) {
							psOne.addPlace(pm.getPlaceByName(pName));
						}
					//}
				}
			}

			ProcessModel pmOne = new ProcessModel(psOne, tsOne);
			pms.add(pmOne);
		}

		return pms;
	}

	public static List<List<String>> getTasksByIntArray(
			ArrayList<int[]> plas, ProcessModel pm, ProcessModel totalPM) {
		List<List<String>> ps = new ArrayList<List<String>>();

		for (int[] ptmpOne : plas) {
			List<String> psOne = new ArrayList<String>();
			
			List<String> tsOne = new ArrayList<String>();//Here could get the list of transition names
			
			for (int i = 0; i < ptmpOne.length; i++) {
				//System.out.print(ptmpOne[i]);
				if (ptmpOne[i] == 1) {
					psOne.add(pm.getPlaces().getPlaces().get(i).getName());
					
					Place pTmp = pm.getPlaces().getPlaces().get(i);
					for (String tName : pTmp.getInputs()) {
						if(pm.getTransitions().getTransitions().contains(totalPM.getTransitionByName(tName))){
							tsOne.add(tName);
						}
					}
//					for (String tName : pTmp.getOutputs()) {
//						if(pm.getTransitions().getTransitions().contains(totalPM.getTransitionByName(tName))){
//							tsOne.add(tName);
//						}
//					}
				}
			}
//			System.out.println();
			//ps.add(psOne);
			ps.add(tsOne);
		}
		return ps;
	}
}
