/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.enginebeans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LiChuanyi
 */
public class WaitingPlaces {
    private static List<Integer> waitList;
    
    public static void initiallize(){
        waitList = new ArrayList<Integer>();
    }
    
    public static void addToWait(int placeID){
        waitList.add(placeID);
    }
    
    public static void deleteFromWait(Integer placeID){
        waitList.remove(placeID);
    }
    
    public static boolean isEmpty(){
        if(waitList.size() > 0){
            return false;
        }else{
            return true;
        }
    }
    
    public static List<Integer> getOKs(){
        List<Integer> oks = new ArrayList<Integer>();
        for(Integer id : waitList){
            for(Integer tid : PlaceList.getByID(id).getOutputs()){
                if(TransitionList.getByID(tid).isReady()){
                    oks.add(id);
                    break;
                }
            }
        }
        return oks;
    }
}
