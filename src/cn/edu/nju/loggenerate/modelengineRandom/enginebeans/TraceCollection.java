package cn.edu.nju.loggenerate.modelengineRandom.enginebeans;

import java.util.ArrayList;
import java.util.List;

public class TraceCollection {

	private List<Trace> traces;
	
	public TraceCollection(){
		this.setTraces(new ArrayList<Trace>());
	}

	public List<Trace> getTraces() {
		return traces;
	}

	public void setTraces(List<Trace> traces) {
		this.traces = traces;
	}
	
	public void addTrace(Trace trace){
		this.traces.add(trace);
	}
	
	public void print(){
		for(Trace trace : traces){
			System.out.println(trace.getTrace());
		}
	}
}
