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
public class PlaceList {
    
    private static List<Place> placeList;
    private static int lastID = 0;
    
    public static void initiallize(){
        placeList = new ArrayList<Place>();
        lastID = 0;
    }
    
    public static boolean addPlace(Place p){
        for(Place ptmp : placeList){
            if(ptmp.getName().equals(p.getName())){
                return false;
            }
        }
        p.setPlaceID(lastID);
        placeList.add(p);
        lastID ++;
        return true;
    }
    
    public static Place getByID(int id){
        return placeList.get(id);
    }
    
    public static int getIDByName(String name){
        for(int i = 0; i < placeList.size(); i ++){
            if(placeList.get(i).getName().equals(name)){
                return i;
            }
        }
        return -1;
    }
    
    public static List<Place> getPlaces(){
        return placeList;
    }
    
    public static Place getStartPlace(){
    	for(Place p : placeList){
    		if(p.isIsFirst()){
    			return p;
    		}
    	}
    	return null;
    }
    
    public static Place getOverPlace(){
    	for(Place p : placeList){
    		if(p.isIsLast()){
    			return p;
    		}
    	}
    	return null;
    }
    
    public static void print(){
    	for(Place p : placeList){
    		System.out.print(p.getName()+":");
    		for(Integer tid : p.getInputs()){
    			Transition t = TransitionList.getByID(tid);
    			System.out.print(t.getName()+",");
    		}
    		System.out.print(":");
    		for(Integer tid : p.getOutputs()){
    			Transition t = TransitionList.getByID(tid);
    			System.out.print(t.getName()+",");
    		}
    		System.out.println();
    	}
    }
}
