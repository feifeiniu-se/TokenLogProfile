package cn.edu.nju.loggenerate.modelengine.enginebeans;

import cn.edu.nju.loggenerate.modelengine.enginemain.EngineInstance;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;
import org.processmining.framework.models.petrinet.PNEdge;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Transition;

import java.util.ArrayList;
import java.util.List;

public class PetriLCY {

    private PetriNet net;

    public PetriLCY(PetriNet pn){
        this.net = pn;
    }

    public PetriNet getNet(){
        return this.net;
    }

    public static void makePlaceList(PetriNet petriNet){
        ArrayList<Place> places = petriNet.getPlaces();
        for( Place place : places ){
            String placeName = place.getName();
            cn.edu.nju.loggenerate.modelengine.flowbeans.Place placeLCY = new cn.edu.nju.loggenerate.modelengine.flowbeans.Place(placeName);
            PlaceList.addPlace(placeLCY);
        }
    }

    public static void makeTransitionList(PetriNet petriNet){
        ArrayList<Transition> transitions =  petriNet.getTransitions();
        for( Transition transition : transitions ){
            String transitionName = transition.getName();
            String visibleName = transition.getIdentifier();
            cn.edu.nju.loggenerate.modelengine.flowbeans.Transition transitionLCY = new cn.edu.nju.loggenerate.modelengine.flowbeans.Transition(transitionName);
            transitionLCY.setVisibleName(visibleName);
            TransitionList.addTransition(transitionLCY);
        }
    }

    public static void addRelations(PetriNet petriNet){
        ArrayList<PNEdge> arcs = petriNet.getEdges();
        for( PNEdge arc : arcs ){
            if(arc.isPT()){
                PlaceList.getByID(PlaceList.getIDByName(arc.getTail().getName())).addOutput(TransitionList.getIDByName(arc.getHead().getName()));
                TransitionList.getByID(TransitionList.getIDByName(arc.getHead().getName())).addInput(PlaceList.getIDByName(arc.getTail().getName()));
            }
            if(arc.isTP()){
                PlaceList.getByID(PlaceList.getIDByName(arc.getHead().getName())).addInput(TransitionList.getIDByName( arc.getTail().getName()));
                TransitionList.getByID(TransitionList.getIDByName(arc.getTail().getName())).addOutput(PlaceList.getIDByName( arc.getHead().getName()));
            }
        }
    }

    public static List<Token> getTokensOfPetriNet(PetriNet pn){
        EngineInstance.initiallizeEngine();
        PetriLCY.makePlaceList(pn);
        PetriLCY.makeTransitionList(pn);
        PetriLCY.addRelations(pn);
        EngineInstance.startEngine();
        return OutTimeTokens.getAll();
    }

    public static String getTokenStringOfPetriNet(){
        return OutTimeTokens.getLog();
    }
}
