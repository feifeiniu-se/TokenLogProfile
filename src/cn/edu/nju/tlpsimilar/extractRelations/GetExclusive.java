package cn.edu.nju.tlpsimilar.extractRelations;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.*;

import static cn.edu.nju.tlpsimilar.extractRelations.GetRelations.putRelation;

public class GetExclusive {

    public static void getExclusive(HashMap<List<Token>, Set<String>> tokenListMap, HashMap<HashMap<String, String>, String> relations){
        Set<String> result1 = new TreeSet<String>();
        Set<String> result2 = new TreeSet<String>();

//      两两比对是否是选择结构
        Iterator<Map.Entry<List<Token>, Set<String>>> entryKeyIterator = tokenListMap.entrySet().iterator();
        while (entryKeyIterator.hasNext()) {
            Map.Entry<List<Token>, Set<String>> e = entryKeyIterator.next();
            for(Map.Entry<List<Token>, Set<String>> entry : tokenListMap.entrySet()){
                if(!e.equals(entry)){
                    if(!e.getValue().equals(entry.getValue())){
                        Set<String> andSet = new TreeSet<>();
                        Set<String> value1 = new TreeSet<String>();
                        value1.addAll(e.getValue());
                        Set<String> value2 = new TreeSet<String>();
                        value2.addAll(entry.getValue());
                        andSet.addAll(value1);
                        andSet.retainAll(value2);//两个集合求交集
                        value1.removeAll(andSet);//集合1的差集
                        value2.removeAll(andSet);//集合2的差集
                        Set<String> start_Exclusive1 = new TreeSet<String>();
                        Set<String> start_Exclusive2 = new TreeSet<String>();
                        Set<String> end_exclusive1 = new TreeSet<String>();
                        Set<String> end_exclusive2 = new TreeSet<String>();
                        for(String v1:value1){
                            for(String v2:value2){
                                if(v1.split(",")[0].equals(v2.split(",")[0])){//两个选择分支的生成任务相同
                                    start_Exclusive1.add(v1);
                                    start_Exclusive2.add(v2);
                                }
                                if(v1.split(",")[1].equals(v2.split(",")[1])){
                                    end_exclusive1.add(v1);
                                    end_exclusive2.add(v2);
                                }
                            }
                        }

                        if(!value1.isEmpty()){
                            addRelation(value1, relations);
                            getExclusiveRelation(e, start_Exclusive1, end_exclusive1, relations);
                        }
                        if(!value2.isEmpty()){
                            addRelation(value2, relations);//先将差集分别添加为选择关系
                            //为了处理选择分支上的循环结构，如果选择分支上有循环结构，也再加上
                            getExclusiveRelation(entry, start_Exclusive2, end_exclusive2, relations);//从原log中将分支上的token加入选择关系
                        }
                    }

                }
            }

        }

    }

    private static void getExclusiveRelation(Map.Entry<List<Token>, Set<String>> entry, Set<String> start_exclusive, Set<String> end_exclusive, HashMap<HashMap<String, String>, String> relations) {
//        从选择分支开始，将分支上的任务加入选择关系
        List<Token> tokenList = entry.getKey();
        Set<String> value = entry.getValue();
        Collections.sort(tokenList, new SortByProTime());
        if(!start_exclusive.isEmpty() && !end_exclusive.isEmpty()){
            for(int i = 0; i < tokenList.size(); i++){
                if(start_exclusive.contains(tokenList.get(i).getProName()+","+tokenList.get(i).getConName())){
                    putRelation(tokenList.get(i), relations, "E");
//                    System.out.println(tokenList.get(i));
                    while(!end_exclusive.contains(tokenList.get(i).getProName()+","+tokenList.get(i).getConName()) && i<tokenList.size()-1){
                        i = i + 1;
                        putRelation(tokenList.get(i), relations, "E");
//                        System.out.println(tokenList.get(i));
                    }
                }
            }
        }
    }

    private static void addRelation(Set<String> result1, HashMap<HashMap<String, String>, String> relations) {
        for(String s:result1){
            HashMap<String, String> k = new HashMap<>();
//            String pro = s.split(",")[0]
            k.put(s.split(",")[0], s.split(",")[1]);
            String value = "E";
            if(relations.containsKey(k)){//����������Ѿ����ڣ����ϵ�����ں��
                if(!relations.get(k).contains("E")){
                    value = relations.get(k) + "," + value;
                }else{
                    value = relations.get(k);
                }
            }
            relations.put(k, value);
        }
    }
}
