/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengineRandom.flowbeans;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author LiChuanyi
 */
public class Transition {
    
    private String name;
    
    private List<Place> inputs;
    private List<Place> outputs;
    
    public boolean isLoop = false;
    private int currentTimes = 0;
    
    public Transition(){
        this.inputs = new LinkedList<Place>();
        this.outputs = new LinkedList<Place>();
    }
    
    public Transition(String name){
        this.inputs = new LinkedList<Place>();
        this.outputs = new LinkedList<Place>();
        
        this.name = name;
        if(name.contains("loop")){
        	this.isLoop = true;
        }
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Place> getInputs() {
        return inputs;
    }

    public void setInputs(List<Place> inputs) {
        this.inputs = inputs;
    }

    public List<Place> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Place> outputs) {
        this.outputs = outputs;
    }
    
    public void addInput(Place place){
        this.inputs.add(place);
    }
    
    public void addOutput(Place place){
        this.outputs.add(place);
    }
    
    public void resetLoopTimes(){
    	this.setCurrentTimes(0);
    }
    
    public void updateLoopTimes(){
    	this.setCurrentTimes(this.getCurrentTimes() + 1);
    }

	public int getCurrentTimes() {
		return currentTimes;
	}

	public void setCurrentTimes(int currentTimes) {
		this.currentTimes = currentTimes;
	}
}
