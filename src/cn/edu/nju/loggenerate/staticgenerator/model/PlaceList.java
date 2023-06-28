package cn.edu.nju.loggenerate.staticgenerator.model;

import java.util.ArrayList;
import java.util.List;

public class PlaceList {

	private List<Place> placeList;
    
    public PlaceList(){
        placeList = new ArrayList<Place>();
    }
    
    public boolean addPlace(Place p){
        for(Place ptmp : placeList){
            if(ptmp.getName().equals(p.getName())){
                return false;
            }
        }
        placeList.add(p);
        return true;
    }
    
    public Place getPlaceByName(String name){
        for(int i = 0; i < placeList.size(); i ++){
            if(placeList.get(i).getName().equals(name)){
                return placeList.get(i);
            }
        }
        return null;
    }
    
    public Place getPlaceByID(String id){
        for(int i = 0; i < placeList.size(); i ++){
            if(placeList.get(i).getID().equals(id)){
                return placeList.get(i);
            }
        }
        return null;
    }
    
    public List<Place> getPlaces(){
        return placeList;
    }
	
}
