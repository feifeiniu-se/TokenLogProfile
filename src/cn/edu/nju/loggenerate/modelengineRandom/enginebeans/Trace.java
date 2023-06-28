package cn.edu.nju.loggenerate.modelengineRandom.enginebeans;

import cn.edu.nju.loggenerate.modelengineRandom.flowbeans.Transition;

public class Trace {

	private String trace;
	
	public Trace(){
		this.setTrace("");
	}
	
	public void append(Transition t){
		this.setTrace(this.getTrace() + t.getName() + ",");
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}
}
