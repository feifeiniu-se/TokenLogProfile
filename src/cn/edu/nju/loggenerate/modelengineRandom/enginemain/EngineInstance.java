/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengineRandom.enginemain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import cn.edu.nju.loggenerate.modelengineRandom.enginebeans.ParaTransitions;
import cn.edu.nju.loggenerate.modelengineRandom.enginebeans.PlacesWithToken;
import cn.edu.nju.loggenerate.modelengineRandom.enginebeans.Trace;
import cn.edu.nju.loggenerate.modelengineRandom.enginebeans.TraceCollection;
import cn.edu.nju.loggenerate.modelengineRandom.flowbeans.Place;
import cn.edu.nju.loggenerate.modelengineRandom.flowbeans.PlaceList;
import cn.edu.nju.loggenerate.modelengineRandom.flowbeans.Transition;
import cn.edu.nju.loggenerate.modelengineRandom.flowbeans.TransitionList;

/**
 * 
 * @author LiChuanyi
 */
public class EngineInstance {

	public int TIMES = 0;
	public static int loopMaxTimes = 0;
	private PlaceList placeList;
	private TransitionList transitionList;

	private PlacesWithToken placeWT;
	private ParaTransitions paraTran;

	private TraceCollection traceCol;

	public EngineInstance(int times) {
		this.setPlaceList(new PlaceList());
		this.setTransitionList(new TransitionList());

		this.setPlaceWT(new PlacesWithToken());
		this.setParaTran(new ParaTransitions());

		this.setTraceCol(new TraceCollection());

		this.TIMES = times;
	}

	public void startEngine(String path) {
		while (this.TIMES-- > 0) {
			this.transitionList.reSet();
			this.placeWT.reSet();
			this.paraTran.reSet();
			this.placeWT.addPlace(this.placeList.getByName("i"));
			//this.traceCol.addTrace(this.run());
			SaveLog.saveLog(this.TIMES + ":" + this.run().getTrace() + "\r\n", path);
		}
	}

	private Trace run() {
		Trace trace = new Trace();
		while (this.placeWT.getPlaceQueue().size() > 0) {
			Transition toBeExecuted = null;
			List<Transition> tmpReadyTransitions = new ArrayList<Transition>();
			
			Set<Place> usedPlaces = new HashSet<Place>();
			Set<Place> leftPlaces = new HashSet<Place>();
			leftPlaces.addAll(this.placeWT.getPlaceQueue());
			
			for (Place p : this.placeWT.getPlaceQueue()) {
				if(usedPlaces.contains(p)){
					continue;
				}
				Transition tmpChoosed = null;
				if (p.getOutputs().size() > 1) {
					List<Transition> readyOutputs = new ArrayList<Transition>();
					List<Transition> outPuts = p.getOutputs();
					for (Transition outPut : outPuts) {
						if (leftPlaces.containsAll(outPut.getInputs())) {
							readyOutputs.add(outPut);
						}
					}
					tmpChoosed = RandomLCY.getRandomTransitionRun(readyOutputs);
				} else if(p.getOutputs().size() == 1){
					tmpChoosed = p.getOutputs().get(0);
					if(!leftPlaces.containsAll(tmpChoosed.getInputs())){
						tmpChoosed = null;
					}
				}else{
					return trace;
				}
				
				if(tmpChoosed != null){
					if(tmpChoosed.isLoop){
						tmpChoosed.updateLoopTimes();
			        }
					usedPlaces.addAll(tmpChoosed.getInputs());
					leftPlaces.removeAll(tmpChoosed.getInputs());
					tmpReadyTransitions.add(tmpChoosed);
				}
			}
			this.placeWT.getPlaceQueue().removeAll(usedPlaces);
			
			if(tmpReadyTransitions.size() > 1 || this.paraTran.getParaTransitions().size() > 0){
				this.paraTran.getParaTransitions().addAll(tmpReadyTransitions);
				while(toBeExecuted == null){
					toBeExecuted = RandomLCY.getRandomTransition(this.paraTran.getParaTransitions());
				}
				this.paraTran.getParaTransitions().remove(toBeExecuted);
			}else if (tmpReadyTransitions.size() == 1){
				toBeExecuted = tmpReadyTransitions.get(0);
			}else{
				return trace;
			}
			trace.append(toBeExecuted);
			this.placeWT.getPlaceQueue().addAll(toBeExecuted.getOutputs());
		}
		
		return null;
	}

	public PlaceList getPlaceList() {
		return placeList;
	}

	public void setPlaceList(PlaceList placeList) {
		this.placeList = placeList;
	}

	public TransitionList getTransitionList() {
		return transitionList;
	}

	public void setTransitionList(TransitionList transitionList) {
		this.transitionList = transitionList;
	}

	public PlacesWithToken getPlaceWT() {
		return placeWT;
	}

	public void setPlaceWT(PlacesWithToken placeWT) {
		this.placeWT = placeWT;
	}

	public ParaTransitions getParaTran() {
		return paraTran;
	}

	public void setParaTran(ParaTransitions paraTran) {
		this.paraTran = paraTran;
	}

	public TraceCollection getTraceCol() {
		return traceCol;
	}

	public void setTraceCol(TraceCollection traceCol) {
		this.traceCol = traceCol;
	}
}
