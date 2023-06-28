/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.enginebeans;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Place;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Transition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LiChuanyi
 */
public class TransitionList {
    
    private static List<Transition> transitionList;
    private static int lastID;
    
    public static void initiallize(){
        transitionList = new ArrayList<Transition>();
        lastID = 0;
    }
    
    public static boolean addTransition(Transition t){
        for(Transition ttmp : transitionList){
            if(ttmp.getName().equals(t.getName())){
                return false;
            }
        }
        t.setTransitionID(lastID);
        transitionList.add(t);
        lastID ++;
        return true;
    }
    
    public static Transition getByID(int id){
        if(id == -2){
            return new Transition(" ");
        }
        if(id == -1){
            return new Transition(" ");
        }
        return transitionList.get(id);
    }
    
    public static int getIDByName(String name){
        for(int i = 0; i < transitionList.size(); i ++){
            if(transitionList.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    
    public static List<Transition> getTransitions(){
        return transitionList;
    }
    
    public static boolean hasUnExecuted(){
        for(Transition t : transitionList){
            if(!t.isExecuted()){
                return true;
            }
        }
        return false;
    }
    
    public static void print(){
    	for(Transition t : transitionList){
    		System.out.print(t.getName()+":");
    		for(Integer pid : t.getInputs()){
    			Place p = PlaceList.getByID(pid);
    			System.out.print(p.getName()+",");
    		}
    		System.out.print(":");
    		for(Integer pid : t.getOutputs()){
    			Place p = PlaceList.getByID(pid);
    			System.out.print(p.getName()+",");
    		}
    		System.out.println();
    	}
    }
    
}
