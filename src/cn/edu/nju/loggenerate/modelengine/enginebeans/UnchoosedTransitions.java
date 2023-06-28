/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.enginebeans;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Transition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author LiChuanyi
 */
public class UnchoosedTransitions {

    private static Map<Integer, List<Integer>> transitions;

    public static void initiallize() {
        transitions = new HashMap<Integer, List<Integer>>();
    }

    public static Transition getOneUnchoosed(Integer placeID) {
        List<Integer> transitionList = transitions.get(placeID);

        List<Integer> ready = new ArrayList<Integer>();
        if (transitionList.size() > 0) {
            for(Integer tID : transitionList){
                if(TransitionList.getByID(tID).isReady()){
                    ready.add(tID);
                }
            }
            if(ready.size() > 0){
                Random r = new Random();
                int id = r.nextInt(ready.size());
                transitions.get(placeID).remove(ready.get(id));
                return TransitionList.getByID(ready.get(id));
            }else{
                return null;
            }
        } else {
            return null;
        }
    }
    
    public static Transition getOne(Integer placeID) {
        List<Integer> transitionList = PlaceList.getByID(placeID).getOutputs();

        if (transitionList.size() > 1) {
            Random r = new Random();
            int id = r.nextInt(transitionList.size());
            return TransitionList.getByID(transitionList.get(id));
        } else {
            return TransitionList.getByID(transitionList.get(0));
        }
    }

    public static boolean addListToMap(Integer placeID, List<Integer> unchoosedList) {
        if (transitions.get(placeID) == null) {
            transitions.put(placeID, new ArrayList<Integer>());
        }
        for (Integer unchoosed : unchoosedList) {
            transitions.get(placeID).add(unchoosed);
        }
        return true;
    }
    
    public static String checkIfHasUnchoosed(Integer placeID){
        if(transitions.get(placeID) == null){
            return "notinlist";
        }else{
            if(transitions.get(placeID).size() < 1){
                return "nounchoosed";
            }else{
                return "has";
            }
        }
    }
    
    public static void print(){
    	for(Integer l : transitions.keySet()){
    		System.out.print(PlaceList.getByID(l).getName()+"-");
    		for(Integer i : transitions.get(l)){
    			System.out.print(TransitionList.getByID(i).getName()+",");
    		}
    		System.out.println();
    	}
    }
    
}
