package cn.edu.nju.loggenerate.staticgenerator.model;

import java.util.ArrayList;
import java.util.List;

public class TransitionList {

	private List<Transition> transitionList;
    
    public TransitionList(){
        transitionList = new ArrayList<Transition>();
    }
    
    public boolean addTransition(Transition t){
        for(Transition ttmp : transitionList){
            if(ttmp.getName().equals(t.getName())){
                return false;
            }
        }
        transitionList.add(t);
        return true;
    }
    
    public Transition getTransitionByName(String name){
        for(int i = 0; i < transitionList.size(); i ++){
            if(transitionList.get(i).getName().equals(name)){
                return transitionList.get(i);
            }
        }
        return null;
    }
    
    public List<Transition> getTransitions(){
        return transitionList;
    }

	public Transition getTransitionByID(String arcSource) {
		// TODO Auto-generated method stub
		for(int i = 0; i < transitionList.size(); i ++){
            if(transitionList.get(i).getID().equals(arcSource)){
                return transitionList.get(i);
            }
        }
        return null;
	}
	
}
