package cn.edu.nju.loggenerate.modelengineRandom.enginebeans;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.modelengineRandom.flowbeans.Transition;

public class ParaTransitions {
	
	private List<Transition> paraTransitions;
	
	public ParaTransitions(){
		this.setParaTransitions(new ArrayList<Transition>());
	}

	public List<Transition> getParaTransitions() {
		return paraTransitions;
	}

	public void setParaTransitions(List<Transition> paraTransitions) {
		this.paraTransitions = paraTransitions;
	}
	
	public void reSet(){
		this.paraTransitions.clear();
	}
}
