/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengineRandom.enginebeans;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.modelengineRandom.flowbeans.Place;

/**
 * 
 * @author LiChuanyi
 */
public class PlacesWithToken {

	private List<Place> placeQueue;

	public PlacesWithToken() {
		setPlaceQueue(new ArrayList<Place>());
	}

	public List<Place> getPlaceQueue() {
		return placeQueue;
	}

	public void setPlaceQueue(List<Place> placeQueue) {
		this.placeQueue = placeQueue;
	}
	
	public void reSet(){
		this.placeQueue.clear();
	}
	
	public void addPlace(Place place){
		this.placeQueue.add(place);
	}
}
