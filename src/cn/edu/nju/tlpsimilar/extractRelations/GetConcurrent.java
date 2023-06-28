package cn.edu.nju.tlpsimilar.extractRelations;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.*;

import static cn.edu.nju.tlpsimilar.extractRelations.GetRelations.putRelation;

public class GetConcurrent {
    public static void getConcurrent(List<Token> key, HashMap<HashMap<String, String>, String> relations, Set<Token> start_concurrent, Set<Token> end_concurrent){
//        System.out.println("Concurrent");
        Collections.sort(key, new SortByProTime());//对每个case中的log按照生产时间排序,如果生产时间相同那么根据消费时间排序
        for(Token s:start_concurrent){
//            System.out.println(s);
            putRelation(s, relations, "C");//将并发分支的开始放入relation
            int i = key.indexOf(s);//记录该并发分支目前的最后一个
            int j = i + 1;//判断下一个是否属于该并发分支
            while(j<key.size()){
                if(isOrder(key.get(i), key.get(j))){
                    if(!end_concurrent.contains(key.get(j))){
                        putRelation(key.get(j), relations, "C");
                        i=j;
                        j=j+1;
                    }else{
                        putRelation(key.get(j), relations, "C");
                        j=key.size();//如果已经是结束节点，则通过j终止循环
                    }
                }else{
                    j = j + 1;
                }
            }

        }
    }

//    判断两个token是否满足and_split关系
    public static boolean and_split(Token t1, Token t2){
        if(t1.getProName().equals(t2.getProName()) && t1.getProduceTime() == t2.getProduceTime() && !(t1.getConName().equals(t2.getConName())) && !(t1.getConsumeTime() == t2.getConsumeTime())){
            return true;
        }else
            return false;
    }
//判断两个token是否满足and_join关系
    public static boolean and_join(Token t1, Token t2){
        if(!(t1.getProName().equals(t2.getProName())) && !(t1.getProduceTime() == t2.getProduceTime()) && t1.getConName().equals(t2.getConName()) && t1.getConsumeTime() == t2.getConsumeTime()){
            return true;
        }else
            return false;
    }
//判断两个token是否是顺序关系
    public static boolean isOrder(Token t1, Token t2) {
        if (t1.getConName().equals(t2.getProName()) && t1.getConsumeTime() == t2.getProduceTime()) {
            return true;
        } else
            return false;
    }
}
