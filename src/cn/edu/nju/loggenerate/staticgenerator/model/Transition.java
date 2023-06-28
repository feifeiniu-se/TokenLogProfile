package cn.edu.nju.loggenerate.staticgenerator.model;

import java.util.ArrayList;
import java.util.List;

public class Transition {

    private String name;
    private String ID;
    
    private List<String> inputs;
    private List<String> outputs;
    
    public Transition(){
        this.inputs = new ArrayList<String>();
        this.outputs = new ArrayList<String>();
    }
    
    public Transition(String name){
        this.inputs = new ArrayList<String>();
        this.outputs = new ArrayList<String>();
        
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInputs() {
        return inputs;
    }

    public void setInputs(List<String> inputs) {
        this.inputs = inputs;
    }

    public List<String> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<String> outputs) {
        this.outputs = outputs;
    }
    
    public void addInput(String placeName){
        this.inputs.add(placeName);
    }
    
    public void addOutput(String placeName){
        this.outputs.add(placeName);
    }

    @Override
    public String toString(){
    	return inputs+"->"+name+"->"+outputs;
    }
    
	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if( !(o instanceof Transition) ) return false;
		final Transition other = (Transition)o;
		if( name.equals(other.getName()) ){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		int result;
		result = name == null?0:name.hashCode();
		return 29*result;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
}
