package cn.edu.nju.loggenerate.staticgenerator.tInvariant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.LinkedList;

import cn.edu.nju.loggenerate.staticgenerator.concurrentRegion.ConcurrentRegion;
import cn.edu.nju.loggenerate.staticgenerator.concurrentRegion.PInvariantToModel;
import cn.edu.nju.loggenerate.staticgenerator.concurrentRegion.RegionAndRules;
import cn.edu.nju.loggenerate.staticgenerator.makelog.MakeLog;
import cn.edu.nju.loggenerate.staticgenerator.makelog.NewQueue;
import cn.edu.nju.loggenerate.staticgenerator.math.CalculateMatrix;
import cn.edu.nju.loggenerate.staticgenerator.math.GetTransitionArrays;
import cn.edu.nju.loggenerate.staticgenerator.math.Solver;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;
import cn.edu.nju.loggenerate.staticgenerator.model.Transition;

public class TraceGen2 {

    public static void addCircleToModel(ProcessModel test) {

        if (!test.getStartPlace().getInputs().contains("-")) {
            Transition tBigCircle = new Transition();
            tBigCircle.setName("-");
            tBigCircle.addInput(test.getStartPlace().getName());
            tBigCircle.addOutput(test.getEndPlace().getName());
            test.getStartPlace().addInput("-");
            test.getEndPlace().addOutput("-");
            test.getTransitions().addTransition(tBigCircle);
        } else {
            Transition tBigCircle = new Transition();
            tBigCircle.setName("-");
            tBigCircle.addInput(test.getStartPlace().getName());
            tBigCircle.addOutput(test.getEndPlace().getName());
            test.getTransitions().addTransition(tBigCircle);
        }

    }

    //	public static NewQueue rootVar(LoopRules root, String filename, String filePath) {
//		List<LoopRules> firstRules = root.getSubRules();
    public static NewQueue rootVar(LoopRules root) {
        List<LoopRules> firstRules = root.getSubRules();
        LinkedList<List<String>> toBeInserted = new LinkedList<List<String>>();
        // 生成mainloop文件

        NewQueue mainQueue = computer(root.getOriModel());
        toBeInserted.offer(mainQueue.tracesQueue.getFirst());

        if (firstRules == null) {
            //System.out.println("This main path has no loops!!!! BREAK!!!");
            return mainQueue;
        }

        Iterator<LoopRules> iter = firstRules.iterator();
        List<LoopRules> nextRules = new ArrayList<LoopRules>();

//		List<FileQueue> loopQueue = new ArrayList<FileQueue>();
        List<NewQueue> loopQueue = new ArrayList<NewQueue>();

        int i = 0;
        //
        while (iter.hasNext()) {
            LoopRules rule = iter.next();
            List<LoopRules> nextLoopRule = rule.getSubRules();

            if (!isSingle(rule)) {
                ProcessModel model = rule.getNewLoopModel();
                addCircleToModel(model);
                NewQueue queue = computer(model);
                loopQueue.add(queue);
            } else {// 单个循环的处理
                NewQueue queue = new NewQueue();
                ProcessModel model = rule.getPartloopModel();
                List<Transition> t1 = model.getTransitions().getTransitions();
                List<String> list = new ArrayList<String>();
                list.add(t1.get(0).getName());
                queue.offer(list, 0);
                loopQueue.add(queue);
            }
            i++;
            if (nextLoopRule != null)
                nextRules.addAll(nextLoopRule);
        }

        iter = firstRules.iterator();// itertaor指向开始
//		Iterator<FileQueue> fileIter = loopQueue.iterator();// 文件itertaor指向开始
        Iterator<NewQueue> fileIter = loopQueue.iterator();

        int k = 0;
        List<NewQueue> tempQueue = new ArrayList<NewQueue>();

        List<String> q_list = toBeInserted.poll();
        while (iter.hasNext() && fileIter.hasNext()) {
            LoopRules rule = iter.next();
            NewQueue queue = fileIter.next();

            String startStr = rule.getInsertBetween()[0];
            String endStr = rule.getInsertBetween()[1];

            NewQueue temp = new NewQueue();
            int start = q_list.indexOf(startStr);
            int end = q_list.indexOf(endStr);
            if (start != -1 && end != -1) {
                List<String> add_list;
                queue.reStart();
                boolean added = false;
                while ((add_list = queue.pollList()) != null) {
                    if (!isSingle(rule)) {
                        List<String> outString = new ArrayList<String>();
                        outString.addAll(q_list.subList(0, end));
                        outString.addAll(add_list);
                        outString.addAll(q_list.subList(end,
                                q_list.size()));
                        temp.offer(outString, 0);
                        if (!added) {
                            toBeInserted.offer(outString);
                            added = true;
                        }
                    } else {// 循环结构
                        for (int m = start + 1; m <= end; m++) {
                            List<String> outString = new ArrayList<String>();
                            outString.addAll(q_list.subList(0, m));
                            outString.addAll(add_list);
                            outString.addAll(q_list.subList(m,
                                    q_list.size()));
                            temp.offer(outString, 0);
                            if (!added) {
                                toBeInserted.offer(outString);
                                added = true;
                            }
                        }
                    }
                }
            }
            tempQueue.add(temp);
            k++;
        }// 一层迭代
        for (NewQueue temp : tempQueue) {
            temp.reStart();
            while ((q_list = temp.pollList()) != null) {
                mainQueue.offer(q_list, 0);
            }
            temp.close();
        }
        tempQueue.clear();
        for (NewQueue q : loopQueue) {
            q.close();
        }

        loopQueue.clear();

        while (nextRules.size() != 0) {
            i = 0;
            iter = nextRules.iterator();
            List<LoopRules> newLoopRules = new ArrayList<LoopRules>();

            while (iter.hasNext()) {
                LoopRules rule = iter.next();
                if (!isSingle(rule)) {
                    ProcessModel model = rule.getNewLoopModel();
                    addCircleToModel(model);
                    NewQueue queue = computer(model);
                    loopQueue.add(queue);
                } else {
                    NewQueue queue = new NewQueue();
                    ProcessModel model = rule.getPartloopModel();
                    List<Transition> t1 = model.getTransitions()
                            .getTransitions();
                    List<String> list = new ArrayList<String>();
                    list.add(t1.get(0).getName());
                    queue.offer(list, 0);
                    loopQueue.add(queue);
                }
                i++;
                if (rule.getSubRules() != null)
                    newLoopRules.addAll(rule.getSubRules());
            }
            iter = nextRules.iterator();
            fileIter = loopQueue.iterator();

            k = 0;
            while (iter.hasNext() && fileIter.hasNext()) {
                LoopRules rule = iter.next();
                NewQueue queue = fileIter.next();
                String startStr = rule.getInsertBetween()[0];
                String endStr = rule.getInsertBetween()[1];

                NewQueue temp = new NewQueue();
                q_list = toBeInserted.poll();

                int start = q_list.indexOf(startStr);
                int end = q_list.indexOf(endStr);
                if (start != -1 && end != -1) {
                    List<String> add_list;
                    queue.reStart();
                    boolean added = false;
                    while ((add_list = queue.pollList()) != null) {
                        if (!isSingle(rule)) {
                            List<String> outString = new ArrayList<String>();
                            outString.addAll(q_list.subList(0, end));
                            outString.addAll(add_list);
                            outString.addAll(q_list.subList(end,
                                    q_list.size()));
                            temp.offer(outString, 0);
                            if (!added) {
                                toBeInserted.offer(outString);
                                added = true;
                            }
                        } else {// 循环结构
                            for (int m = start + 1; m <= end; m++) {
                                List<String> outString = new ArrayList<String>();
                                outString.addAll(q_list.subList(0, m));
                                outString.addAll(add_list);
                                outString.addAll(q_list.subList(m,
                                        q_list.size()));
                                temp.offer(outString, 0);
                                if (!added) {
                                    toBeInserted.offer(outString);
                                    added = true;
                                }
                            }
                        }
                    }
                }
                if (start != -1 && end == -1) {
                    List<String> add_list;
                    queue.reStart();
                    boolean added = false;
                    while ((add_list = queue.pollList()) != null) {
                        if (!isSingle(rule)) {
                            List<String> outString = new ArrayList<String>();
                            outString.addAll(q_list.subList(0, start + 1));
                            outString.addAll(add_list);
                            outString.addAll(q_list.subList(start + 1,
                                    q_list.size()));
                            temp.offer(outString, 0);
                            if (!added) {
                                toBeInserted.offer(outString);
                                added = true;
                            }
                        } else {// 循环结构
                            for (int m = start + 1; m < q_list.size(); m++) {
                                List<String> outString = new ArrayList<String>();
                                outString.addAll(q_list.subList(0, m));
                                outString.addAll(add_list);
                                outString.addAll(q_list.subList(m,
                                        q_list.size()));
                                temp.offer(outString, 0);
                                if (!added) {
                                    toBeInserted.offer(outString);
                                    added = true;
                                }
                            }
                        }
                    }
                }
                tempQueue.add(temp);
                k++;
            }// 一层迭代
            for (NewQueue temp : tempQueue) {
                temp.reStart();
                while ((q_list = temp.pollList()) != null) {
                    mainQueue.offer(q_list, 0);
                }
                temp.close();
            }
            tempQueue.clear();
            for (NewQueue q : loopQueue) {
                q.close();
            }
            loopQueue.clear();
            nextRules = newLoopRules;
        }
        return mainQueue;
    }

    public static boolean isEnd(LoopRules rule) {
        if (rule.getSubRules() == null)
            return true;
        return false;
    }

    public static boolean isSingle(LoopRules rule) {
        if (rule.getSinglePlaceName() != null)
            return true;
        return false;
    }

    public static NewQueue computer(ProcessModel pm) {
        List<List<List<String>>> t_pathes = GetTransitionArrays.solve(pm);

        NewQueue outFile = new NewQueue();

        for (List<List<String>> path : t_pathes) {
            PInvariantToModel ptm = new PInvariantToModel(pm);
            List<ProcessModel> pms = ptm.convertToModel(path);

            ConcurrentRegion cr = new ConcurrentRegion(pm);
            cr.genConcurrentResions(pms);
            List<String> mainPathNames = cr.getMainPathTNames();
            List<RegionAndRules> regionAndRulesList = cr
                    .getRegionAndRulesList();

            NewQueue outQueue = MakeLog.dealWithAllParts(regionAndRulesList,
                    mainPathNames);

            List<String> q_out;
            int number = 0;
            while ((q_out = outQueue.pollList()) != null) {
                outFile.offer(q_out, number);
                number++;
            }

            outQueue.close();
        }

        return outFile;
    }

    /*
     * 首先计算ProcessModel 的矩阵值 然后对其进行不变量分解，得到
     */
    public static List<List<String>> TinvariantResult(ProcessModel pm) {
        // 大循环变迁T9
        int[][] totalMatrix = CalculateMatrix.calcMatrix(pm);
//		 PrintMatrix.printRelationMatrix(totalMatrix, pm);
//		 Solver.print(totalMatrix);

        ArrayList<int[]> LMS_T_out = Solver.solve(totalMatrix, "T");

//		System.out.println("------------------");
//		for (int i = 0; i < LMS_T_out.size(); i++) {
//			int[] l = LMS_T_out.get(i);
//			for (int j = 0; j < l.length; j++) {
//				System.out.print(l[j] + ",");
//			}
//			System.out.println();
//		}
        List<List<String>> retstr = new ArrayList<List<String>>();
        for (int i = 0; i < LMS_T_out.size(); i++) {
            List<String> lstr = new ArrayList<String>();
            int[] l = LMS_T_out.get(i);
            for (int j = 0; j < l.length; j++) {
                if (l[j] != 0)
                    lstr.add(pm.getTransitions().getTransitions().get(j)
                            .getName());
            }
            retstr.add(lstr);
        }

        return retstr;
    }

    public static boolean check(List<List<String>> path) {
        // TODO Auto-generated method stub
        for (List<String> str : path) {
            if (str.contains("-"))
                return true;
        }
        return false;
    }
}
