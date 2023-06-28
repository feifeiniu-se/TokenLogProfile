package cn.edu.nju.tlpsimilar.extractRelations;

import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import java.util.*;

import static cn.edu.nju.tlpsimilar.extractRelations.GetConcurrent.isOrder;
import static cn.edu.nju.tlpsimilar.extractRelations.GetRelations.putRelation;

public class GetLoop {
    public static void getLoop(Map.Entry<List<Token>, Set<String>> e, HashMap<HashMap<String, String>, String> relations, Set<Token> start_loop, Set<Token> end_loop){
//        System.out.println(start_loop);
//        System.out.println(end_loop);
//        获得循环结构之后，将value设为不包含循环结构的部分，这样便于下一步识别选择结构
        List<Token> key = e.getKey();
        Set<String> value = new TreeSet<>();
        Collections.sort(key, new SortByProTime());//��������ʱ������
        for(Token s:start_loop){
//            System.out.println(s);
            putRelation(s, relations, "L");
            value = e.getValue();
            value.remove(s.getProName()+","+s.getConName());
            e.setValue(value);
            int i = key.indexOf(s);
            int j = i + 1;
            while(!end_loop.contains(key.get(j)) && j<key.size()){
                if(isOrder(key.get(i), key.get(j))){
//                    System.out.println(key.get(j));
                    putRelation(key.get(j), relations, "L");
                    value = e.getValue();
                    value.remove(key.get(j).getProName()+","+key.get(j).getConName());
                    e.setValue(value);
//                    System.out.println(relations);
                    i = j;
                }else{
                    j = j + 1;
                }
            }
            if(j>=key.size()){
                System.out.println("fault in getLoop relation");
            }
        }
    }
}
