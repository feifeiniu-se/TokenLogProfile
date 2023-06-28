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
public class TransitionList {
    
    private Map<String, Transition> transitions;
    
    public TransitionList(){
    	transitions = new HashMap<String, Transition>();
    }
    
    public boolean addTransition(Transition t){
        if(!transitions.containsKey(t.getName())){
        	transitions.put(t.getName(), t);
        	return true;
        }
        return false;
    }
    
    public Transition getByName(String taskName){
    	return this.transitions.get(taskName);
    }

	public void reSet() {
		// TODO Auto-generated method stub
		for(Transition trans : this.transitions.values()){
			if(trans.isLoop){
				trans.resetLoopTimes();
			}
		}
	}
}
