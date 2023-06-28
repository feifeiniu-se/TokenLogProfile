/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.flowbeans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LiChuanyi
 */
public class Place {
    
    private int placeID;
    private String name;
    
    private List<Integer> inputs;
    private List<Integer> outputs;
    
    private List<Token> tokens;
    
    public Place(){
        this.inputs = new ArrayList<Integer>();
        this.outputs = new ArrayList<Integer>();
        this.tokens = new ArrayList<Token>();
    }
    
    public Place(String name){
        this.placeID = -1;
        this.name = name;
        this.inputs = new ArrayList<Integer>();
        this.outputs = new ArrayList<Integer>();
        this.tokens = new ArrayList<Token>();
    }
    
    public boolean hasToken(){
        return this.tokens.size() > 0 ? true : false;
    }
    
    public Token consumeOne(){
        Token consumed = this.tokens.remove(0);
        return consumed;
    }
    
    public void receiveOne(Token t){
        this.tokens.add(t);
    }
    
    public void handelTransitions(){
        
    }
    
    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public List<Integer> getInputs() {
        return inputs;
    }

    public void setInputs(List<Integer> inputs) {
        this.inputs = inputs;
    }

    public List<Integer> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Integer> outputs) {
        this.outputs = outputs;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
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
    
    public void addInput(Integer taskID){
        this.inputs.add(taskID);
    }
    
    public void addOutput(Integer taskID){
        this.outputs.add(taskID);
    }
}
