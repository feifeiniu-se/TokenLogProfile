/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.enginemain;

import cn.edu.nju.loggenerate.modelengine.enginebeans.*;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Place;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Transition;
import java.util.List;

/**
 * 
 * @author LiChuanyi
 */
public class EngineInstance {
	private static int TIMES = 0;

	public static void initiallizeEngine() {
		PlaceList.initiallize();
		TransitionList.initiallize();
		OutTimeTokens.initiallize();
		UnchoosedTransitions.initiallize();
		PlaceWaitingQueue.initiallize();
		WaitingPlaces.initiallize();
		EngineTime.initiallize();
		EngineCase.initiallize();
		TIMES = 0;
	}

	public static void initiallizeStartPlace() {
		for (int i = 0; i < PlaceList.getStartPlace().getOutputs().size(); i++) {
			PlaceWaitingQueue.addPlaceToQueue(PlaceList.getStartPlace().getPlaceID());
			Token firstToken = new Token();
			firstToken.setProduceTime(EngineTime.getEngineTime());
			firstToken.setCaseid(EngineCase.getEngineCase());
			PlaceList.getStartPlace().receiveOne(firstToken);
			EngineTime.timeGoOn();
		}
		// EngineTime.timeGoOn();
	}

	public static void startEngine() {
		Integer currentPlaceID;
		Place currentPlace;

		int times = 0;

		while (TransitionList.hasUnExecuted()) {
			EngineCase.caseGoOn();
			times++;
			initiallizeStartPlace();
			// get the head of the place waiting queue until the queue is empty
			while ((currentPlaceID = PlaceWaitingQueue.removeRandomly()) != null) {
				// define the transition to be executed
				Transition nextOK = null;

				// get current place outputs
				currentPlace = PlaceList.getByID(currentPlaceID);
				List<Integer> outputs = currentPlace.getOutputs();

				// 1.if the output is only one then judge weather it is ok and
				// if ok set the transition to be executed;
				// if not ok then set the transition to be executed "null";
				// 2.if the out put is more than one then choose the one hasn't
				// been choosed and is ok now; but if there is
				// no unchoosed one then add all the outputs to the unchoosed
				// list and get one ok and remove this one from the
				// unchoosed list; and if no one is ok then set the transition
				// to be executed "null".
				int outputsSize = outputs.size();
				if (outputsSize > 1) {
					String unchoosed = UnchoosedTransitions
							.checkIfHasUnchoosed(currentPlaceID);
					if (unchoosed.equals("notinlist")) {
						UnchoosedTransitions.addListToMap(currentPlaceID,
								outputs);
						nextOK = UnchoosedTransitions
								.getOneUnchoosed(currentPlaceID);
					} else {
						if (unchoosed.equals("has")) {
							nextOK = UnchoosedTransitions
									.getOneUnchoosed(currentPlaceID);
						} else {
							nextOK = UnchoosedTransitions
									.getOne(currentPlaceID);
							if (!nextOK.isReady()) {
								nextOK = null;
							}
						}
					}
				} else if (outputsSize == 1) {
					Transition nextTMP = TransitionList.getByID(outputs.get(0));
					if (nextTMP.isReady()) {
						nextOK = nextTMP;
					}
				} else {
					continue;
				}

				// 1.if the transition to be executed is not null, then remove
				// one instance of the other needed places from the waiting
				// queue;
				// and then execute the transition. While executing the
				// transiton put all the outputs' places into the waiting queue.
				// 2.if the transition to be executed is null,
				if (nextOK != null) {
					// PlaceWaitingQueue.print();
					PlaceWaitingQueue.removeOthersFromQueue(currentPlaceID,
							nextOK);
					nextOK.excute();
					// PlaceWaitingQueue.print();
					// UnchoosedTransitions.print();

					PlaceWaitingQueue.removeAllLast();

					if (!WaitingPlaces.isEmpty()) {
						List<Integer> waitingButOK = WaitingPlaces.getOKs();
						if (waitingButOK.size() > 0) {
							for (Integer okid : waitingButOK) {
								WaitingPlaces.deleteFromWait(okid);
								PlaceWaitingQueue.addPlaceToQueueHead(okid);
							}
							continue;
						}
					}
					if (PlaceWaitingQueue.getHead() == null) {
						if (WaitingPlaces.isEmpty()) {
							if (TransitionList.hasUnExecuted()) {
								EngineCase.caseGoOn();
								initiallizeStartPlace();
							} else {
								break;
							}
						} else {
							//PlaceWaitingQueue.removeHead();
							PlaceWaitingQueue.addPlaceToQueueHead(PlaceList.getStartPlace().getPlaceID());
							EngineCase.caseGoOn();
						}
					}
				} else {
					WaitingPlaces.addToWait(currentPlaceID);
				}
			}
		}
	}
}
