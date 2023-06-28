/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.enginemain;

/**
 *
 * @author LiChuanyi
 */
public class EngineTime {
    
    private static int engineTime;
    
    public static void initiallize(){
        engineTime = 0;
    }
    
    public static void timeGoOn(){
        EngineTime.engineTime ++;
    }
    
    public static int getEngineTime(){
        return EngineTime.engineTime;
    }
}
