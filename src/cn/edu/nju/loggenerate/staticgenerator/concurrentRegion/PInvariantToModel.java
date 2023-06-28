package cn.edu.nju.loggenerate.staticgenerator.concurrentRegion;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.staticgenerator.model.Place;
import cn.edu.nju.loggenerate.staticgenerator.model.PlaceList;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;
import cn.edu.nju.loggenerate.staticgenerator.model.Transition;
import cn.edu.nju.loggenerate.staticgenerator.model.TransitionList;

/**
 * 根据P不变量活动，转化为ProcessModel
 * @author Administrator
 *
 */
public class PInvariantToModel {

	private ProcessModel processModel;
	
	public PInvariantToModel( ProcessModel processModel) {
		this.processModel = processModel;
	}
	
	
	public List<ProcessModel> convertToModel(List<List<String>> pInvariants, List<String> transitions){
		List<ProcessModel> models = new ArrayList<ProcessModel>();
		for( List<String> pNames : pInvariants ){
			Place place = processModel.getStartPlace();
			PlaceList placeList = new PlaceList();//新的库所集
			TransitionList transitionList = new TransitionList();//新的变迁集
			while( !place.isIsLast() ){
				List<String> inputTNames = place.getInputs();
				List<String> outputTNames = place.getOutputs();
				
				//组装Place
				Place newPlace = new Place(place.getName());
				for( String inputTName : inputTNames ){
					if( transitions.contains(inputTName) ){
						newPlace.addInput(inputTName);
						break;
					}
				}
				for( String outputTName : outputTNames ){
					if( transitions.contains(outputTName) ){
						newPlace.addOutput(outputTName);
						placeList.addPlace(newPlace);
						//组装Transition
						Transition newTransition = new Transition(outputTName);
						newTransition.addInput(place.getName());
						Transition t = processModel.getTransitionByName(outputTName);
						List<String> placeNames = t.getOutputs();
						for( String placeName : placeNames  ){
							if( pNames.contains(placeName)){
								newTransition.addOutput(placeName);
								place = processModel.getPlaceByName(placeName);
								transitionList.addTransition(newTransition);
								break;
							}
						}
						break;
					}
				}
			}
			//处理结束库所
			List<String> inputTNames = place.getInputs();
			//组装Place
			Place lastPlace = new Place(place.getName());
			for( String inputTName : inputTNames ){
				if( transitions.contains(inputTName) ){
					lastPlace.addInput(inputTName);
					break;
				}
			}
			lastPlace.getOutputs().add("-");
			placeList.addPlace(lastPlace);
			
			ProcessModel pModel = new ProcessModel(placeList, transitionList);
			models.add(pModel);
		}
		return models;
	}
	
	/**
	 * 根据P不变量活动，转化为ProcessModel
	 * @param pInvariants
	 * @return
	 */
	public List<ProcessModel> convertToModel(List<List<String>> pInvariants){
		List<ProcessModel> models = new ArrayList<ProcessModel>();
		for( List<String> tNames : pInvariants ){
			//创建新的placeList
			PlaceList placeList = new PlaceList();
			List<Place> places = processModel.getPlaces().getPlaces();
			String startPlaceName = processModel.getStartPlace().getName();
			String endPlaceName = processModel.getEndPlace().getName();
			for( Place place : places ){
				String placeName = place.getName();
				List<String> inputsTransition = place.getInputs();
				List<String> outputsTransition = place.getOutputs();
				//对于整个model的place，查看前驱和后继，是否有tName在其中
				for( String tName : tNames ){
					boolean inputFlag = inputsTransition.contains(tName);
					boolean outputFlag = outputsTransition.contains(tName);
					//如果存在，就是相关的place，创建新的place，添加其前驱，后继
					if( inputFlag || outputFlag ){
						Place newPlace = placeList.getPlaceByName(placeName);
						if( null == newPlace ){
							newPlace = new Place(placeName);
							placeList.addPlace(newPlace);
						}
							
						if( inputFlag )
							newPlace.addInput(tName);
						if( outputFlag )
							newPlace.addOutput(tName);
						
					}
				}
			}
			//剔除那些不连续的库所
			List<Place> removePlaces = new ArrayList<Place>();
			for( Place newPlace : placeList.getPlaces() ){
				String placeName = newPlace.getName();
				List<String> inputsTransition = newPlace.getInputs();
				List<String> outputsTransition = newPlace.getOutputs();
				if( inputsTransition.size() == 0 || outputsTransition.size() == 0 ) {
					if( !(placeName.equals(startPlaceName) || placeName.equals(endPlaceName)) ){
						removePlaces.add(newPlace);
					}
				}
			}
			for(Place removePlace : removePlaces){
				placeList.getPlaces().remove(removePlace);
			}
			
			//创建新的transitionList
			TransitionList transitionList = new TransitionList();
			for( String tName : tNames ){
				Transition t = processModel.getTransitionByName(tName);
				Transition newT = new Transition(t.getName());
				List<String> inputPlaces = t.getInputs();
				List<String> outputPlaces = t.getOutputs();
				for( Place place : placeList.getPlaces() ){
					String pName = place.getName();
					boolean inputFlag = inputPlaces.contains(pName);
					boolean outputFlag = outputPlaces.contains(pName);
					if( inputFlag )
						newT.addInput(pName);
					if( outputFlag )
						newT.addOutput(pName);
						
				}
				transitionList.addTransition(newT);
			}
			
			ProcessModel pModel = new ProcessModel(placeList, transitionList);
			models.add(pModel);
		}
		
		return models;
	}
	
}
