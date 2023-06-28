package cn.edu.nju.loggenerate.staticgenerator.math;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.processmining.framework.models.petrinet.PNEdge;
import org.processmining.framework.models.petrinet.PetriNet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cn.edu.nju.loggenerate.staticgenerator.model.Place;
import cn.edu.nju.loggenerate.staticgenerator.model.PlaceList;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;
import cn.edu.nju.loggenerate.staticgenerator.model.Transition;
import cn.edu.nju.loggenerate.staticgenerator.model.TransitionList;

public class LoadModelFromPNML {

	static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

	public static ProcessModel loadFromPetriNet(PetriNet petriNet) {
		PlaceList modelPlaceList = new PlaceList();
		TransitionList modelTransitionList = new TransitionList();

		ArrayList<org.processmining.framework.models.petrinet.Place> places = petriNet.getPlaces();
		for( org.processmining.framework.models.petrinet.Place place : places ){
			String placeName = place.getName();
			Place modelPlace = new Place(placeName);
			modelPlaceList.addPlace(modelPlace);
		}

		ArrayList<org.processmining.framework.models.petrinet.Transition> transitions =  petriNet.getTransitions();
		for( org.processmining.framework.models.petrinet.Transition transition : transitions ) {
			String transitionName = transition.getName();
			Transition modelTransition = new Transition(transitionName);
			modelTransition.setID(transition.getIdentifier());
			modelTransitionList.addTransition(modelTransition);
		}

		ArrayList<PNEdge> arcs = petriNet.getEdges();
		for( PNEdge arc : arcs ){
			String source = arc.getTail().getName();
			String target = arc.getHead().getName();
			attachRelation(source, target, modelTransitionList, modelPlaceList);
		}

		ProcessModel model = new ProcessModel(modelPlaceList, modelTransitionList);
		setStartAndEndPlace(model);
		return model;
	}

	public static ProcessModel loadFromPNMLFile(File file) {
		PlaceList modelPlaceList = new PlaceList();
		TransitionList modelTransitionList = new TransitionList();

		Document document = parse(file);
		Element rootElement = document.getDocumentElement();

		NodeList placeList = rootElement.getElementsByTagName("place");
		for (int i = 0; i < placeList.getLength(); i++) {
			Element place = (Element) placeList.item(i);
			String placeName = place.getAttribute("id");
			
			Place modelPlace = new Place(placeName);
			modelPlaceList.addPlace(modelPlace);
		}

		NodeList transitionList = rootElement.getElementsByTagName("transition");
		for (int i = 0; i < transitionList.getLength(); i++) {
			Element transition = (Element) transitionList.item(i);
			String transitionName = transition.getAttribute("id");
			
			Transition modelTransition = new Transition(transitionName);
			modelTransitionList.addTransition(modelTransition);
		}

		NodeList arcList = rootElement.getElementsByTagName("arc");
		for (int i = 0; i < arcList.getLength(); i++) {
			Element arc = (Element) arcList.item(i);
			String arcSource = arc.getAttribute("source");
			String arcTarget = arc.getAttribute("target");

			attachRelation(arcSource, arcTarget, modelTransitionList, modelPlaceList);
		}

		ProcessModel model = new ProcessModel(modelPlaceList, modelTransitionList);
		setStartAndEndPlace(model);
		return model;
	}

	public static Document parse(File file) {
		Document document = null;
		try {
			// DOM parser instance
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			// parse an XML file into a DOM tree
			document = builder.parse(file);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

	private static void attachRelation(String sourceInput, String targetInput, TransitionList transitionList,
			PlaceList placeList) {
		Object o = transitionList.getTransitionByName(sourceInput);
		// 如果前驱是transition，那后继肯定就是place
		if (o != null) {
			Transition t = (Transition) o;
			Place p = placeList.getPlaceByName(targetInput);
			t.addOutput(p.getName());
			p.addInput(t.getName());
		} else {
			Place p = placeList.getPlaceByName(sourceInput);
			Transition t = transitionList.getTransitionByName(targetInput);
			t.addInput(p.getName());
			p.addOutput(t.getName());
		}
	}

	private static void setStartAndEndPlace(ProcessModel model) {
		for (Place place : model.getPlaces().getPlaces()) {
			if (place.getInputs().size() == 0) {
				place.addInput("-");
				model.setStartPlace(place);
				continue;
			}
			if (place.getOutputs().size() == 0) {
				place.addOutput("-");
				model.setEndPlace(place);
				continue;
			}
		}
		Transition tBigCircle = new Transition();
		tBigCircle.setName("-");
		tBigCircle.addInput(model.getEndPlace().getName());
		tBigCircle.addOutput(model.getStartPlace().getName());
		model.getTransitions().addTransition(tBigCircle);
	}
}
