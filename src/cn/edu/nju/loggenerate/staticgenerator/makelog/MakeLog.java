package cn.edu.nju.loggenerate.staticgenerator.makelog;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.staticgenerator.concurrentRegion.RegionAndRules;
import cn.edu.nju.loggenerate.staticgenerator.concurrentRegion.Rule;

public class MakeLog {
	
	private static String path = "";
	
	public static void setPath(String queue_path){
		path = queue_path;
	}

//	public static FileQueue dealWithAllParts(
//			List<RegionAndRules> regionAndRulesList, List<String> mainPathNames) {
//		FileQueue outQueue = new FileQueue("out_file.txt", path);
	public static NewQueue dealWithAllParts(
			List<RegionAndRules> regionAndRulesList, List<String> mainPathNames) {
		NewQueue outQueue = new NewQueue();
		int partNumber = 0;

		if(regionAndRulesList.size()==0)
		{
			outQueue.offer(mainPathNames, partNumber);
		}
		for (RegionAndRules region_rule : regionAndRulesList) {
//			FileQueue fileQueue = MakeLog.dealwithSigleRule(region_rule);
			NewQueue fileQueue = MakeLog.dealwithSigleRule(region_rule);

			if (partNumber == 0) {
				List<String> prePart = new ArrayList<String>();
				List<String> postPart = new ArrayList<String>();
				int pre_index = mainPathNames.indexOf(region_rule
						.getPartRegion()[0]);
				int post_index = mainPathNames.indexOf(region_rule
						.getPartRegion()[1]);

				for (int i = 0; i < pre_index + 1; i++) {
					prePart.add(mainPathNames.get(i));
				}
				for (int i = post_index; i < mainPathNames.size(); i++) {
					postPart.add(mainPathNames.get(i));
				}
				List<String> q_list;
				while ((q_list = fileQueue.pollList()) != null) {
					List<String> outTmp = new ArrayList<String>();
					outTmp.addAll(prePart);
					outTmp.addAll(q_list);
					outTmp.addAll(postPart);

					outQueue.offer(outTmp, partNumber + 1);
				}
			} else {
				List<String> q_outlist;
				while ((q_outlist = outQueue.pollList()) != null) {
					if (outQueue.pollNextNum() == partNumber) {
						List<String> prePart = new ArrayList<String>();
						List<String> postPart = new ArrayList<String>();
						int pre_index = q_outlist.indexOf(region_rule
								.getPartRegion()[0]);
						int post_index = q_outlist.indexOf(region_rule
								.getPartRegion()[1]);

						for (int i = 0; i < pre_index + 1; i++) {
							prePart.add(q_outlist.get(i));
						}
						for (int i = post_index; i < q_outlist.size(); i++) {
							postPart.add(q_outlist.get(i));
						}
						List<String> q_list;
						while ((q_list = fileQueue.pollList()) != null) {
							List<String> outTmp = new ArrayList<String>();
							outTmp.addAll(prePart);
							outTmp.addAll(q_list);
							outTmp.addAll(postPart);

							outQueue.offer(outTmp, partNumber + 1);
						}
						fileQueue.reStart();
					} else {
						outQueue.offer(q_outlist, partNumber + 1);
						break;
					}
				}
			}
			fileQueue.close();
			partNumber++;
		}
		
		return outQueue;
	}

//	public static FileQueue dealwithSigleRule(RegionAndRules rules) {
	public static NewQueue dealwithSigleRule(RegionAndRules rules) {
		List<List<String>> traces = rules.getIrrelevantPaths();
		List<Rule> eliminateRules = rules.getEliminateRules();
		List<Rule> inserRules = rules.getInserRules();

//		FileQueue fQueue = new FileQueue("lcy_queue1.txt", path);
//		FileQueue fQueueinsert = new FileQueue("lcy_queue2.txt", path);
//		FileQueue fQueueOut = new FileQueue("lcy_queue3.txt", path);
		
		NewQueue fQueue = new NewQueue();
		NewQueue fQueueinsert = new NewQueue();
		NewQueue fQueueOut = new NewQueue();
		
		/***********************************************************/
		if(traces.size() < 2) {
			fQueue.close();
			fQueueinsert.close();
			fQueueOut.offer(traces.get(0), 0);
			return fQueueOut;
		}

		MakeLog.writeWithEliminateRules(traces.get(0), traces.get(1), fQueue,
				1, eliminateRules);

		List<String> q_tmpList;
		while ((q_tmpList = fQueue.pollList()) != null) {
			int nextTraceIndex = fQueue.pollNextNum();

			if (nextTraceIndex < traces.size()) {
				MakeLog.writeWithEliminateRules(q_tmpList,
						traces.get(nextTraceIndex), fQueue, nextTraceIndex + 1,
						eliminateRules);
			} else {
				if (inserRules.size() > 0) {
					writeWithInsertRules(q_tmpList, inserRules.get(0),
							fQueueinsert, 0);
				} else {
					writeOutput(q_tmpList, fQueueOut);
				}
			}
		}

		fQueue.close();

		while ((q_tmpList = fQueueinsert.pollList()) != null) {
			int nextRuleIndex = fQueueinsert.pollNextNum();

			if (nextRuleIndex < inserRules.size()) {
				writeWithInsertRules(q_tmpList, inserRules.get(nextRuleIndex),
						fQueueinsert, nextRuleIndex);
			} else {
				writeOutput(q_tmpList, fQueueOut);
			}
		}

		fQueueinsert.close();
		return fQueueOut;
	}

//	public static void writeWithEliminateRules(List<String> trace1,
//			List<String> trace2, FileQueue fQueue, int nextNum,
//			List<Rule> eliminateRules) {
	public static void writeWithEliminateRules(List<String> trace1,
				List<String> trace2, NewQueue fQueue, int nextNum,
				List<Rule> eliminateRules) {
		List<Integer> places = new ArrayList<Integer>();
		for (int i = 0; i < trace1.size(); i++) {
			places.add(0);
		}

		boolean outOK = true;
		while (places.get(0) <= trace2.size()) {
			List<String> oneTmp = new ArrayList<String>();
			for (int index_place = 0; index_place < places.size(); index_place++) {
				if (index_place == 0) {
					for (int printNum = 0; printNum < places.get(index_place); printNum++) {
						oneTmp.add(trace2.get(printNum));
						// System.out.print(trace2.get(printNum));
					}
				} else {
					for (int printNum = places.get(index_place - 1); printNum < places
							.get(index_place); printNum++) {
						oneTmp.add(trace2.get(printNum));
						// System.out.print(trace2.get(printNum));
					}
				}
				oneTmp.add(trace1.get(index_place));
				// System.out.print(trace1.get(index_place));

				if (index_place == places.size() - 1) {
					for (int trace2left = places.get(index_place); trace2left < trace2
							.size(); trace2left++) {
						oneTmp.add(trace2.get(trace2left));
						// System.out.print(trace2.get(trace2left));
					}
					// System.out.println();

					outOK = true;
					for (int index = trace1.size() - 1; index >= 0; index--) {
						if (places.get(index) < trace2.size()) {
							places.set(index, places.get(index) + 1);
							if (index < trace1.size() - 1) {
								for (int left = index + 1; left < trace1.size(); left++) {
									places.set(left, places.get(index));
								}
							}
							outOK = false;
							break;
						} else {
							continue;
						}
					}
				}
			}

			boolean addOK = true;
			for (Rule elim : eliminateRules) {
				String pre = elim.getPreTran();
				String post = elim.getPostTran();
				if (oneTmp.indexOf(post) < oneTmp.indexOf(pre)) {
					addOK = false;
					break;
				}
			}
			if (addOK) {
				fQueue.offer(oneTmp, nextNum + 1);
			}

			if (outOK) {
				break;
			}
		}
	}

//	public static void writeWithInsertRules(List<String> trace,
//			Rule insertRule, FileQueue fqueue, int ruleNumber) {
	public static void writeWithInsertRules(List<String> trace,
			Rule insertRule, NewQueue fqueue, int ruleNumber) {
		List<String> insertPart = new ArrayList<String>();
		List<String> prePart = new ArrayList<String>();
		List<String> postPart = new ArrayList<String>();
		int pre_index = trace.indexOf(insertRule.getPreTran());
		int post_index = trace.indexOf(insertRule.getPostTran());

		for (int i = pre_index + 1; i < post_index; i++) {
			insertPart.add(trace.get(i));
		}
		for (int i = 0; i < pre_index + 1; i++) {
			prePart.add(trace.get(i));
		}
		for (int i = post_index; i < trace.size(); i++) {
			postPart.add(trace.get(i));
		}

		int insert_place = 0;
		int insertPartSize = insertPart.size();
		while (insert_place <= insertPartSize) {
			List<String> outTmp = new ArrayList<String>();
			outTmp.addAll(prePart);
			for (int i = 0; i < insert_place; i++) {
				outTmp.add(insertPart.get(i));
			}
			outTmp.add(insertRule.getCurTran());
			for (int i = insert_place; i < insertPartSize; i++) {
				outTmp.add(insertPart.get(i));
			}
			outTmp.addAll(postPart);

			fqueue.offer(outTmp, ruleNumber + 1);
			insert_place++;
		}
	}

//	public static void writeOutput(List<String> q_tmpList, FileQueue fQueueOut) {
//		fQueueOut.offer(q_tmpList, 0);
//	}
	
	public static void writeOutput(List<String> q_tmpList, NewQueue fQueueOut) {
		fQueueOut.offer(q_tmpList, 0);
	}

	public static void printList(String name, List<String> trace) {
		System.out.println(name + ":");
		for (String task : trace) {
			System.out.print(task + ", ");
		}
		System.out.println();
	}

	public static void printList(List<String> trace) {
		for (String task : trace) {
			System.out.print(task + ", ");
		}
	}
}
