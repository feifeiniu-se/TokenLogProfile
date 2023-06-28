/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengineRandom.flowbeans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LiChuanyi
 */
public class Place {
    
    private String name;
    
    private List<Transition> inputs;
    private List<Transition> outputs;
    
    public Place(){
        this.inputs = new ArrayList<Transition>();
        this.outputs = new ArrayList<Transition>();
    }
    
    public Place(String name){
        this.name = name;
        this.inputs = new ArrayList<Transition>();
        this.outputs = new ArrayList<Transition>();
    }
   
    public void handelTransitions(){
        
    }
    
    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Transition> getInputs() {
        return inputs;
    }

    public void setInputs(List<Transition> inputs) {
        this.inputs = inputs;
    }

    public List<Transition> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Transition> outputs) {
        this.outputs = outputs;
    }

    public boolean isIsLast() {
        if(this.outputs.size() > 0){
            return false;
        }else{
            return true;
        }
    }
    
    public boolean isIsFirst(){
    	if(this.inputs.size() > 0){
            return false;
        }else{
            return true;
        }
    }
    
    public void addInput(Transition task){
        this.inputs.add(task);
    }
    
    public void addOutput(Transition task){
        this.outputs.add(task);
    }
}
