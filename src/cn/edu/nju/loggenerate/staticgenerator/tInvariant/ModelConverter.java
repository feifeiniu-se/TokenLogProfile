package cn.edu.nju.loggenerate.staticgenerator.tInvariant;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.staticgenerator.model.Place;
import cn.edu.nju.loggenerate.staticgenerator.model.PlaceList;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;
import cn.edu.nju.loggenerate.staticgenerator.model.Transition;
import cn.edu.nju.loggenerate.staticgenerator.model.TransitionList;

/**
 * 把经过不变量分解得出的变迁集合转化成ProcessModel
 * @author Administrator
 *
 */
public class ModelConverter {

	private ProcessModel processModel;
	
	public ModelConverter(ProcessModel processModel){
		this.processModel = processModel;
	}
	
	/**
	 * 转化为ProcessModel
	 * @param results 就是T不变量变迁集合
	 * @param transition 大循环变迁
	 * @param isLMST 是否是LMST
	 * @return
	 */
	public List<ProcessModel> convertModelsFromResult(List<List<String>> results, String transition, boolean isLMST){
		List<List<String>> tInvariants = new ArrayList<List<String>>();
		
		for( List<String> list : results ){
			boolean flag = list.contains(transition);
			if(isLMST&&flag){
				tInvariants.add(list);
			}
			if(!isLMST&&!flag){
				tInvariants.add(list);
			}
		}
		
		List<ProcessModel> models = this.generateModels(tInvariants);
		
		return models;
	}
	
	private List<ProcessModel> generateModels(List<List<String>> tInvariants){
		List<ProcessModel> models = new ArrayList<ProcessModel>();
		for( List<String> invariant : tInvariants ){
			PlaceList placeList = new PlaceList();
			TransitionList transitionList = new TransitionList();
			
			for( String tName : invariant ){
				Transition t = processModel.getTransitionByName(tName);
				transitionList.addTransition(t);
				this.addPlacesByTransition(placeList, t);
			}
			ProcessModel model = new ProcessModel(placeList, transitionList);
//			model.setStartPlace(processModel.getStartPlace());
//			model.setEndPlace(processModel.getEndPlace());
			models.add(model);
		}
		return models;
	}
	
	private void addPlacesByTransition(PlaceList placeList, Transition t){
		List<String> inputs = t.getInputs();
		List<String> outputs = t.getOutputs();
		for( String input : inputs ){
			Place place = this.processModel.getPlaceByName(input);
			placeList.addPlace(place);
		}
		for( String output : outputs ){
			Place place = this.processModel.getPlaceByName(output);
			placeList.addPlace(place);
		}
	}
	
}
