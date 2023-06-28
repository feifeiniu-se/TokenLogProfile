/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.flowmaker;

import cn.edu.nju.loggenerate.modelengine.enginebeans.PlaceList;
import cn.edu.nju.loggenerate.modelengine.enginebeans.TransitionList;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Place;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Transition;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LiChuanyi
 */
public class WorkFlowMaker {

    private static List<String[]> placesLines;
    private static List<String[]> transitionsLines;

    public static void initiallize() {
        placesLines = new ArrayList<String[]>();
        transitionsLines = new ArrayList<String[]>();
    }

    public static void readPlaces(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] placeLine = line.split("\\|");
                placesLines.add(placeLine);
                Place place = new Place(placeLine[1]);
                PlaceList.addPlace(place);
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(WorkFlowMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void readTransitions(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] transitionLine = line.split("\\|");
                transitionsLines.add(transitionLine);
                Transition transition = new Transition(transitionLine[1]);
                TransitionList.addTransition(transition);
            }
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(WorkFlowMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void constructPlaces() {
        for (String[] placeline : placesLines) {
            String[] inputs = placeline[0].split(",");
            for (String input : inputs) {
                if (!input.equals("")) {
                    PlaceList.getByID(PlaceList.getIDByName(placeline[1])).addInput(TransitionList.getIDByName(input));
                }
            }

            if (placeline.length > 2) {
                String[] outputs = placeline[2].split(",");
                for (String output : outputs) {
                    PlaceList.getByID(PlaceList.getIDByName(placeline[1])).addOutput(TransitionList.getIDByName(output));
                }
            }
        }
    }

    public static void constructTransitions() {
        for (String[] transitionline : transitionsLines) {
            String[] inputs = transitionline[0].split(",");
            for (String input : inputs) {
                TransitionList.getByID(TransitionList.getIDByName(transitionline[1])).addInput(PlaceList.getIDByName(input));
            }

            String[] outputs = transitionline[2].split(",");
            for (String output : outputs) {
                TransitionList.getByID(TransitionList.getIDByName(transitionline[1])).addOutput(PlaceList.getIDByName(output));
            }
        }
    }
    
    public static void setPlaces(List<String[]> places){
    	placesLines = places;
    }
    public static void setTransitions(List<String[]> transitions){
    	transitionsLines = transitions;
    }
}
