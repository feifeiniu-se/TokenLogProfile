package cn.edu.nju.tlpsimilar.extractRelations;

import cn.edu.nju.loggenerate.modelengine.enginebeans.PlaceList;
import cn.edu.nju.loggenerate.modelengine.enginebeans.TransitionList;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.*;
import java.util.stream.Collectors;

import static cn.edu.nju.tlpsimilar.extractRelations.GetConcurrent.getConcurrent;
import static cn.edu.nju.tlpsimilar.extractRelations.GetExclusive.getExclusive;
import static cn.edu.nju.tlpsimilar.extractRelations.GetLoop.getLoop;
import static cn.edu.nju.tlpsimilar.extractRelations.GetSequence.getSequence;

public class GetRelations {
    //    tokenListMap中包含没有重复的tokenlist
    public static HashMap<HashMap<String, String>, String> getRelations(HashMap<List<Token>, Set<String>> tokenListMap){
        HashMap<HashMap<String, String>, String> relations = new HashMap<HashMap<String, String>, String>();
        Iterator<Map.Entry<List<Token>, Set<String>>> entryKeyIterator = tokenListMap.entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Map.Entry<List<Token>, Set<String>> e = entryKeyIterator.next();
            List<Token> key = e.getKey();
//          proSet和conSet中分别包含了生产任务和消费任务出现不止一次的token，接下来要识别出不同的token之间是并发关系还是循环关系
            Map<String, List<Token>> proSet = groupByPro(key);
            Map<String, List<Token>> conSet = groupByCon(key);

            //并发的开始和结束token
            Set<Token> start_concurrent = new HashSet<Token>();
            Set<Token> end_concurrent = new HashSet<Token>();
            //循环的开始、中间和结束token
            Set<Token> start_loop = new HashSet<Token>();
            Set<Token> end_loop = new HashSet<Token>();

//          生产任务出现不止一次
            for(List<Token> tokens:proSet.values()){
                for(int i=0; i<tokens.size()-1; i++){
                    for(int j=i+1; j<tokens.size(); j++){
                        if(tokens.get(i).getProName().equals(tokens.get(j).getProName())){//如果两个token的生产任务相同
                            if(tokens.get(i).getProduceTime() == tokens.get(j).getProduceTime()){//如果生产时间相同，那么是并发
//                            System.out.println(tokens.get(i));
                                if(!(tokens.get(i).getConName().equals(tokens.get(j).getConName())) && !(tokens.get(i).getConsumeTime() == tokens.get(j).getConsumeTime())){
//                                System.out.println("OK");
                                    if(tokens.get(i).getConsumeTime()<tokens.get(j).getConsumeTime()){
                                        start_concurrent.add(tokens.get(i));
                                    }else
                                        start_concurrent.add(tokens.get(j));
                                }else{
                                    System.out.println("start_concurrent false");
                                }
                            }else{//如果生产任务相同生产时间不同，那么可能是循环
                                if(tokens.get(i).getConName().equals(tokens.get(j).getConName())){
                                    if(!(tokens.get(i).getConsumeTime() == tokens.get(j).getConsumeTime())){
                                        //生产任务相同，生产时间不同，消费任务相同，消费时间不同，为循环中间token
                                        putRelation(tokens.get(i), relations, "L");
                                    }else{
                                        System.out.println("something wrong");
                                    }
                                }else if(tokens.get(i).getConsumeTime() > tokens.get(j).getConsumeTime()){//生产任务相同，生产时间不同，消费任务不同，消费时间不同为循环的开始和结束
//                                System.out.println(tokens.get(i));
//                                System.out.println(tokens.get(j));
                                    start_loop.add(tokens.get(j));
                                    end_loop.add(tokens.get(i));
                                }else if(tokens.get(i).getConsumeTime()<tokens.get(j).getConsumeTime()){
                                    start_loop.add(tokens.get(i));
                                    end_loop.add(tokens.get(j));
                                }else{
                                    System.out.println("something wrong");
                                }
                            }
                        }
                    }
                }
            }

//            消费任务出现不止一次
            for(List<Token> tokens:conSet.values()){
                for(int i=0; i<tokens.size()-1; i++){
                    for(int j=i+1; j<tokens.size(); j++){
                        if(tokens.get(i).getConsumeTime() == tokens.get(j).getConsumeTime()){//如果消费任务相同，消费时间相同，那么是并发的结束
                            if(!(tokens.get(i).getProName().equals(tokens.get(j).getProName())) && !(tokens.get(i).getProduceTime() == tokens.get(j).getProduceTime())){
                                if(tokens.get(i).getConsumeTime()< tokens.get(j).getConsumeTime()){
                                    end_concurrent.add(tokens.get(j));
                                }else
                                    end_concurrent.add(tokens.get(i));

                            }else{
                                System.out.println("end_concurrent false");
                            }
                        }else{//如果消费任务相同消费时间不同，那么可能是循环
                            if(tokens.get(i).getProName().equals(tokens.get(j).getProName())){
                                if(!(tokens.get(i).getProduceTime() == tokens.get(j).getProduceTime())){
                                    //生产任务相同，生产时间不同，消费任务相同，消费时间不同，为循环中间token
                                    putRelation(tokens.get(i), relations, "L");
                                }else{
                                    System.out.println("生成任务相同，生成时间相同，消费任务相同，消费时间不同");//生成任务相同，生成时间相同，消费任务相同，消费时间不同，报错
                                }
                            }
//                            else if(!(tokens.get(i).getProTime().equals(tokens.get(j).getProTime()))){//生产任务不同，生产时间不同，消费任务相同，消费时间不同为循环的开始
//                                start_loop.add(tokens.get(i));
//                                start_loop.add(tokens.get(j));
//                            }else{
//                                System.out.println("生成任务不同，生成时间相同，消费任务相同，消费时间不同");
//                            }
                        }
                    }
                }
            }
            //记录了所有的并发和循环的开始节点和结束节点

//          找并发结构和循环结构
            if(!proSet.isEmpty() && !conSet.isEmpty()){
//                System.out.println(start_concurrent);
//                System.out.println(end_concurrent);
                if(!start_concurrent.isEmpty() && !end_concurrent.isEmpty()){
//                    System.out.println(start_concurrent);
                    getConcurrent(key, relations, start_concurrent, end_concurrent);
                }
//                System.out.println(e.getValue());
                if(!start_loop.isEmpty() && !end_loop.isEmpty()){
//                    System.out.println("loop:" + e);
                    getLoop(e, relations, start_loop, end_loop);
                }
//                System.out.println(e.getValue());

            }
//            找选择结构
////            存储顺序结构
            getSequence(key, relations);
        }
        if(tokenListMap.size()>1){
            getExclusive(tokenListMap, relations);
        }
        return relations;
    }

    public static void putRelation(Token t, HashMap<HashMap<String, String>, String> relations, String r){//��token�еĹ�ϵr���������
        HashMap<String, String> k = new HashMap<>();
//        t.print();
//        System.out.println(t.getProducer() + " " + t.getConsumer());

        String pro = (t.getProducer()==-2) ? PlaceList.getByID(TransitionList.getByID(t.getConsumer()).getInputs().get(0)).getName() : t.getProName();
        String con = (t.getConsumer()==-1) ? PlaceList.getByID(TransitionList.getByID(t.getProducer()).getOutputs().get(0)).getName() : t.getConName();
//        System.out.println(pro + " " + con);

        k.put(pro, con);
        String value = r;
        if(relations.containsKey(k)){//如果矩阵中已经存在，则关系附加在后边
            if(!relations.get(k).contains(r)){
                value = relations.get(k) + "," + value;
            }else{
                value = relations.get(k);
            }
        }
        relations.put(k, value);
    }
    //将tokenlist中的token根据生产者分组并挑选出出现次数大于1的token
    public static Map<String, List<Token>> groupByPro(List<Token> tokenList){
        Set<String> xSet = new TreeSet<String>();
        Map<String, Long> filter = tokenList.stream().collect(Collectors.groupingBy(Token::getProName,Collectors.counting()));//将tokenlist根据生产者分组排序，并统计次数
        filter.entrySet().stream().filter(e -> e.getValue() > 1).forEach(x->xSet.add(x.getKey()));//filter，将生产者出现次数大于1的挑出来
        Map<String, List<Token>> group =tokenList.stream().filter(t->xSet.contains(t.getProName())).
                collect(Collectors.groupingBy(Token::getProName));//将tokenlist中所有的token根据生产者分组，去掉生产者只出现一次的token，剩下出现次数大于1的token
//        System.out.println(group);
        return group;

    }

// 将tokenlist中的token根据消费任务分组并过滤出出现次数大于1的token
    public static Map<String, List<Token>> groupByCon(List<Token> tokenList){
        Set<String> xSet = new TreeSet<String>();
        Map<String, Long> filter = tokenList.stream().collect(Collectors.groupingBy(Token::getConName, Collectors.counting()));
        filter.entrySet().stream().filter(e->e.getValue()>1).forEach(x->xSet.add(x.getKey()));
        Map<String, List<Token>> group =tokenList.stream().filter(t->xSet.contains(t.getConName())).
                collect(Collectors.groupingBy(Token::getConName));
//        System.out.println(group);
        return group;
    }

}

