package cn.edu.nju.loggenerate.modelengine.logbeans;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Transition;

public class PostLogBean {

	public Transition current;
    public List<Transition> posts;
    private int times;
    public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int enginetime;
    
    public boolean handled;
    
    public PostLogBean(){
    	
    }
    
    public PostLogBean(Transition cur, List<Transition> pos, int time, int enginet){
    	this.current = cur;
    	this.posts = pos;
    	this.times = time;
    	this.enginetime = enginet;
    }
    
    public PostLogBean(Transition cur, int enginet){
    	this.posts = new ArrayList<Transition>();
    	this.current = cur;
    	this.enginetime = enginet;
    	this.times = 1;
    	this.handled = false;
    }
    
    public boolean hasSameTime(PostLogBean one){
    	if(one.enginetime == this.enginetime){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public void addPost(Transition t){
    	this.posts.add(t);
    }
	
    public void addInstance(){
    	this.times ++;
    }
    
    public boolean equals(PostLogBean one){
    	for(Transition t : one.posts){
    		if(!this.posts.contains(t)){
    			return false;
    		}
    	}
    	for(Transition t : this.posts){
    		if(!one.posts.contains(t)){
    			return false;
    		}
    	}
    	return true;
    }
    
    public void print(){
    	System.out.print(this.current.getName() + " ");
    	for(Transition t : this.posts){
    		System.out.print(t.getName() + ",");
    	}
    	System.out.print(" " + this.times);
    }
    
    public String saveToFile(){
    	String logItem = "";
    	logItem = this.current.getName() + " ";
    	for(Transition t : this.posts){
    		logItem = logItem + t.getName() + ",";
    	}
    	logItem = logItem + " " + this.times;
    	return logItem;
    }
}
