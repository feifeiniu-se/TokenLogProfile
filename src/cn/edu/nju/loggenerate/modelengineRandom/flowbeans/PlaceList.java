/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengineRandom.flowbeans;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LiChuanyi
 */
public class PlaceList {
    
    private Map<String, Place> places;
    
    public PlaceList(){
    	places = new HashMap<String, Place>();
    }
    
    public boolean addPlace(Place p){
        if(!places.containsKey(p.getName())){
        	places.put(p.getName(), p);
        	return true;
        }
        return false;
    }
    
    public boolean contains(String placeName){
    	if(this.places.containsKey(placeName)){
    		return true;
    	}
    	return false;
    }
    
    public Place getByName(String placeName){
    	return this.places.get(placeName);
    }
}
