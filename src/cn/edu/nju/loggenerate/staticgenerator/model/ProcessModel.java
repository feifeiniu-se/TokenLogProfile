package cn.edu.nju.loggenerate.staticgenerator.model;

import java.util.ArrayList;

public class ProcessModel {

	private PlaceList pList;
	private TransitionList tList;
	
	private Place startPlace;
	private Place endPlace;

	private boolean loopModelAdded = false;

	public boolean isLoopModelAdded(){
		return this.loopModelAdded ? true : false;
	}

	public void setLoopModelAdded(boolean added){
		loopModelAdded = added;
	}
	
	public ProcessModel(PlaceList plist, TransitionList tlist){
		this.pList = plist;
		this.tList = tlist;
	}
	
	public Place getPlaceByName(String name){
        return this.pList.getPlaceByName(name);
    }
	
	public Transition getTransitionByName(String name){
        return this.tList.getTransitionByName(name);
    }
	
	public PlaceList getPlaces(){
		return this.pList;
	}
	
	public TransitionList getTransitions(){
		return this.tList;
	}
	
	@Override
	public String toString() {
		StringBuffer placeStrbuffer = new StringBuffer();
		for( Place place : this.pList.getPlaces() ){
			placeStrbuffer.append(place.getName()+",");
		}
		StringBuffer transitionStrbuffer = new StringBuffer();
		for( Transition transition : this.tList.getTransitions() ){
			transitionStrbuffer.append(transition.getName()+",");
		}
		return placeStrbuffer.toString()+"\n"+transitionStrbuffer.toString()+"\n";
	}

	public Place getEndPlace() {
		return endPlace;
	}

	public void setEndPlace(Place endPlace) {
		this.endPlace = endPlace;
	}

	public Place getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(Place startPlace) {
		this.startPlace = startPlace;
	}
	
	public ProcessModel copyModel(){
		
		
		PlaceList newpList = new PlaceList();
		TransitionList newtList = new TransitionList();
		
		for( Place p : pList.getPlaces() ){
			Place newp = new Place(p.getName());
			newpList.addPlace(newp);
			
			//附加关系
			newp.setInputs(new ArrayList<String>(p.getInputs()));
			newp.setOutputs(new ArrayList<String>(p.getOutputs()));
		}
		
		for(Transition t : tList.getTransitions()){
			Transition newT = new Transition(t.getName());
			newtList.addTransition(newT);
			
			//附加关系
			newT.setOutputs(new ArrayList<String>(t.getOutputs()));
			newT.setInputs(new ArrayList<String>(t.getInputs()));
		}
		
		ProcessModel pm = new ProcessModel(newpList, newtList);
		if( this.getStartPlace() != null )
			pm.setStartPlace(pList.getPlaceByName(this.getStartPlace().getName()));
		
		if(this.getEndPlace() != null)
			pm.setEndPlace(pList.getPlaceByName(this.getEndPlace().getName()));
		return pm;
	}


}
