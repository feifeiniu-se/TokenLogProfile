package cn.edu.nju.loggenerate.modelengineRandom.enginemain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.edu.nju.loggenerate.modelengineRandom.flowbeans.Transition;

public class RandomLCY {

	public static Transition getRandomTransition(List<Transition> transitions){
		Random r = new Random();
		Transition choosed = null;
		
		if(transitions.size() == 1){
			int result = r.nextInt(2);
			if(result == 1){
				choosed = transitions.get(0);
			}else{
				return null;
			}
		}
		
		if(transitions.size() == 0){
			return null;
		}
		
        int index = r.nextInt(transitions.size());
        choosed = transitions.get(index);
        
        return choosed;
	}
	
	public static Transition getRandomTransitionRun(List<Transition> transitions){
		List<Transition> randomList = new ArrayList<Transition>();
		for(Transition transTmp : transitions){
			if(transTmp.isLoop){
				if(transTmp.getCurrentTimes() < EngineInstance.loopMaxTimes){
					randomList.add(transTmp);
				}
			}else{
				randomList.add(transTmp);
			}
		}
		
		Random r = new Random();
		Transition choosed = null;
		
		if(randomList.size() == 1){
			choosed = randomList.get(0);
		}
		
		if(randomList.size() == 0){
			return null;
		}
		
        int index = r.nextInt(randomList.size());
        choosed = randomList.get(index);
        
        return choosed;
	}
}
