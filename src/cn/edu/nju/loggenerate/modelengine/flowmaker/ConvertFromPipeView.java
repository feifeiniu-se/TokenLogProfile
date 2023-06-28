//package cn.edu.nju.loggenerate.modelengine.flowmaker;
//
//import java.util.ArrayList;
//
//import cn.edu.nju.loggenerate.modelengine.enginebeans.PlaceList;
//import cn.edu.nju.loggenerate.modelengine.enginebeans.TransitionList;
//import cn.edu.nju.loggenerate.modelengine.flowbeans.Place;
//import cn.edu.nju.loggenerate.modelengine.flowbeans.Transition;
//
//import pipe.views.ArcView;
//import pipe.views.PetriNetView;
//import pipe.views.PlaceView;
//import pipe.views.TransitionView;
//
//public class ConvertFromPipeView {
//
//	public static void makePlaceList(PetriNetView pnmlData){
//		ArrayList<PlaceView> placeViews = pnmlData.getPlacesArrayList();
//		for( PlaceView placeView : placeViews ){
//			String placeName = placeView.getId();
//			Place place = new Place(placeName);
//            PlaceList.addPlace(place);
//		}
//	}
//
//	public static void makeTransitionList(PetriNetView pnmlData){
//		ArrayList<TransitionView> transitionViews =  pnmlData.getTransitionsArrayList();
//		for( TransitionView transitionView : transitionViews ){
//			String transitionName = transitionView.getId();
//			Transition transition = new Transition(transitionName);
//        	TransitionList.addTransition(transition);
//		}
//	}
//
//	public static void addRelations(PetriNetView pnmlData){
//		ArrayList<ArcView> arcViews = pnmlData.getArcsArrayList();
//		for( ArcView arcView : arcViews ){
//			String sourceInput = arcView.getSource().getId();
//			String targetInput = arcView.getTarget().getId();
//
//			if(PlaceList.getIDByName(sourceInput) != -1){
//				PlaceList.getByID(PlaceList.getIDByName(sourceInput)).addOutput(TransitionList.getIDByName(targetInput));
//				TransitionList.getByID(TransitionList.getIDByName(targetInput)).addInput(PlaceList.getIDByName(sourceInput));
//			}else{
//				PlaceList.getByID(PlaceList.getIDByName(targetInput)).addInput(TransitionList.getIDByName(sourceInput));
//				TransitionList.getByID(TransitionList.getIDByName(sourceInput)).addOutput(PlaceList.getIDByName(targetInput));
//			}
//		}
//	}
//}
