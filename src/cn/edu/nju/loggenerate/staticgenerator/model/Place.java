package cn.edu.nju.loggenerate.staticgenerator.model;

import java.util.ArrayList;
import java.util.List;

public class Place {

	private String name;
	private String ID;
    
    private List<String> inputs;
    private List<String> outputs;
    
    public Place(){
        this.inputs = new ArrayList<String>();
        this.outputs = new ArrayList<String>();
    }
    
    public Place(String name){
        this.name = name;
        this.inputs = new ArrayList<String>();
        this.outputs = new ArrayList<String>();
    }
    
    public void handelTransitions(){
        
    }
    
    public String getName(){
        return this.name;
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

    public boolean isIsLast() {
        if(this.outputs.size() > 1){
            return false;
        }else {
        	if(this.outputs.size() == 1){
        		if(this.outputs.get(0).equals("-")){
        			return true;
        		}else{
        			return false;
        		}
        	}
            return true;
        }
    }
    
    public void addInput(String taskName){
        this.inputs.add(taskName);
    }
    
    public void addOutput(String taskName){
        this.outputs.add(taskName);
    }
    
	@Override
    public String toString(){
    	return inputs+"->"+name+"->"+outputs;
    }
    
	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if( !(o instanceof Place) ) return false;
		final Place other = (Place)o;
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
