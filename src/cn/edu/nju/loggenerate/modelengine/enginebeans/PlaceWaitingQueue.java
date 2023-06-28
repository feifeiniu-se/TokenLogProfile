/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.enginebeans;

import cn.edu.nju.loggenerate.modelengine.enginemain.EngineTime;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Transition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author LiChuanyi
 */
public class PlaceWaitingQueue {

	private static LinkedList<Integer> placeQueue;

	public static void initiallize() {
		placeQueue = new LinkedList<Integer>();
	}

	public static void addPlaceToQueue(int placeID) {
		placeQueue.offer(placeID);
	}

	public static Integer getHead() {
		return placeQueue.peek();
	}

	public static Integer removeHead() {
		return placeQueue.poll();
	}
	public static Integer removeRandomly() {
		Random r = new Random();
		int id = r.nextInt(placeQueue.size());
		Integer nextPlaceID = placeQueue.get(id);
		placeQueue.remove(id);
		return nextPlaceID;
	}

	public static boolean removeOthersFromQueue(int currentPlaceID,
			Transition transition) {
		List<Integer> placeID = transition.getInputs();

		for (Integer placeIDTmp : placeID) {
			if (placeIDTmp != currentPlaceID) {
				placeQueue.remove(placeIDTmp);
			}
		}

		return true;
	}

	public static void addPlaceToQueueHead(int placeID) {
		placeQueue.add(0, placeID);
	}

	public static boolean hasOneElement() {
		if (placeQueue.size() == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static void removeAllLast() {
		boolean has = false;
		List<Integer> lasts = new ArrayList<Integer>();
		for (Integer id : placeQueue) {
			if (PlaceList.getByID(id).isIsLast()) {
				lasts.add(id);
				Token consumedOne = PlaceList.getByID(id).consumeOne();
				consumedOne.setConsumeTime(EngineTime.getEngineTime());
				OutTimeTokens.addToken(consumedOne);
				has = true;
			}
		}
		if (has) {
			EngineTime.timeGoOn();
		}

		for (Integer item : lasts) {
			placeQueue.remove(item);
		}
	}

	public static void print(){
		for(Integer i : placeQueue){
			System.out.print(PlaceList.getByID(i).getName());
		}
		System.out.println();
	}
	
}
