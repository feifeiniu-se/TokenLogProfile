/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.flowbeans;

import cn.edu.nju.loggenerate.modelengine.enginebeans.OutTimeTokens;
import cn.edu.nju.loggenerate.modelengine.enginebeans.PlaceList;
import cn.edu.nju.loggenerate.modelengine.enginebeans.PlaceWaitingQueue;
import cn.edu.nju.loggenerate.modelengine.enginemain.EngineCase;
import cn.edu.nju.loggenerate.modelengine.enginemain.EngineTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author LiChuanyi
 */
public class Transition {
    
    private int transitionID;
    private String name;

    public String getVisibleName() {
        return visibleName;
    }

    public void setVisibleName(String visibleName) {
        this.visibleName = visibleName;
    }

    private String visibleName;
    
    private List<Integer> inputs;
    private List<Integer> outputs;
    
    private boolean executed = false;
    
    public Transition(){
        this.inputs = new ArrayList<Integer>();
        this.outputs = new ArrayList<Integer>();
    }
    
    public Transition(String name){
        this.inputs = new ArrayList<Integer>();
        this.outputs = new ArrayList<Integer>();
        
        this.name = name;
    }
    
    public boolean isReady(){
        boolean isready = true;
        for(Integer p : inputs){
            if(!PlaceList.getByID(p).hasToken()){
                isready = false;
                break;
            }
        }
        return isready;
    }
    
    public boolean excute(){
        int engineTime = EngineTime.getEngineTime();
        int caseid = EngineCase.getEngineCase();
        for(int p : inputs){
            Token consumedOne = PlaceList.getByID(p).consumeOne();
            consumedOne.setConsumeTime(engineTime);
            consumedOne.setConsumer(this.transitionID);
            OutTimeTokens.addToken(consumedOne);
        }
        List<Integer> outputTmp = new ArrayList<Integer>();
        for(Integer outID : outputs){
        	outputTmp.add(outID);
            Token t = new Token();
            t.setProducer(this.transitionID);
            t.setProduceTime(engineTime);
            t.setCaseid(caseid);
            PlaceList.getByID(outID).receiveOne(t);
        }
        
        while(outputTmp.size() > 0){
        	Random r = new Random();
        	int index = r.nextInt(outputTmp.size());
        	PlaceWaitingQueue.addPlaceToQueue(outputTmp.get(index));
        	outputTmp.remove(index);
        }
        
        this.executed = true;
        EngineTime.timeGoOn();
        return true;
    }

    public int getTransitionID() {
        return transitionID;
    }

    public void setTransitionID(int transitionID) {
        this.transitionID = transitionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
    public void addInput(int placeid){
        this.inputs.add(placeid);
    }
    
    public void addOutput(int placeid){
        this.outputs.add(placeid);
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}
